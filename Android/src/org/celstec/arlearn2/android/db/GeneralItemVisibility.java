package org.celstec.arlearn2.android.db;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.db.RunAdapter.RunQuery;
import org.celstec.arlearn2.android.db.RunAdapter.RunResults;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Run;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Message;
import android.util.Log;

public class GeneralItemVisibility extends GenericDbTable {
	
	public static final int NOT_INITIALISED = -1;
	public static final int NOT_YET_VISIBLE = 0;
	public static final int VISIBLE = 1;
	public static final int NO_LONGER_VISIBLE = 2;
	
	public static final String GENERALITEM_VISIBILITY_TABLE = "generalItemsVisibility";
	
	public static final String GENERAL_ITEM_ID = "id";// 0
	public static final String RUNID = "runId";//10
	public static final String SATISFIED_AT = "satisfiedAt";// 7
	public static final String VISIBILITY_STATUS = "visibilityStatus";//11

	public GeneralItemVisibility(DBAdapter db) {
		super(db);
	}
	
	@Override
	public String createStatement() {
		 return "create table " + GENERALITEM_VISIBILITY_TABLE + " (" 
				+ GENERAL_ITEM_ID + " long, " //0 
				+ SATISFIED_AT + " long , "  
				+ RUNID + " long ,"
				+ VISIBILITY_STATUS+" int);";
	}

	@Override
	protected String getTableName() {
		return GENERALITEM_VISIBILITY_TABLE;
	}
	
	public void setVisibilityStatus(long itemId, Long runId, long satisfiedAt, int status) {
		String whereArgs = GENERAL_ITEM_ID + " = " + itemId+ " and "+RUNID + " = "+runId + " and "+SATISFIED_AT +" > "+satisfiedAt +" and "+VISIBILITY_STATUS + " = "+status;
		if (runId == null) {
			whereArgs = GENERAL_ITEM_ID + " = " + itemId;
		}
		long oldSatisfiedAt = query(itemId, runId, status);
		if (oldSatisfiedAt == -1 || oldSatisfiedAt > satisfiedAt) {
			db.getSQLiteDb().delete(getTableName(), whereArgs, null);
			ContentValues initialValues = new ContentValues();
			initialValues.put(GENERAL_ITEM_ID, itemId);
			initialValues.put(RUNID, runId);
			initialValues.put(SATISFIED_AT, satisfiedAt);
			initialValues.put(VISIBILITY_STATUS, status);
			db.getSQLiteDb().insert(getTableName(), null, initialValues);
			GeneralItemVisibilityCache.getInstance().put(runId, itemId, status, satisfiedAt);
		}
	}
	
	public void resetAllRunsVisibility (long itemId) {
		String whereArgs = GENERAL_ITEM_ID + " = " + itemId;
		db.getSQLiteDb().delete(getTableName(), whereArgs, null);
		//TODO update cache
	}
	
	public int deleteRun(long runId) {
		GeneralItemVisibilityCache.getInstance().remove(runId);
		return db.getSQLiteDb().delete(getTableName(), RUNID + " = " + runId, null);
	}
	
	public Long[] query(long runId, int status) {
		Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), new String[]{GENERAL_ITEM_ID, SATISFIED_AT}, VISIBILITY_STATUS + " = ? and "+RUNID + " = ?", new String[]{""+status, ""+runId}, null, null, null, null);
		Long[] result  = new Long[mCursor.getCount()];
		int i = 0;
		while (mCursor.moveToNext()) {
			Long itemId = mCursor.getLong(0);
			Long satisfiedAt = mCursor.getLong(1);
			result[i++] = itemId;
			GeneralItemVisibilityCache.getInstance().put(runId, itemId, status, satisfiedAt);
		}
		mCursor.close();
		return result;
	}
	
	public Long query(long itemId, long runId, int status) {
		Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), new String[] {SATISFIED_AT}, VISIBILITY_STATUS + " = ? and "+RUNID + " = ? and "+GENERAL_ITEM_ID + " = ?", new String[]{""+status, ""+runId, ""+itemId}, null, null, null, null);
		long result = -1;
		
		if (mCursor.moveToNext()) {
			result = mCursor.getLong(0);
		}
		mCursor.close();
		return result;
	}
	
	public void queryAll(DBAdapter db, long runId) {
		Run[] resultRuns = null;
		try {
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), new String[]{GENERAL_ITEM_ID, VISIBILITY_STATUS, SATISFIED_AT }, RUNID + " = ?", new String[] { ""+runId}, null, null, null, null);
			while (mCursor.moveToNext()) {
				GeneralItemVisibilityCache.getInstance().put(runId, mCursor.getLong(0), mCursor.getInt(1), mCursor.getLong(2) );
			}
			GeneralItemVisibilityCache.getInstance().setRunLoaded(runId);
			mCursor.close();
		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
	}
	

}
