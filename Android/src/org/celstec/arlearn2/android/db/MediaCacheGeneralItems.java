package org.celstec.arlearn2.android.db;

import java.util.HashMap;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.cache.MediaCache;
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
	
	public MediaCacheGeneralItems(DBAdapter db) {
		super(db);
	}

	@Override
	public String createStatement() {
		return "create table " + MEDIACACHE_TABLE + " (" +
				ITEM_ID + " long primary key, " +
				GAME_ID + " long, " + 
				REMOTE_FILE + " text, " + 
				URI + " text, " +
				REPLICATED + " integer, " +
				MIMETYPE + " text, " + 
				BYTESTOTAL + " long, " + 
				BYTESAVAILABLE + " long);";
	}

	@Override
	protected String getTableName() {
		return MEDIACACHE_TABLE;
	}
	
	
	public void listItemsToCache() {
		Cursor mCursor = null;
		try {
			HashMap<Long, String> allMediaURLs =  new HashMap<Long, String>();
			for (Long gameId : GeneralItemsCache.getInstance().getCachedGameIds() ) {
				for (GeneralItem gi : GeneralItemsCache.getInstance().getGeneralItemsWithGameId(gameId).values()) {
					if (MediaCache.getInstance().getLocalUri(gi.getId()) == null) {
						if (gi instanceof AudioObject) {
							allMediaURLs.put(gi.getId(), ((AudioObject) gi).getAudioFeed());
						}
						if (gi instanceof VideoObject) {
							allMediaURLs.put(gi.getId(), ((VideoObject) gi).getVideoFeed());
						}
						if (gi instanceof SingleChoiceImageTest) {
							SingleChoiceImageTest test = (SingleChoiceImageTest) gi;
							int i = 0;
							for (MultipleChoiceAnswerItem imageAnswer: test.getAnswers()){
								allMediaURLs.put(gi.getId(), ((MultipleChoiceImageAnswerItem) imageAnswer).getImageUrl());
							}
						}
					}
				}
			}
			
			
			mCursor = db.getSQLiteDb().query(true, getTableName(), new String[]{ITEM_ID, GAME_ID, REPLICATED, URI}, null, null, null, null, null, null);
			while (mCursor.moveToNext()) {
				Long itemId = mCursor.getLong(0);
				Long gameId = mCursor.getLong(1);
				allMediaURLs.remove(itemId);
				if (mCursor.getInt(2) == REP_STATUS_TODO) MediaCache.getInstance().localToDownload(gameId, itemId);
				if (mCursor.getInt(2) == REP_STATUS_DONE) MediaCache.getInstance().putLocalURI(itemId, Uri.parse(mCursor.getString(3)));

			}
			
			for (Long id : allMediaURLs.keySet()) {
				ContentValues initialValues = new ContentValues();
				GeneralItem gi = GeneralItemsCache.getInstance().getGeneralItems(id);
				initialValues.put(ITEM_ID, id);
				initialValues.put(GAME_ID, gi.getGameId());
				initialValues.put(REMOTE_FILE, allMediaURLs.get(id));
				initialValues.put(REPLICATED, REP_STATUS_TODO);
				db.getSQLiteDb().insert(getTableName(), null, initialValues);
			}
			
		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		} finally {
			if (mCursor != null)  mCursor.close();
		}
	}
	
	public MediaCacheItem getNextUnsyncedItem() {
		Cursor mCursor = null;
		try {
			mCursor = db.getSQLiteDb().query(true, getTableName(), new String[]{ITEM_ID, REMOTE_FILE}, REPLICATED + " = ? ", new String[] { "" + REP_STATUS_TODO }, null, null, null, null);
			if (mCursor.getCount() == 0) return null;
			if (mCursor.moveToNext()) {
				MediaCacheItem mci = new MediaCacheItem();
				mci.setItemId(""+mCursor.getLong(0));
				mci.setRemoteFile(mCursor.getString(1));
				return mci;
			}
		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		} finally {
			if (mCursor != null)
				mCursor.close();
		}
		return null;

	}
	
	public void setReplicationStatus(final Long itemId, final int replicationStatus, Uri localUri) {
		MediaCache.getInstance().putReplicationstatus(""+itemId, replicationStatus);
		ContentValues newValue = new ContentValues();
		newValue.put(REPLICATED, replicationStatus);
		if (localUri != null) {
			newValue.put(URI, localUri.toString());
			MediaCache.getInstance().putLocalURI(itemId, localUri);
		}
		db.getSQLiteDb().update(getTableName(), newValue, ITEM_ID + "= ?", new String[] { "" + itemId });
		
		
	}

}
