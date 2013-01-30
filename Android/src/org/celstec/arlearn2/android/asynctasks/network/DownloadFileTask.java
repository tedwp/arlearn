package org.celstec.arlearn2.android.asynctasks.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.activities.ListExcursionsActivity;
import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;

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
		if (currentlyRunning) return;
		currentlyRunning = true;
		this.ctx = ctx;
		NetworkTaskHandler nwHandler = NetworkQueue.getNetworkTaskHandler();
		if (!nwHandler.hasMessages(NetworkTaskHandler.SYNC_UPLOAD_MEDIA)) {
			Message m = Message.obtain(nwHandler);
			m.obj = this;
			m.what = NetworkTaskHandler.SYNC_UPLOAD_MEDIA;
			m.sendToTarget();
		}
	}

	@Override
	public void execute() {
		LoadItemFromDatabase loadItem = new LoadItemFromDatabase();

		loadItem.run(ctx);
	}

	private void startUpload() {
		try {
			setReplicationStatus(MediaCacheGeneralItems.REP_STATUS_SYNCING);
			downloadItem.setLocalPath(Uri.fromFile(new File(downloadFile(downloadItem.getRemoteUrl(), downloadItem))));
			setReplicationStatus(MediaCacheGeneralItems.REP_STATUS_DONE);
		} catch (FileNotFoundException fnf) {
			setReplicationStatus(MediaCacheGeneralItems.REP_STATUS_TODO);
			fnf.printStackTrace();
		}

		ActivityUpdater.updateActivities(ctx, ListExcursionsActivity.class.getCanonicalName(), ListMessagesActivity.class.getCanonicalName(), ListMapItemsActivity.class.getCanonicalName());
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
			}
		};
		m.sendToTarget();
	}

	// private HashMap<String, String> getRemoteFile(GeneralItem gi) {
	// HashMap<String, String> hm = new HashMap<String, String>();
	// if (gi instanceof AudioObject) {
	// hm.put("audio", ((AudioObject) gi).getAudioFeed());
	// }
	// if (gi instanceof VideoObject) {
	// hm.put("video", ((VideoObject) gi).getVideoFeed());
	// }
	// if (gi instanceof SingleChoiceImageTest) {
	// SingleChoiceImageTest test = (SingleChoiceImageTest) gi;
	// int i = 0;
	// for (MultipleChoiceAnswerItem imageAnswer: test.getAnswers()){
	// MultipleChoiceImageAnswerItem mciai = (MultipleChoiceImageAnswerItem)
	// imageAnswer;
	// hm.put(mciai.getId(), mciai.getImageUrl());
	// if (mciai.getAudioUrl() != null) {
	// hm.put(mciai.getId()+":a", mciai.getAudioUrl());
	//
	// }
	// }
	// }
	// return hm;
	// }

	private class LoadItemFromDatabase extends GenericTask implements DatabaseTask {

	
		@Override
		public void execute(DBAdapter db) {
			downloadItem = db.getMediaCacheGeneralItems().getNextUnsyncedItem(gameId);
			if (downloadItem != null) {
				startUpload();
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
				}
			}
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
			suffix = url.substring(index+1, url.length());
		} else {
			return "";
		}
		if (suffix.contains("/") || suffix.contains("\\")) {
			return "";
		}
		return suffix;
	}

}
