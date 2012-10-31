package org.celstec.arlearn2.android.db;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicSliderUI.ActionScroller;

import org.celstec.arlearn2.android.cache.ActionCache;
import org.celstec.arlearn2.android.db.GeneralItemAdapter.GeneralItemResults;
import org.celstec.arlearn2.android.service.GeneralItemDependencyHandler;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.client.ActionClient;

//import org.celstec.arlearn2.android.db.beans.Action;
//import org.celstec.arlearn2.android.db.beans.GeneralItem;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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
				TIMESTAMP + " long not null," + REPLICATED
				+ " boolean not null);";
	}

	@Override
	protected String getTableName() {
		return MYACTIONS_TABLE;
	}

	@Override
	public boolean insert(Object o) {
		Action action = (Action) o;
		ContentValues initialValues = new ContentValues();
		String id = action.getAction() + ":" + action.getUserEmail() + ":" + action.getGeneralItemId() + ":" + action.getGeneralItemType() + ":" + action.getRunId();
		if (queryById(id) == null) {
			initialValues.put(ID, id);
			initialValues.put(ACCOUNT, action.getUserEmail());
			initialValues.put(ACTION, action.getAction());
			if (action.getGeneralItemId()!= null) initialValues.put(GENERAL_ITEM_ID, action.getGeneralItemId());
			if (action.getGeneralItemType()!= null) initialValues.put(GENERAL_ITEM_TYPE, action.getGeneralItemType());
			initialValues.put(RUNID, action.getRunId());
			initialValues.put(TIMESTAMP, action.getTime());
			initialValues.put(REPLICATED, false);
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
		ActionCache.getInstance().setActions(runId, actions);
		return actions;
	}

	public void confirmReplicated(Action action) {
		ContentValues newValue = new ContentValues();
		newValue.put(REPLICATED, true);
		db.getSQLiteDb().update(getTableName(), newValue, TIMESTAMP + "=?", new String[] { "" + action.getTime() });

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
	

	public void publishAction(Action action) {
		PublishAction pa = new PublishAction();
		pa.action = action;
		Message m = Message.obtain(DBAdapter.getDatabaseThread(db.getContext()));
		m.obj = pa;
		m.sendToTarget();
	}

	public class PublishAction implements DBAdapter.DatabaseTask{
		public Action action;
		
		@Override
		public void execute(DBAdapter db) {
			insert(action);
			for (Action action: queryActionsNotReplicated()) {
				ActionClient ac = ActionClient.getActionClient();
				Action result = ac.publishAction(PropertiesAdapter.getInstance(db.getContext()).getFusionAuthToken(), action);
				if (result.getError()== null) {
					confirmReplicated(result);	
				} else {
					if (result.getError() !=null && "User not found".equals(result.getError())) 				
						confirmReplicated(result);	 //this is not elegant... but it mean the user was deleted, so don't try to sync in future
				}
			}
			(new GeneralItemDependencyHandler(db.getContext())).checkDependencies(db);
		}

	}

}
