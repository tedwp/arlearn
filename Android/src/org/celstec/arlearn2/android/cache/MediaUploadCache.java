package org.celstec.arlearn2.android.cache;

import java.util.HashMap;

import org.celstec.arlearn2.android.db.MediaCacheUpload;
import org.celstec.arlearn2.android.db.MediaCacheUpload.UploadItem;

import android.net.Uri;

public class MediaUploadCache  extends GenericCache {
	
	private static HashMap<Long, MediaUploadCache> instance = new HashMap<Long, MediaUploadCache>();
	
	private HashMap<String, Integer> replicationStatus = new HashMap<String, Integer>();
	private HashMap<String, Long> totalBytes = new HashMap<String, Long>();
	private HashMap<String, Long> bytesUploaded = new HashMap<String, Long>();
	
	private HashMap<String, Uri> remoteUriToLocalURI = new HashMap<String, Uri>();


	private MediaUploadCache() {

	}
	
	public static MediaUploadCache getInstance(long runId) {
		if (!instance.containsKey(runId)) {
			instance.put(runId, new MediaUploadCache());
		}
		return instance.get(runId);
	}

	public void put(UploadItem ui) {
		put(ui.getRemoteUrl(), ui.getReplicated());
		synchronized (remoteUriToLocalURI) {
			remoteUriToLocalURI.put(ui.getRemoteUrl(), ui.getUri());
		}
	}
	
	public void put(String remoteUrl, int replicationStatusInt) {
		synchronized (replicationStatus) {
			replicationStatus.put(remoteUrl, replicationStatusInt);
		}
	}
	
	public int getReplicationStatus(String remoteUrl) {
		Integer result = replicationStatus.get(remoteUrl);
		if (result != null)
			return result;
		return MediaCacheUpload.REP_STATUS_DONE;
	}
	
	public double getPercentageUploaded(String remoteUrl) {
		Long totalBytesLong = totalBytes.get(remoteUrl);
		Long bytesUploadedLong = bytesUploaded.get(remoteUrl);
		if (totalBytesLong == null || bytesUploadedLong == null) return 1d;
		if (totalBytesLong == 0) return 1d;
		return ((double)bytesUploadedLong)/ ((double)totalBytesLong);
	}
	
	public Uri getLocalUri(String remoteFile) {
		return remoteUriToLocalURI.get(remoteFile);//TODO 
	}

	public void remove() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
