package org.celstec.arlearn2.android.broadcast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCache;
import org.celstec.arlearn2.android.db.beans.MediaCacheItem;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.util.AppengineFileUploadHandler;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
		syncronize(this);
	}

	private void process(Context context, long runId, long currentTime, Uri recordingPath, Uri imagePath, Uri videoUri, String username) {
		DBAdapter db = new DBAdapter(context);
		db.openForWrite();
		MediaCache mc = ((MediaCache) db.table(DBAdapter.MEDIA_CACHE));
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
		db.close();

	}

	private MediaCacheItem createMediaCacheAudioItem(long currentTime, long runId, Uri recordingPath, String username) {
		MediaCacheItem mci = new MediaCacheItem();
		mci.setItemId(MediaCacheItem.getAudioId(runId, currentTime));

		// mci.setItemId(runId + ":audio:" + currentTime);
		// mci.setLocalFile(recordingPath);
		mci.setUri(recordingPath);

		mci.setRunId(runId);
		mci.setAccount(username);
		mci.setMimetype("audio/AMR");
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
		// mci.setMimetype("image/jpeg");
		mci.setMimetype(getContentResolver().getType(imagePath));

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

	private void syncronize(final Context context) {
		new Thread(new Runnable() {
			public void run() {

				DBAdapter db = null;
				try {
					db = new DBAdapter(context);
					db.openForWrite();
					MediaCache mc = ((MediaCache) db.table(DBAdapter.MEDIA_CACHE));
					mc.reset();
					MediaCacheItem[] mcis;

					mcis = mc.getNextUnsyncedItems();
					CountDownLatch latch = new CountDownLatch(mcis.length);
					for (int i = 0; i < mcis.length; i++) {

						if (mcis[i].isIncomming()) {
							synchronizeIncommingFile(mcis[i], mc, latch);
						} else {
							synchronizeOutgoingUri(context, mcis[i], mc, latch);
						}
					}

					
					latch.await();
					db.close();
				} catch (InterruptedException e) {
					e.printStackTrace();
					if (db != null)
						db.close();
				} finally {
					// syncing = false;
				}
			}
		}).start();

	}

	private void synchronizeIncommingFile(final MediaCacheItem mci, final MediaCache mc, final CountDownLatch latch) {
		mc.setReplicationStatus(mci.getItemId(), MediaCache.REP_STATUS_SYNCING);
		new Thread(new Runnable() {
			public void run() {
				try {
					String localPath = downloadFile(mci.getRemoteFile());
					mc.updateLocalPath(mci.getItemId(), localPath);
					if (localPath == null) {
						mc.setReplicationStatus(mci.getItemId(), MediaCache.REP_STATUS_TODO);
					}
				} catch (FileNotFoundException fnf) {
					mc.setReplicationStatus(mci.getItemId(), MediaCache.REP_STATUS_TODO);
					mc.updateLocalPath(mci.getItemId(), null);
				} finally {
					latch.countDown();
				}

			}
		}).start();
	}

	private void synchronizeOutgoingUri(final Context context, final MediaCacheItem mci, final MediaCache mc, final CountDownLatch latch) {
		// File localFile = new File(mci.getLocalFile());
		mc.setReplicationStatus(mci.getItemId(), MediaCache.REP_STATUS_SYNCING);
		mc.updateRemotePath(mci.getItemId(), mci.buildRemotePath(mci.getUri()));
		new Thread(new Runnable() {
			public void run() {
				try {
					updateActivities(context, NarratorItemActivity.class.getCanonicalName());
					boolean successful = publishData(mci.getRunId(), mci.getAccount(), mci.getUri(), mci.getToken(), mci.getMimetype());
					if (successful) {
						mc.setReplicationStatus(mci.getItemId(), MediaCache.REP_STATUS_DONE);
						updateActivities(context, NarratorItemActivity.class.getCanonicalName());
					} else {
						mc.setReplicationStatus(mci.getItemId(), MediaCache.REP_STATUS_TODO);
					}
				} finally {
					latch.countDown();
				}
			}
		}).start();
		// return successful;

	}

	private boolean publishData(Long runId, String account, Uri uri, String token, String mimeType) {
		AppengineFileUploadHandler uploadHandler = new AppengineFileUploadHandler(this, uri, mimeType, token, runId, account);
		uploadHandler.startUpload();
		return !uploadHandler.endedwithError();
	}

	private String downloadFile(String url) throws FileNotFoundException {
		try {
			URL myFileUrl = new URL(url);
			File outputFile = urlToCacheFile(url);
			HttpURLConnection.setFollowRedirects(false); // new
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			// if (url.contains("sites.google.com") && token !=null) {
			// conn.setRequestProperty("Authorization","GoogleLogin auth=" +
			// token);
			// }
			if (conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
				String newUrl = conn.getHeaderField("location");
				conn.disconnect();
				return downloadFile(newUrl);
			}
			conn.connect();

			
			InputStream is = conn.getInputStream();
			// File cacheDir = getCacheDir();
			String cLength = conn.getHeaderField("Content-Length");
			int contentLength = 0;
			DBAdapter db = new DBAdapter(this);
			db.openForWrite();
			MediaCache mc = ((MediaCache) db.table(DBAdapter.MEDIA_CACHE));
			if (cLength != null) {
				 contentLength = Integer.parseInt(cLength);
				mc.registerTotalAmountofBytes(url, contentLength);
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
					Log.e("GAME", "startTime "+startTime+ "");
					startTime = System.currentTimeMillis();
					mc.registerBytesAvailable(url, contentLength-byteCounter);
					updateActivities(this, ListMessagesActivity.class.getCanonicalName(), ListMapItemsActivity.class.getCanonicalName());
				}
			}
			db.close();
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

	private File getCacheDir2() {
		File sdcard = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		File cacheDirFile = new File(sdcard, Constants.CACHE_DIR);
		if (!cacheDirFile.exists())
			cacheDirFile.mkdir();
		File incommingDir = new File(cacheDirFile, Constants.INCOMMING);
		if (!incommingDir.exists())
			incommingDir.mkdir();
		return incommingDir;
	}

	private File urlToCacheFile(String url) {
		String urlRet = url.hashCode() + getURLSuffix(url);
		if (urlRet.contains("?"))
			urlRet = urlRet.substring(0, urlRet.indexOf("?"));
		return new File(getCacheDir2(), urlRet);

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

	protected void updateActivities(Context ctx, String... activities) {
		Intent updateIntent = new Intent();
		updateIntent.setAction("org.celstec.arlearn.updateActivities");
		for (int i = 0; i < activities.length; i++) {
			updateIntent.putExtra(activities[i], true);
		}
		ctx.sendBroadcast(updateIntent);
	}

}
