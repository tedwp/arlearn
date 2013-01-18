package org.celstec.arlearn2.android.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

	private HashMap<String, Uri> itemIdToURI = new HashMap<String, Uri>();

	private HashMap<Long, Set<String>> toDownloadItems = new HashMap<Long, Set<String>>();
	
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

	public MediaCacheItem getMediaCacheItem(Long itemId, String localId) {
		return idToMediaCacheItem.get(getKey(itemId, localId));
	}

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
			idToMediaCacheItem.put(getKey(mci.getItemId(), mci.getLocalId()), mci);
		}
	}

	public void remove(long runId) {
		synchronized (idToMediaCacheItem) {
			synchronized (replicationStatus) {
				synchronized (percentageUploaded) {
					for (Iterator<MediaCacheItem> iterator = idToMediaCacheItem.values().iterator(); iterator.hasNext();) {
						MediaCacheItem mci = iterator.next();
						if (mci.getRunId() == runId) {
							percentageUploaded.remove(mci.getRemoteFile());
							replicationStatus.remove(mci.getRemoteFile());
							iterator.remove();
						}
					}
				}
			}
		}
	}

	public void putLocalURI(Long itemId, String localId, Uri uri) {
		synchronized (itemIdToURI) {
			itemIdToURI.put(getKey(itemId, localId), uri);
		}
	}

	public Uri getLocalUri(Long itemId, String localId) {
		return itemIdToURI.get(getKey(itemId, localId));
	}

	public void localToDownload(long gameId, long itemId, String localId) {
		if (toDownloadItems.get(gameId) == null) {
			synchronized (toDownloadItems) {
				toDownloadItems.put(gameId, new HashSet<String>());
			}
		}
		toDownloadItems.get(gameId).add(getKey(itemId, localId));
	}

	public void localToDownloadRemove(long gameId, long itemId, String localId) {
		if (toDownloadItems.get(gameId) == null) {
			synchronized (toDownloadItems) {
				toDownloadItems.put(gameId, new HashSet<String>());
			}
		}
		toDownloadItems.get(gameId).remove(getKey(itemId, localId));
	}

	public Set<String> getToDownloadItems(Long gameId) {
		return toDownloadItems.get(gameId);
	}

	public int getAmountOfItemsToDownload(long gameId) {
		Set<String> itemsToDL = toDownloadItems.get(gameId);
		if (itemsToDL == null)
			return 0;
		return itemsToDL.size();
	}

	public void setTotalBytes(Long itemId, String localId, Long bytesAmount) {
		synchronized (totalBytes) {
			totalBytes.put(getKey(itemId, localId), bytesAmount);
		}
	}

	public void setBytesUploaded(Long itemId, String localId, Long bytesAmount) {
		synchronized (bytesUploaded) {
			bytesUploaded.put(getKey(itemId, localId), bytesAmount);

		}
	}
	
	private static String getKey(Long itemId, String localId) {
		return itemId +":" + localId;
	}
	
	public static Long itemIdFromKey(String key) {
		return Long.parseLong(key.substring(0, key.indexOf(":")));
	}
	
	public static String localIdFromKey(String key) {
		return key.substring(key.indexOf(":")+1);
	}
}
