package org.celstec.arlearn2.android.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.celstec.arlearn2.android.db.beans.MediaCacheItem;

import android.net.Uri;

public class MediaCache {

	private static MediaCache instance;

	private HashMap<String, Integer> replicationStatus = new HashMap<String, Integer>();
	private HashMap<String, Double> percentageUploaded = new HashMap<String, Double>();
	private HashMap<String, Long> totalBytes = new HashMap<String, Long>();
	private HashMap<String, Long> bytesUploaded = new HashMap<String, Long>();
	private HashMap<String, MediaCacheItem> idToMediaCacheItem = new HashMap<String, MediaCacheItem>();

	private HashMap<Long, Uri> itemIdToURI = new HashMap<Long, Uri>();

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
		if (result != null)
			return result;
		return org.celstec.arlearn2.android.db.MediaCache.REP_STATUS_DONE;
	}

	public Double getPercentageUploaded(String remotePath) {
		Double result = percentageUploaded.get(remotePath);
		if (result != null)
			return result;
		return 1d;
	}

	public MediaCacheItem getMediaCacheItem(String id) {
		return idToMediaCacheItem.get(id);
	}

	// public void reloadFromDb(final long runId, final Context ctx) {
	// DBAdapter.getAdapter(ctx).getMediaCache().queryUploadStatus(runId, new
	// UploadStatus() {
	//
	// @Override
	// public void onResults(String remoteFilePath, int repStatus, double
	// percentage) {
	//
	// }
	// });
	// }

	public void putReplicationstatus(String remoteFilePath, int repStatus) {
		synchronized (replicationStatus) {
		replicationStatus.put(remoteFilePath, repStatus);
		}
	}

	public void putPercentageUploaded(String remoteFilePath, Double percentage) {
		synchronized (percentageUploaded) {
			percentageUploaded.put(remoteFilePath, percentage);
		}
	}

	public void put(MediaCacheItem mci, double percentage) {
		synchronized (replicationStatus) {
			replicationStatus.put(mci.getRemoteFile(), mci.getReplicated());
		}
		synchronized (percentageUploaded) {
			percentageUploaded.put(mci.getRemoteFile(), percentage);
		}
		synchronized (idToMediaCacheItem) {
			idToMediaCacheItem.put(mci.getItemId(), mci);
		}
	}

	public void putLocalURI(Long itemId, Uri uri) {
		synchronized (itemIdToURI) {
			itemIdToURI.put(itemId, uri);
		}
	}

	public Uri getLocalUri(Long itemId) {
		return itemIdToURI.get(itemId);
	}

	private HashMap<Long, Set<Long>> toDownloadItems = new HashMap<Long, Set<Long>>();

	public void localToDownload(long gameId, long itemId) {
		if (toDownloadItems.get(gameId) == null) {
			synchronized (toDownloadItems) {
				toDownloadItems.put(gameId, new HashSet<Long>());
			}
		}
		toDownloadItems.get(gameId).add(itemId);
	}

	public void localToDownloadRemove(long gameId, long itemId) {
		if (toDownloadItems.get(gameId) == null) {
			synchronized (toDownloadItems) {
				toDownloadItems.put(gameId, new HashSet<Long>());
			}
		}
		toDownloadItems.get(gameId).remove(itemId);
	}

	public Set<Long> getToDownloadItems(Long gameId) {
		return toDownloadItems.get(gameId);
	}

	public int getAmountOfItemsToDownload(long gameId) {
		Set<Long> itemsToDL = toDownloadItems.get(gameId);
		if (itemsToDL == null)
			return 0;
		return itemsToDL.size();
	}

	public void setTotalBytes(Long itemId, Long bytesAmount) {
		synchronized (totalBytes) {
			totalBytes.put("" + itemId, bytesAmount);
		}
	}

	public void setBytesUploaded(Long itemId, Long bytesAmount) {
		synchronized (bytesUploaded) {
			bytesUploaded.put("" + itemId, bytesAmount);

		}
	}

}
