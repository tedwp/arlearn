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
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.cache.MediaCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCacheGeneralItems;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceImageAnswerItem;
import org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest;
import org.celstec.arlearn2.beans.generalItem.VideoObject;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

public class DownloadFileTask implements NetworkTask{

	private HashMap<String, String> urls;
	public Context ctx;
	public Long itemId;
	public String localId;
	public String[] allIds;
	public Long gameId;
	private boolean restartTask = false;
	
	@Override
	public void execute() {
			if (itemId == null) {
				setItemId();
			}
			if (itemId == null)
				return;
			GeneralItem gi = GeneralItemsCache.getInstance().getGeneralItems(itemId);
			if (gi != null) {
				urls = getRemoteFile(gi);
				
				if (!urls.isEmpty()) {
					for (String key : urls.keySet()) {
						try {
							setReplicationStatus(itemId, key,MediaCacheGeneralItems.REP_STATUS_SYNCING, null);
							Uri localUri = Uri.fromFile(new File(downloadFile(urls.get(key), key)));
							setReplicationStatus(itemId, key, MediaCacheGeneralItems.REP_STATUS_DONE, localUri);
							MediaCache.getInstance().localToDownloadRemove(gameId, itemId, key);
						} catch (FileNotFoundException fnf) {
							setReplicationStatus(itemId, key, MediaCacheGeneralItems.REP_STATUS_TODO, null);
							fnf.printStackTrace();
						} 
					}
				}
			}
		if (restartTask) {
			this.itemId = null;
			Message m = Message.obtain(NetworkQueue.getNetworkTaskHandler());
			m.obj = this;
			m.sendToTarget();
		}
		ActivityUpdater.updateActivities(ctx, 
				ListExcursionsActivity.class.getCanonicalName(), 
				ListMessagesActivity.class.getCanonicalName(), 
				ListMapItemsActivity.class.getCanonicalName());
	}
	
	private void setItemId() {
		for (int i = 0; i < allIds.length; i++) {
			if (allIds[i] != null) {
				itemId = MediaCache.itemIdFromKey(allIds[i]);
				localId =MediaCache.localIdFromKey(allIds[i]); 
				allIds[i] = null;
				restartTask = true;
				return;
			}
		}
		
	}

	private void setReplicationStatus(final Long itemId, final String localId, final int status, final Uri localUri) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = new DBAdapter.DatabaseTask() {
			@Override
			public void execute(DBAdapter db) {
				db.getMediaCacheGeneralItems().setReplicationStatus(itemId, localId, status, localUri);
			}
		};
		m.sendToTarget();
	}
	
	private HashMap<String, String> getRemoteFile(GeneralItem gi) {
		HashMap<String, String> hm = new HashMap<String, String>();
		if (gi instanceof AudioObject) {
			hm.put("audio", ((AudioObject) gi).getAudioFeed());
		}
		if (gi instanceof VideoObject) {
			hm.put("video", ((VideoObject) gi).getVideoFeed());
		}
		if (gi instanceof SingleChoiceImageTest) {
			SingleChoiceImageTest test = (SingleChoiceImageTest) gi;
			int i = 0;
			for (MultipleChoiceAnswerItem imageAnswer: test.getAnswers()){
				MultipleChoiceImageAnswerItem mciai = (MultipleChoiceImageAnswerItem) imageAnswer;
				hm.put(mciai.getId(), mciai.getImageUrl());
				if (mciai.getAudioUrl() != null) {
					hm.put(mciai.getId()+":a", mciai.getAudioUrl());

				}
			}
		}
		return hm;
	}
	
	private String downloadFile(String url, String localId) throws FileNotFoundException {
		try {
			
			URL myFileUrl = new URL(url);
			File outputFile = urlToCacheFile(url);
			HttpURLConnection.setFollowRedirects(false); // new
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			if (conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
				url = conn.getHeaderField("location");
				conn.disconnect();
				return downloadFile(url, localId);
			}
			conn.connect();

			
			InputStream is = conn.getInputStream();
			// File cacheDir = getCacheDir();
			String cLength = conn.getHeaderField("Content-Length");
			int contentLength = 0;

			if (cLength != null) {
				 contentLength = Integer.parseInt(cLength);
				 DBAdapter.getAdapter(ctx).getMediaCache().registerTotalAmountofBytes(url, contentLength);
			}
			
			FileOutputStream fos = new FileOutputStream(outputFile);
			int len1;
			long byteCounter = 0;
			byte[] buffer = new byte[1024];
			long startTime= System.currentTimeMillis();
			while ((len1 = is.read(buffer)) > 0) {
				fos.write(buffer, 0, len1);
				byteCounter +=1024;
				if ((startTime + 1500)<System.currentTimeMillis()) {
					startTime = System.currentTimeMillis();
//					DBAdapter.getAdapter(ctx).getMediaCache().registerBytesAvailable(url, contentLength-byteCounter);
					MediaCache.getInstance().setBytesUploaded(itemId, localId, byteCounter);
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
		File gameDir = new File(incommingDir, ""+gameId);
		if (!gameDir.exists())
			gameDir.mkdir();
		
		return gameDir;
	}
	
	private String getURLSuffix(String url) {
		String suffix = "";
		int index = url.lastIndexOf("/");
		if (index != -1) {
			suffix = url.substring(index, url.length());
		} else {
			return "";
		}
		if (suffix.contains("/") || suffix.contains("\\")) {
			return "";
		}
		return suffix;
	}

}
