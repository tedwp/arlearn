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
	
	public static final int NOT_INITIALISED = 0;
	public static final int NOT_YET_VISIBLE = 1;
	public static final int VISIBLE = 2;
	public static final int NO_LONGER_VISIBLE = 3;
	
	public static final String GENERALITEM_VISIBILITY_TABLE = "generalItemsVisibility";
	
	public static final String GENERAL_ITEM_ID = "id";// 0
	public static final String RUNID = "runId";//10
	public static final String SHOW_AT = "showAtTimeStamp";// 7
	public static final String VISIBILITY_STATUS = "visibilityStatus";//11

	public GeneralItemVisibility(DBAdapter db) {
		super(db);
	}
	
	@Override
	public String createStatement() {
		 return "create table " + GENERALITEM_VISIBILITY_TABLE + " (" 
				+ GENERAL_ITEM_ID + " long, " //0 
				+ SHOW_AT + " long , "  
				+ RUNID + " long ,"
				+ VISIBILITY_STATUS+" int);";
	}

	@Override
	protected String getTableName() {
		return GENERALITEM_VISIBILITY_TABLE;
	}
	
	public void setVisibilityStatus(long itemId, Long runId, long satisfiedAt, int status) {
		String whereArgs = GENERAL_ITEM_ID + " = " + itemId+ " and "+RUNID + " = "+runId;
		if (runId == null) {
			whereArgs = GENERAL_ITEM_ID + " = " + itemId;
		}
		db.getSQLiteDb().delete(getTableName(), whereArgs, null);
		ContentValues initialValues = new ContentValues();
		initialValues.put(GENERAL_ITEM_ID, itemId);
		initialValues.put(RUNID, runId);
		initialValues.put(SHOW_AT, satisfiedAt);
		initialValues.put(VISIBILITY_STATUS, status);
		db.getSQLiteDb().insert(getTableName(), null, initialValues);
		if (satisfiedAt < System.currentTimeMillis()) GeneralItemVisibilityCache.getInstance().put(runId, itemId, status);
	}
	
	public int deleteRun(long runId) {
		GeneralItemVisibilityCache.getInstance().remove(runId);
		return db.getSQLiteDb().delete(getTableName(), RUNID + " = " + runId, null);
	}
	
	public Long[] query(long runId, int status) {
		Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null, VISIBILITY_STATUS + " = ? and "+RUNID + " = ?", new String[]{""+status, ""+runId}, null, null, null, null);
		Long[] result  = new Long[mCursor.getCount()];
		int i = 0;
		while (mCursor.moveToNext()) {
			Long itemId = mCursor.getLong(0);
			result[i++] = itemId;
			GeneralItemVisibilityCache.getInstance().put(runId, itemId, status);
		}
//		results.onResults(result);
		mCursor.close();
		return result;
//		VisibilityQuery query = new VisibilityQuery();
//		query.status = status;
//		query.results = results;
//		query.runId = runId;
//		Message m = Message.obtain(DBAdapter.getDatabaseThread(db.getContext()));
//		m.obj = query;
//		m.sendToTarget();
	}
	
//	public interface VisibilityResults {
//		
//		public void onResults(Long itemIds[]);
//	}
//	
//	public class VisibilityQuery implements DBAdapter.DatabaseTask{
//		
//		public int status;
//		public long runId;
//		public VisibilityResults results;
//		
//		@Override
//		public void execute(DBAdapter db) {
//			
//		}
//		
//	}
	
//	public class VisibilityTask implements DBAdapter.DatabaseTask{
//		
//		public Long itemId;
//		public Long runId;
//		public Long satisfiedAt;
//		public int status;
//		
//		@Override
//		public void execute(DBAdapter db) {
//			
//		}
//		
//	}

	public void queryAll(DBAdapter db, long runId) {
		Run[] resultRuns = null;
		try {
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null, RUNID + " = ?", new String[] { ""+runId}, null, null, null, null);
			while (mCursor.moveToNext()) {
//				results.onResult(mCursor.getLong(2), mCursor.getLong(0), mCursor.getInt(3), mCursor.getLong(1));
				GeneralItemVisibilityCache.getInstance().put(mCursor.getLong(2), mCursor.getLong(0), mCursor.getInt(3));
			}
			GeneralItemVisibilityCache.getInstance().setRunLoaded(runId);
			mCursor.close();
		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
	}
	

}
