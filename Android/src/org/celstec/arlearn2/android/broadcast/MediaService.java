package org.celstec.arlearn2.android.broadcast;

import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.DownloadFileTask;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTaskHandler;
//import org.celstec.arlearn2.android.asynctasks.network.UploadFileTask;
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

//	public static String USERNAME = "username";
//	public static String RECORDING_PATH = "recordingPath";
//	public static String IMAGE_PATH = "imagePath";
//	public static String VIDEO_URI = "videoUri";
//	public static String NEW_MEDIA = "newMEDIA";
//	public static String CURRENT_TIME = "currentTime";
//	public static String RUNID = "runId";
//
//
//
	public MediaService() {
		super("Media item service");
	}
//
//	@Override
//	protected void onHandleIntent(Intent intent) {
//		Bundle extras = intent.getExtras();
//		if (extras != null) {
//			if (extras.getBoolean(NEW_MEDIA, false)) {
//				long runId = extras.getLong(RUNID);
//				String username = extras.getString(USERNAME);
//				Uri recordingPath = (Uri) extras.getParcelable(RECORDING_PATH);
//				// String imagePath = extras.getString(IMAGE_PATH);
//				Uri imagePath = (Uri) extras.getParcelable(IMAGE_PATH);
//				Uri videoUri = (Uri) extras.getParcelable(VIDEO_URI);
//				long currentTime = extras.getLong(CURRENT_TIME);
//				process(this, runId, currentTime, recordingPath, imagePath, videoUri, username);
//			}
//		}
//		syncronize();
//	}
//
//	private void process(Context context, long runId, long currentTime, Uri recordingPath, Uri imagePath, Uri videoUri, String username) {
//		MediaCache mc = DBAdapter.getAdapter(context).getMediaCache();
//		MediaCacheItem mci = null;
//		if (recordingPath != null) {
//			mci = createMediaCacheAudioItem(currentTime, runId, recordingPath, username);
//		}
//		if (imagePath != null) {
//			mci = createMediaCacheImageItem(currentTime, runId, imagePath, username);
//		}
//		if (videoUri != null) {
//			mci = createMediaCacheVideoItem(currentTime, runId, videoUri, username);
//			
//		}
//		if (mci != null) {
//			mc.addOutgoingObject(mci);
//		}
//	}
//
//	private MediaCacheItem createMediaCacheAudioItem(long currentTime, long runId, Uri recordingPath, String username) {
//		MediaCacheItem mci = new MediaCacheItem();
//		mci.setItemId(Long.parseLong(MediaCacheItem.getAudioId(runId, currentTime))); //TODO adapt
//
//		// mci.setItemId(runId + ":audio:" + currentTime);
//		// mci.setLocalFile(recordingPath);
//		mci.setUri(recordingPath);
//
//		mci.setRunId(runId);
//		mci.setAccount(username);
//		mci.setMimetype(getContentResolver().getType(recordingPath));
//		if (mci.getMimetype() == null) {
//			mci.setMimetype("audio/AMR");	
//		}
//
//		
//		return mci;
//	}
//
//	private MediaCacheItem createMediaCacheImageItem(long currentTime, long runId, Uri imagePath, String username) {
//		MediaCacheItem mci = new MediaCacheItem();
//		// mci.setItemId(runId + ":img:" + currentTime);
//		mci.setItemId(Long.parseLong(MediaCacheItem.getImageId(runId, currentTime))); //TODO this ain't a long
//		// mci.setLocalFile(imagePath);
//		// mci.setLocalThumbnail(getThumbnail(imagePath));
//		mci.setUri(imagePath);
//
//		mci.setRunId(runId);
//		mci.setAccount(username);
//
//		mci.setMimetype(getContentResolver().getType(imagePath));
//		if (mci.getMimetype() == null) {
//			mci.setMimetype("image/jpeg");
//		}
//
//		return mci;
//	}
//
//	private MediaCacheItem createMediaCacheVideoItem(long currentTime, long runId, Uri videoUri, String username) {
//		MediaCacheItem mci = new MediaCacheItem();
//		// mci.setItemId(runId + ":vid:" + currentTime);
//		mci.setItemId(Long.parseLong(MediaCacheItem.getVideoId(runId, currentTime))); //TODO this ain't a long
//		mci.setUri(videoUri);
//		mci.setRunId(runId);
//		mci.setAccount(username);
//		mci.setMimetype(getContentResolver().getType(videoUri));
//		return mci;
//	}
//
//	private void syncronize() {
//		DBAdapter.getAdapter(this).getMediaCache().reset();
//		
//		Message m = Message.obtain(DBAdapter.getDatabaseThread(this));
//		m.obj =  new DBAdapter.DatabaseTask(){
//			@Override
//			public void execute(DBAdapter db) {
//				if (!NetworkQueue.getNetworkTaskHandler().hasMessages(NetworkTaskHandler.SYNC_USER_MEDIA)) {
//					db.getMediaCache().resetOnGoingSyncs();
//				}
//				MediaCacheItem[] mcis = db.getMediaCache().getNextUnsyncedItems();
//				for (int i = 0; i < mcis.length; i++) {
//					if (!mcis[i].isIncomming()) {
//						db.getMediaCache().setReplicationStatus(db, ""+mcis[i].getItemId(), MediaCache.REP_STATUS_SYNCING);
//						synchronizeOutgoingUri(mcis[i]);
//					}
//				}
//			}
//		};
//		m.sendToTarget();
//	}
//
//	private void synchronizeOutgoingUri(final MediaCacheItem mci) {
//		// File localFile = new File(mci.getLocalFile());
//		DBAdapter.getAdapter(this).getMediaCache().updateRemotePath(""+mci.getItemId(), mci.buildRemotePath(mci.getUri()));
//		
//		UploadFileTask task = new UploadFileTask();
//		task.mimeType = mci.getMimetype();
//		task.token = mci.getToken();
//		task.runId = mci.getRunId();
//		task.account = mci.getAccount();
//		task.fileName =  mci.getUri().getLastPathSegment();
//		task.uri = mci.getUri();
//		task.ctx = this;
//		task.mcItemId = ""+mci.getItemId();
//		
//		Message m = Message.obtain(NetworkQueue.getNetworkTaskHandler());
//		m.what = NetworkTaskHandler.SYNC_USER_MEDIA;
//		m.obj = task;
//		m.sendToTarget();
//	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		
	}

}
