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

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.android.asynctasks.network.PublishActionTask;
import org.celstec.arlearn2.android.cache.ActionCache;
import org.celstec.arlearn2.beans.run.Action;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Message;
import android.util.Log;

public class MyActions extends GenericDbTable {

	public static final String MYACTIONS_TABLE = "myActions";
	public static final String ID = "id";
	public static final String ACCOUNT = "account";
	public static final String ACTION = "action";
	public static final String GENERAL_ITEM_ID = "generalItemId";
	public static final String GENERAL_ITEM_TYPE = "generalItemType";
	public static final String RUNID = "runId";
	public static final String TIMESTAMP = "timestamp";
	public static final String REPLICATED = "replicated";

	public MyActions(DBAdapter db) {
		super(db);
	}

	@Override
	public String createStatement() {
		return "create table " + MYACTIONS_TABLE + " (" + 
				ID + " text primary key, " + 
				ACTION + " text not null, " + 
				ACCOUNT + " text not null, "
				+ GENERAL_ITEM_ID + " text, " + 
				GENERAL_ITEM_TYPE + " text, " + 
				RUNID + " long not null, " + 
				TIMESTAMP + " long not null," + 
				REPLICATED+ " boolean not null);";
	}

	@Override
	protected String getTableName() {
		return MYACTIONS_TABLE;
	}

	
	public boolean insert(Action action, boolean replicated) {
		ContentValues initialValues = new ContentValues();
		String id = action.getAction() + ":" + action.getUserEmail() + ":" + action.getGeneralItemId() + ":" + action.getGeneralItemType() + ":" + action.getRunId()+":"+action.getTime();
		if (queryById(id) == null) {
			initialValues.put(ID, id);
			initialValues.put(ACCOUNT, action.getUserEmail());
			initialValues.put(ACTION, action.getAction());
			if (action.getGeneralItemId()!= null) initialValues.put(GENERAL_ITEM_ID, action.getGeneralItemId());
			if (action.getGeneralItemType()!= null) initialValues.put(GENERAL_ITEM_TYPE, action.getGeneralItemType());
			initialValues.put(RUNID, action.getRunId());
			initialValues.put(TIMESTAMP, action.getTime());
			initialValues.put(REPLICATED, replicated);
			ActionCache.getInstance().cacheAction(action.getRunId(), action);

			//			((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).unblockVisibleItem(resp);
			return db.getSQLiteDb().insert(getTableName(), null, initialValues) != -1;
		}
		
		return false;
	}

	@Override
	public int delete(Object o) {
		return 0;
	}

	public int deleteRun(long runId) {
		return db.getSQLiteDb().delete(getTableName(), RUNID + " = " + runId, null);
	}

	public List<Action> query(String selection, String[] selectionArgs) {
		List<Action> actions = new ArrayList<Action>();
		try {
			
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null, selection, selectionArgs, null, null, null, null);
//			actions = new Action[mCursor.getCount()];
			int i = 0;
			while (mCursor.moveToNext()) {
				Action action = new Action();
				action.setAction(mCursor.getString(1));
				action.setUserEmail(mCursor.getString(2));
				action.setGeneralItemId(mCursor.getLong(3));
				action.setGeneralItemType(mCursor.getString(4));
				action.setRunId(mCursor.getLong(5));
				action.setTime(mCursor.getLong(6));
				actions.add( action);
			}
			mCursor.close();
		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
		return actions;
	}

	public List<Action> queryActionsNotReplicated() {
		return query(REPLICATED + " = 0", null);
	}
	
	public List<Action> query(long runId) {
		List<Action> actions = query(RUNID + "= ?  ", new String[] { ""+runId});
//		ActionCache.getInstance().setActions(runId, actions);
		return actions;
	}

	

	public Object queryById(Object id) {
		try {
			return query(ID + "= ?", new String[] { (String) id }).get(0);
		} catch (Exception e) {
			return null;
		}
	}
	

	public long[] queryReadItems(long runId) {
		long[] ids = null;
		try {
			String selection = ACTION + " = 'read' and "+RUNID +" = ?";
			String[] selectionArgs = new String[] { ""+runId };
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), new String[]{GENERAL_ITEM_ID}, selection, selectionArgs, null, null, null, null);
			ids = new long[mCursor.getCount()];
			int i = 0;
			while (mCursor.moveToNext()) {
				ids[i++] = mCursor.getLong(0);
			}
			mCursor.close();
		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
		return ids;
	}
	
	public void queryAll(DBAdapter db, long runId) {
		ActionCache.getInstance().setRunLoaded(runId);
		for (Action a: query(runId)) {
			ActionCache.getInstance().cacheAction(runId, a);
		}
	}
	

//	public void publishAction(Action action) {
//		PublishAction pa = new PublishAction();
//		pa.action = action;
//		Message m = Message.obtain(DBAdapter.getDatabaseThread(db.getContext()));
//		m.obj = pa;
//		m.sendToTarget();
//	}
//
//	public class PublishAction implements DBAdapter.DatabaseTask{
//		public Action action;
//		
//		@Override
//		public void execute(DBAdapter db) {
//			insert(action, false);
//			for (Action action: queryActionsNotReplicated()) {
//				(new PublishActionTask(db.getContext(), action)).addTaskToQueue(db.getContext());
//			}
//		}
//
//	}
	
	public void confirmReplicated(long timeStamp, String action) {
		ContentValues newValue = new ContentValues();
		newValue.put(REPLICATED, true);
		db.getSQLiteDb().update(getTableName(), newValue, TIMESTAMP + "= ? and "+ACTION + " = ?", new String[] { "" + timeStamp , action });
	}
	
	public void replicationFailed(long timeStamp, String action) {
		ContentValues newValue = new ContentValues();
		newValue.put(REPLICATED, false);
		db.getSQLiteDb().update(getTableName(), newValue, TIMESTAMP + "= ? and "+ACTION + " = ?", new String[] { "" + timeStamp , action });
	}

}
