package org.celstec.arlearn2.android.cache;

import java.util.HashMap;

import org.celstec.arlearn2.android.db.MediaCacheGeneralItems;
import org.celstec.arlearn2.android.db.MediaCacheGeneralItems.DownloadItem;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import android.net.Uri;

public class MediaGeneralItemCache extends GenericCache {

	private static HashMap<Long, MediaGeneralItemCache> instance = new HashMap<Long, MediaGeneralItemCache>();
	private int amountOfItemsToDownload = 0;
	private HashMap<String, Integer> replicationStatusMap = new HashMap<String, Integer>();
	private HashMap<String, Long> totalBytesMap = new HashMap<String, Long>();
	private HashMap<String, Long> bytesDownloaded = new HashMap<String, Long>();
	private HashMap<Long, HashMap<String, Uri>> uriMaps = new HashMap<Long, HashMap<String, Uri>>();

	private MediaGeneralItemCache() {

	}

	public static MediaGeneralItemCache getInstance(long gameId) {
		if (!instance.containsKey(gameId)) {
			synchronized (instance) {
				instance.put(gameId, new MediaGeneralItemCache());
			}
		}
		return instance.get(gameId);
	}

	public void setAmountOfItemsToDownload(int amount) {
		amountOfItemsToDownload = amount;
	}

	public void putReplicationstatus(int replicationStatus, DownloadItem di) {
		synchronized (replicationStatusMap) {
			replicationStatusMap.put(getkey(di), replicationStatus);
		}
		if (replicationStatus == MediaCacheGeneralItems.REP_STATUS_DONE && di.getLocalPath() != null) {
			addDoneDownload(di);
		}
	}

	public void addDoneDownload(DownloadItem downloadItem) {
		synchronized (uriMaps) {
			if (!uriMaps.containsKey(downloadItem.getItemId())) {
				uriMaps.put(downloadItem.getItemId(), new HashMap<String, Uri>());
			}
			uriMaps.get(downloadItem.getItemId()).put(downloadItem.getLocalId(), downloadItem.getLocalPath());
		}
	}

	public HashMap<String, Uri> getLocalMediaUriMap(GeneralItem gi) {
		HashMap<String, Uri> result = getLocalMediaUriMap(gi.getId());
		if (result == null) return new HashMap<String, Uri>();
		return result;
	}

	public HashMap<String, Uri> getLocalMediaUriMap(long itemId) {
		if (!uriMaps.containsKey(itemId))
			return null;
		return uriMaps.get(itemId);
	}

	public int getAmountOfItemsToDownload() {
		return amountOfItemsToDownload;
	}

	public void registerTotalAmountofBytes(DownloadItem di, long contentLength) {
		synchronized (totalBytesMap) {
			totalBytesMap.put(getkey(di), contentLength);
		}
	}

	public void setBytesDownloaded(DownloadItem di, long byteCounter) {
		synchronized (bytesDownloaded) {
			bytesDownloaded.put(getkey(di), byteCounter);
		}
	}

	public double getPercentageDownloaded(DownloadItem di) {
		Long amount = totalBytesMap.get(getkey(di));
		Long bytesUpload = bytesDownloaded.get(getkey(di));
		if (amount == null || bytesUpload == null || amount == 0)
			return 0;
		return ((double) bytesUpload) / ((double) amount);
	}

	public double getPercentageDownloaded(GeneralItem gi) {
		DownloadItem[] allItems = GeneralItemsDelegator.getInstance().getDownloadItems(gi);
		if (allItems == null) return 1;
		double result = -1;
		for (DownloadItem di : allItems) {
			double percentage = getPercentageDownloaded(di);
			if (percentage > 0 && percentage < 1)
				return percentage;
			if (result == -1) {
				result = percentage;
			} else {
				result *= percentage;
			}
		}
		return result;
	}

	private String getkey(DownloadItem di) {
		return di.getItemId() + ":" + di.getLocalId();
	}

}
