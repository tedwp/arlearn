package org.celstec.arlearn2.android.broadcast;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.DownloadFileTask;
import org.celstec.arlearn2.android.asynctasks.network.UploadFileTask;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.beans.MediaCacheItem;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

public class MediaService extends IntentService {

	public static String USERNAME = "username";
	public static String RECORDING_PATH = "recordingPath";
	public static String IMAGE_PATH = "imagePath";
	public static String VIDEO_URI = "videoUri";
	public static String NEW_MEDIA = "newMEDIA";
	public static String CURRENT_TIME = "currentTime";
	public static String RUNID = "runId";



	public MediaService() {
		super("Media item service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			if (extras.getBoolean(NEW_MEDIA, false)) {
				long runId = extras.getLong(RUNID);
				String username = extras.getString(USERNAME);
				Uri recordingPath = (Uri) extras.getParcelable(RECORDING_PATH);
				// String imagePath = extras.getString(IMAGE_PATH);
				Uri imagePath = (Uri) extras.getParcelable(IMAGE_PATH);
				Uri videoUri = (Uri) extras.getParcelable(VIDEO_URI);
				long currentTime = extras.getLong(CURRENT_TIME);
				process(this, runId, currentTime, recordingPath, imagePath, videoUri, username);
			}
		}
		syncronize();
	}

	private void process(Context context, long runId, long currentTime, Uri recordingPath, Uri imagePath, Uri videoUri, String username) {
		MediaCache mc = DBAdapter.getAdapter(context).getMediaCache();
//		DBAdapter db = new DBAdapter(context);
//		db.openForWrite();
		if (recordingPath != null) {
			MediaCacheItem mci = createMediaCacheAudioItem(currentTime, runId, recordingPath, username);
			mc.addOutgoingObject(mci);
		}
		if (imagePath != null) {
			MediaCacheItem mci = createMediaCacheImageItem(currentTime, runId, imagePath, username);
			mc.addOutgoingObject(mci);
		}
		if (videoUri != null) {
			MediaCacheItem mci = createMediaCacheVideoItem(currentTime, runId, videoUri, username);
			mc.addOutgoingObject(mci);
		}
//		db.close();

	}

	private MediaCacheItem createMediaCacheAudioItem(long currentTime, long runId, Uri recordingPath, String username) {
		MediaCacheItem mci = new MediaCacheItem();
		mci.setItemId(MediaCacheItem.getAudioId(runId, currentTime));

		// mci.setItemId(runId + ":audio:" + currentTime);
		// mci.setLocalFile(recordingPath);
		mci.setUri(recordingPath);

		mci.setRunId(runId);
		mci.setAccount(username);
		mci.setMimetype(getContentResolver().getType(recordingPath));
		if (mci.getMimetype() == null) {
			mci.setMimetype("audio/AMR");	
		}

		
		return mci;
	}

	private MediaCacheItem createMediaCacheImageItem(long currentTime, long runId, Uri imagePath, String username) {
		MediaCacheItem mci = new MediaCacheItem();
		// mci.setItemId(runId + ":img:" + currentTime);
		mci.setItemId(MediaCacheItem.getImageId(runId, currentTime));
		// mci.setLocalFile(imagePath);
		// mci.setLocalThumbnail(getThumbnail(imagePath));
		mci.setUri(imagePath);

		mci.setRunId(runId);
		mci.setAccount(username);

		mci.setMimetype(getContentResolver().getType(imagePath));
		if (mci.getMimetype() == null) {
			mci.setMimetype("image/jpeg");
		}

		return mci;
	}

	private MediaCacheItem createMediaCacheVideoItem(long currentTime, long runId, Uri videoUri, String username) {
		MediaCacheItem mci = new MediaCacheItem();
		// mci.setItemId(runId + ":vid:" + currentTime);
		mci.setItemId(MediaCacheItem.getVideoId(runId, currentTime));
		mci.setUri(videoUri);
		mci.setRunId(runId);
		mci.setAccount(username);
		mci.setMimetype(getContentResolver().getType(videoUri));
		return mci;
	}

	private void syncronize() {
		DBAdapter.getAdapter(this).getMediaCache().reset();
		
		Message m = Message.obtain(DBAdapter.getDatabaseThread(this));
		m.obj =  new DBAdapter.DatabaseTask(){
			@Override
			public void execute(DBAdapter db) {
				MediaCacheItem[] mcis = db.getMediaCache().getNextUnsyncedItems();
				for (int i = 0; i < mcis.length; i++) {
					if (!mcis[i].isIncomming()) {
//						synchronizeIncommingFile(mcis[i]);
//					} else {
						synchronizeOutgoingUri(mcis[i]);
					}
				}

				
			}
		};
		m.sendToTarget();
	}

//	private void synchronizeIncommingFile(final MediaCacheItem mci) {
//		DBAdapter.getAdapter(this).getMediaCache().setReplicationStatus(mci.getItemId(), MediaCache.REP_STATUS_SYNCING);
//		
//		DownloadFileTask task = new DownloadFileTask();
//		task.url = mci.getRemoteFile();
//		task.ctx = this;
//		task.itemId = mci.getItemId();
//		
//		Message m = Message.obtain(NetworkQueue.getNetworkTaskHandler());
//		m.obj = task;
//		m.sendToTarget();
//	}

	private void synchronizeOutgoingUri(final MediaCacheItem mci) {
		// File localFile = new File(mci.getLocalFile());
		DBAdapter.getAdapter(this).getMediaCache().updateRemotePath(mci.getItemId(), mci.buildRemotePath(mci.getUri()));
		
		UploadFileTask task = new UploadFileTask();
		task.mimeType = mci.getMimetype();
		task.token = mci.getToken();
		task.runId = mci.getRunId();
		task.account = mci.getAccount();
		task.fileName =  mci.getUri().getLastPathSegment();
		task.uri = mci.getUri();
		task.ctx = this;
		task.mcItemId = mci.getItemId();
		
		Message m = Message.obtain(NetworkQueue.getNetworkTaskHandler());
		m.obj = task;
		m.sendToTarget();
		
	
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					updateActivities(context, NarratorItemActivity.class.getCanonicalName());
//					boolean successful = publishData(mci.getRunId(), mci.getAccount(), mci.getUri(), mci.getToken(), mci.getMimetype());
//					if (successful) {
//						mc.setReplicationStatus(mci.getItemId(), MediaCache.REP_STATUS_DONE);
//						updateActivities(context, NarratorItemActivity.class.getCanonicalName());
//					} else {
//						mc.setReplicationStatus(mci.getItemId(), MediaCache.REP_STATUS_TODO);
//					}
//				} finally {
//					latch.countDown();
//				}
//			}
//		}).start();
		// return successful;

	}

//	private boolean publishData(Long runId, String account, Uri uri, String token, String mimeType) {
//		AppengineFileUploadHandler uploadHandler = new AppengineFileUploadHandler(this, uri, mimeType, token, runId, account);
//		uploadHandler.startUpload();
//		return !uploadHandler.endedwithError();
//	}

//	private String downloadFile(String url) throws FileNotFoundException {
//		try {
//			URL myFileUrl = new URL(url);
//			File outputFile = urlToCacheFile(url);
//			HttpURLConnection.setFollowRedirects(false); // new
//			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
//			conn.setDoInput(true);
//			
//			if (conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
//				String newUrl = conn.getHeaderField("location");
//				conn.disconnect();
//				return downloadFile(newUrl);
//			}
//			conn.connect();
//
//			
//			InputStream is = conn.getInputStream();
//			String cLength = conn.getHeaderField("Content-Length");
//			int contentLength = 0;
//			DBAdapter db = new DBAdapter(this);
//			db.openForWrite();
//			MediaCache mc = ((MediaCache) db.table(DBAdapter.MEDIA_CACHE));
//			if (cLength != null) {
//				 contentLength = Integer.parseInt(cLength);
//				mc.registerTotalAmountofBytes(url, contentLength);
//			}
//			
//			FileOutputStream fos = new FileOutputStream(outputFile);
//			int len1;
//			long byteCounter = 0;
//			byte[] buffer = new byte[1024];
//			long startTime= System.currentTimeMillis();
//			while ((len1 = is.read(buffer)) > 0) {
//				fos.write(buffer, 0, len1);
//				byteCounter +=1024;
//				if ((startTime + 1500)<System.currentTimeMillis()) {
//					startTime = System.currentTimeMillis();
//					mc.registerBytesAvailable(url, contentLength-byteCounter);
//					updateActivities(this, ListMessagesActivity.class.getCanonicalName(), ListMapItemsActivity.class.getCanonicalName());
//				}
//			}
//			db.close();
//			fos.flush();
//			fos.close();
//			is.close();
//			return outputFile.getAbsolutePath();
//		} catch (FileNotFoundException e) {
//			throw e;
//		} catch (IOException e) {
//			Log.e("error while retrieve media item - addToCache", e.getMessage(), e);
//		}
//		return null;
//	}

//	private File getCacheDir2() {
//		File sdcard = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
//		File cacheDirFile = new File(sdcard, Constants.CACHE_DIR);
//		if (!cacheDirFile.exists())
//			cacheDirFile.mkdir();
//		File incommingDir = new File(cacheDirFile, Constants.INCOMMING);
//		if (!incommingDir.exists())
//			incommingDir.mkdir();
//		return incommingDir;
//	}

//	private File urlToCacheFile(String url) {
//		String urlRet = url.hashCode() + getURLSuffix(url);
//		if (urlRet.contains("?"))
//			urlRet = urlRet.substring(0, urlRet.indexOf("?"));
//		return new File(getCacheDir2(), urlRet);
//
//	}

//	private String getURLSuffix(String url) {
//		String suffix = "";
//		int index = url.lastIndexOf("/");
//		if (index != -1) {
//			suffix = url.substring(index, url.length());
//		} else {
//			return "";
//		}
//		if (suffix.contains("/") || suffix.contains("\\")) {
//			return "";
//		}
//		return suffix;
//	}

//	protected void updateActivities(Context ctx, String... activities) {
//		Intent updateIntent = new Intent();
//		updateIntent.setAction("org.celstec.arlearn.updateActivities");
//		for (int i = 0; i < activities.length; i++) {
//			updateIntent.putExtra(activities[i], true);
//		}
//		ctx.sendBroadcast(updateIntent);
//	}

}
