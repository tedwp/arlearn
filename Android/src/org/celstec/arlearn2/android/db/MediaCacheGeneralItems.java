/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.android.db;

import java.util.HashMap;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.cache.MediaCache;
import org.celstec.arlearn2.android.cache.MediaGeneralItemCache;
import org.celstec.arlearn2.android.db.MediaCacheGeneralItems.DownloadItem;
import org.celstec.arlearn2.android.db.MediaCacheUpload.UploadItem;
import org.celstec.arlearn2.android.db.beans.MediaCacheItem;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceImageAnswerItem;
import org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest;
import org.celstec.arlearn2.beans.generalItem.VideoObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Message;
import android.util.Log;

public class MediaCacheGeneralItems extends GenericDbTable {
	
	public static final String MEDIACACHE_TABLE = "mediaCacheGeneralItems";

	public static final String ITEM_ID = "itemId";
	public static final String LOCAL_ID = "localId";
	public static final String GAME_ID = "gameId";

	public static final String REMOTE_FILE = "remoteFile";
	public static final String URI = "localUri";
	public static final String REPLICATED = "replicated";
	public static final String MIMETYPE = "mimetype";
	public static final String BYTESTOTAL = "bytesTotal";
	public static final String BYTESAVAILABLE = "bytesAvailable";

	public static final int REP_STATUS_TODO = 0;
	public static final int REP_STATUS_SYNCING = 1;
	public static final int REP_STATUS_DONE = 2;
	public static final int REP_STATUS_FILE_NOT_FOUND = 3;
	
	public MediaCacheGeneralItems(DBAdapter db) {
		super(db);
	}

	@Override
	public String createStatement() {
		return "create table " + MEDIACACHE_TABLE + " (" +
				ITEM_ID + " long, " + //0
				LOCAL_ID + " text, " +
				GAME_ID + " long, " + 
				REMOTE_FILE + " text, " + //3 
				URI + " text, " +
				REPLICATED + " integer, " + //5
				MIMETYPE + " text); " ;
				
	}

	@Override
	protected String getTableName() {
		return MEDIACACHE_TABLE;
	}
	

	
	private DownloadItem[] query(String selection, String[] selectionArgs, int amount) {
		DownloadItem[] resultGenIt = null;
		Cursor mCursor = null;
		try {
			mCursor = db.getSQLiteDb().query(true, getTableName(), null, selection, selectionArgs, null, null, null, null);
			if (amount > mCursor.getCount())
				amount = mCursor.getCount();
			if (amount == 0)
				amount = mCursor.getCount();

			resultGenIt = new DownloadItem[amount];
			int i = 0;

			while (mCursor.moveToNext() && i < amount) {
				
				resultGenIt[i++] = cursorToUploadItem(mCursor);
			}

		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		} finally {
			if (mCursor != null)
				mCursor.close();
		}
		return resultGenIt;
	}
	
	private DownloadItem cursorToUploadItem(Cursor mCursor ) {
		DownloadItem di = new DownloadItem();
		di.setItemId(mCursor.getLong(0));
		di.setLocalId(mCursor.getString(1));
		di.setGameId(mCursor.getLong(2));
		di.setRemoteUrl(mCursor.getString(3));
		String uri = mCursor.getString(4);
		if (uri != null && !"".equals(uri))
			di.setLocalPath(Uri.parse(uri));
		di.setReplicated(mCursor.getInt(5));
		di.setMimetype(mCursor.getString(6));
		
		return di;
	}
	
	
	public void addToCache(long gameId) {
		for (DownloadItem di: query(GAME_ID + " = ?", new String[] { ""+gameId}, 0)) {
			switch (di.getReplicated()) {
			case REP_STATUS_DONE:
				MediaGeneralItemCache.getInstance(gameId).addDoneDownload(di);
				break;
			default:
				break;
			}
		}
	}
	
	public void create(DownloadItem item) {
		db.getSQLiteDb().insert(getTableName(), null, getContentValues(item));
	}

	public void update(DownloadItem item) {
		db.getSQLiteDb().update(getTableName(), getContentValues(item),ITEM_ID + " = ? and "+LOCAL_ID +" = ?", new String[] { "" + item.getItemId(), item.getLocalId() });
	}
	
	private ContentValues getContentValues(DownloadItem item) {
		ContentValues values = new ContentValues();
		values.put(ITEM_ID, item.getItemId());
		values.put(LOCAL_ID, item.getLocalId());
		values.put(GAME_ID, item.getGameId());
		values.put(REMOTE_FILE, item.getRemoteUrl());
		if (item.getMimetype() != null) item.setMimetype(item.getMimetype());
		values.put(REPLICATED, item.getReplicated());
		return values;
	}
	
	public DownloadItem getNextUnsyncedItem(Long gameId) {
		DownloadItem []  di = query(REPLICATED + " = ? and "+GAME_ID + " = ?", new String[] { "" + REP_STATUS_TODO, ""+gameId }, 0);
		MediaGeneralItemCache.getInstance(gameId).setAmountOfItemsToDownload(di.length);
		if (di.length == 0) return null;
		return di[0];
	}
	
	public DownloadItem get(Long itemId, String localId) {
		DownloadItem []  di = query(ITEM_ID + " = ? and "+LOCAL_ID +" = ?", new String[] { "" + itemId, localId }, 0);
		if (di.length == 0) return null;
		return di[0];
	}
	
	public DownloadItem[] getDownloadItemsForGame(Long gameId) {
		return query(GAME_ID + " = ?", new String[] { ""+gameId }, 0);
	}

	public void setReplicationStatus(Long gameId, DownloadItem di, int status) {
		MediaGeneralItemCache.getInstance(gameId).putReplicationstatus(status, di);
		setReplicationStatus(di.getGameId(), di.getItemId(), di.getLocalId(), status, di.getLocalPath());
	}

	private void setReplicationStatus(final long gameId, final Long itemId, final String localId, final int replicationStatus, Uri localUri) {
		ContentValues newValue = new ContentValues();
		newValue.put(REPLICATED, replicationStatus);
		if (localUri != null) {
			newValue.put(URI, localUri.toString());
//			MediaCache.getInstance().putLocalURI(itemId, localId, localUri);
		}
		db.getSQLiteDb().update(getTableName(), newValue, ITEM_ID + "= ? and "+LOCAL_ID + " = ?", new String[] { "" + itemId, localId });
		
		
	}
	
	public static class DownloadItem {
		
		private long itemId;
		private String localId;
		private long gameId;
		private String remoteUrl;
		private Uri localPath;
		private int replicated;
		private  String mimetype;
		public DownloadItem() {
			
		}
		public long getItemId() {
			return itemId;
		}
		public void setItemId(long itemId) {
			this.itemId = itemId;
		}
		public String getLocalId() {
			return localId;
		}
		public void setLocalId(String localId) {
			this.localId = localId;
		}
		public long getGameId() {
			return gameId;
		}
		public void setGameId(long gameId) {
			this.gameId = gameId;
		}
		public String getRemoteUrl() {
			return remoteUrl;
		}
		public void setRemoteUrl(String remoteUrl) {
			this.remoteUrl = remoteUrl;
		}
		public Uri getLocalPath() {
			return localPath;
		}
		public void setLocalPath(Uri localPath) {
			this.localPath = localPath;
		}
		public int getReplicated() {
			return replicated;
		}
		public void setReplicated(int replicated) {
			this.replicated = replicated;
		}
		public String getMimetype() {
			return mimetype;
		}
		public void setMimetype(String mimetype) {
			this.mimetype = mimetype;
		}
	}

	
}
