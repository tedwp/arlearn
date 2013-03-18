/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.android.asynctasks.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.HashMap;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.ListRunsParticipateActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.asynctasks.download.DownloadQueue;
import org.celstec.arlearn2.android.asynctasks.download.DownloadTaskHandler;
import org.celstec.arlearn2.android.broadcast.NetworkSwitcher;
import org.celstec.arlearn2.android.cache.MediaGeneralItemCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCacheGeneralItems;
import org.celstec.arlearn2.android.db.MediaCacheGeneralItems.DownloadItem;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

public class DownloadFileTask extends GenericTask implements NetworkTask {

	private HashMap<String, String> urls;
	private static boolean currentlyRunning;
	public Context ctx;
	public DownloadItem[] allDownloadItems;
	public DownloadItem downloadItem;
	public Long gameId;

	@Override
	public void run(Context ctx) {
		if (NetworkSwitcher.isOnline(ctx)) {
			if (currentlyRunning)
				return;
			currentlyRunning = true;
			this.ctx = ctx;

			LoadItemFromDatabase loadItem = new LoadItemFromDatabase();
			loadItem.run(ctx);

		}
	}

	@Override
	public void execute() {
		// LoadItemFromDatabase loadItem = new LoadItemFromDatabase();
		//
		// loadItem.run(ctx);
		// }
		//
		// private void startUpload() {
		try {
			setReplicationStatus(MediaCacheGeneralItems.REP_STATUS_SYNCING);
			downloadItem.setLocalPath(Uri.fromFile(new File(downloadFile(downloadItem.getRemoteUrl(), downloadItem))));
			setReplicationStatus(MediaCacheGeneralItems.REP_STATUS_DONE);
		} catch (FileNotFoundException fnf) {
			setReplicationStatus(MediaCacheGeneralItems.REP_STATUS_FILE_NOT_FOUND);
			fnf.printStackTrace();
		} catch (Exception e) {
			setReplicationStatus(MediaCacheGeneralItems.REP_STATUS_TODO);
		} 

		exitTask();
		run(ctx); // run again
	}

	private void exitTask() {
		currentlyRunning = false;
	}

	private void setReplicationStatus(final int status) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = new DBAdapter.DatabaseTask() {
			@Override
			public void execute(DBAdapter db) {
				db.getMediaCacheGeneralItems().setReplicationStatus(gameId, downloadItem, status);
				ActivityUpdater.updateActivities(ctx, ListRunsParticipateActivity.class.getCanonicalName(), ListMessagesActivity.class.getCanonicalName(), ListMapItemsActivity.class.getCanonicalName());

			}
		};
		m.sendToTarget();
	}

	private class LoadItemFromDatabase extends GenericTask implements DatabaseTask {

		@Override
		public void execute(DBAdapter db) {
			downloadItem = db.getMediaCacheGeneralItems().getNextUnsyncedItem(gameId);
			if (downloadItem != null) {
				DownloadTaskHandler nwHandler = DownloadQueue.getNetworkTaskHandler();
				if (!nwHandler.hasMessages(DownloadTaskHandler.SYNC_UPLOAD_MEDIA)) {
					Message m = Message.obtain(nwHandler);
					m.obj = DownloadFileTask.this;
					m.what = DownloadTaskHandler.SYNC_UPLOAD_MEDIA;
					m.sendToTarget();
				}

			} else {
				exitTask();
			}
		}

		@Override
		protected void run(Context ctx) {
			Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
			m.obj = this;
			m.sendToTarget();
		}
	}

	private String downloadFile(String url, DownloadItem di) throws FileNotFoundException {
		try {

			URL myFileUrl = new URL(url);
			File outputFile = urlToCacheFile(url);
			HttpURLConnection.setFollowRedirects(false); // new
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			if (conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
				url = conn.getHeaderField("location");
				conn.disconnect();
				return downloadFile(url, di);
			}
			conn.connect();

			InputStream is = conn.getInputStream();
			// File cacheDir = getCacheDir();
			String cLength = conn.getHeaderField("Content-Length");
			int contentLength = 0;

			if (cLength != null) {
				contentLength = Integer.parseInt(cLength);
				MediaGeneralItemCache.getInstance(gameId).registerTotalAmountofBytes(di, contentLength);
			}

			FileOutputStream fos = new FileOutputStream(outputFile);
			int len1;
			long byteCounter = 0;
			byte[] buffer = new byte[1024];
			long startTime = System.currentTimeMillis();
			while ((len1 = is.read(buffer)) > 0) {
				fos.write(buffer, 0, len1);
				byteCounter += 1024;
				if ((startTime + 1500) < System.currentTimeMillis()) {
					startTime = System.currentTimeMillis();
					MediaGeneralItemCache.getInstance(gameId).setBytesDownloaded(di, byteCounter);
					ActivityUpdater.updateActivities(ctx, ListMessagesActivity.class.getCanonicalName());
				}
			}
			MediaGeneralItemCache.getInstance(gameId).setBytesDownloaded(di, byteCounter);
			ActivityUpdater.updateActivities(ctx, ListMessagesActivity.class.getCanonicalName());
			fos.flush();
			fos.close();
			is.close();
			return outputFile.getAbsolutePath();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			Log.e("error while retrieve media item - addToCache", e.getMessage(), e);
		}
		return null;
	}

	private File urlToCacheFile(String url) {
		String urlRet = url.hashCode() + getURLSuffix(url);
		if (urlRet.contains("?"))
			urlRet = urlRet.substring(0, urlRet.indexOf("?"));
		return new File(getCacheDir2(), urlRet);

	}

	private File getCacheDir2() {
		File sdcard = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		File cacheDirFile = new File(sdcard, Constants.CACHE_DIR);
		if (!cacheDirFile.exists())
			cacheDirFile.mkdir();
		File incommingDir = new File(cacheDirFile, Constants.INCOMMING);
		if (!incommingDir.exists())
			incommingDir.mkdir();
		File gameDir = new File(incommingDir, "" + gameId);
		if (!gameDir.exists())
			gameDir.mkdir();

		return gameDir;
	}

	private String getURLSuffix(String url) {
		String suffix = "";
		int index = url.lastIndexOf("/");
		if (index != -1) {
			suffix = url.substring(index + 1, url.length());
		} else {
			return "";
		}
		if (suffix.contains("/") || suffix.contains("\\")) {
			return "";
		}
		return suffix;
	}

}
