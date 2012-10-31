package org.celstec.arlearn2.android.cache;

import java.util.HashMap;

import org.celstec.arlearn2.android.db.beans.MediaCacheItem;

public class MediaCache {

private static MediaCache instance;
	
	private HashMap<String, Integer> replicationStatus = new HashMap<String, Integer>();
	private HashMap<String, Double> percentageUploaded = new HashMap<String, Double>();
	private HashMap<String, MediaCacheItem> idToMediaCacheItem = new HashMap<String, MediaCacheItem>();
	
	private MediaCache() {
		
	}
	
	public static MediaCache getInstance() {
		if (instance == null) {
			instance = new MediaCache();
		}
		return instance;
	}
	
	public Integer getReplicationStatus(String remotePath) {
		Integer result = replicationStatus.get(remotePath); 
		if (result != null) return result;
		return  org.celstec.arlearn2.android.db.MediaCache.REP_STATUS_DONE;
	}
	
	public Double getPercentageUploaded(String remotePath) {
		Double result =  percentageUploaded.get(remotePath);
		if (result != null) return result;
		return 1d;
	}
	
	public MediaCacheItem getMediaCacheItem(String id) {
		return idToMediaCacheItem.get(id);
	}
	
//	public void reloadFromDb(final long runId, final Context ctx) {
//		DBAdapter.getAdapter(ctx).getMediaCache().queryUploadStatus(runId, new UploadStatus() {
//			
//			@Override
//			public void onResults(String remoteFilePath, int repStatus, double percentage) {
//					
//			}
//		}); 
//	}
	
	public void putReplicationstatus (String remoteFilePath, int repStatus) {
		replicationStatus.put(remoteFilePath, repStatus);
	}
	
	public void putPercentageUploaded(String remoteFilePath, Double percentage) {
		percentageUploaded.put(remoteFilePath, percentage);
	}

	public void put(MediaCacheItem mci, double percentage) {
		replicationStatus.put(mci.getRemoteFile(), mci.getReplicated());
		percentageUploaded.put(mci.getRemoteFile(), percentage);
		idToMediaCacheItem.put(mci.getItemId(), mci);
		
	}
	
}
