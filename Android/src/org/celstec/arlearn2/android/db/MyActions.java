package org.celstec.arlearn2.android.db;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.game.DependsOn;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Action;

//import org.celstec.arlearn2.android.db.beans.Action;
//import org.celstec.arlearn2.android.db.beans.GeneralItem;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
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
		Action resp = (Action) o;
		ContentValues initialValues = new ContentValues();
		String id = resp.getAction() + ":" + resp.getUserEmail() + ":" + resp.getGeneralItemId() + ":" + resp.getGeneralItemType() + ":" + resp.getRunId();
		if (queryById(id) == null) {
			initialValues.put(ID, id);
			initialValues.put(ACCOUNT, resp.getUserEmail());
			initialValues.put(ACTION, resp.getAction());
			initialValues.put(GENERAL_ITEM_ID, resp.getGeneralItemId());
			initialValues.put(GENERAL_ITEM_TYPE, resp.getGeneralItemType());
			initialValues.put(RUNID, resp.getRunId());
			initialValues.put(TIMESTAMP, resp.getTime());
			initialValues.put(REPLICATED, false);
			
			//TODO check if this action unblocks a
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

	public List<Action> query() {
		return query(REPLICATED + " = 0", null);
	}
	
	public List<Action> query(long runId) {
		return query(RUNID + "= ?  ", new String[] { ""+runId});
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

//	public boolean checkVisibility(GeneralItem item) {
////		try {
//			if (item.getDependsOn() == null) return true;
//			DependsOn dependsOn = item.getDependsOn();
////			JSONObject dependsOn = new JSONObject(item.getDependsOn());
////			String action = null;
////			String scope = null;
////			String generalItemId = null;
////			if (dependsOn.has("action")) action = dependsOn.getString("action");
////			if (dependsOn.has("scope")) scope = dependsOn.getString("scope");
//			//TODO deal with scope
////			if (dependsOn.has("generalItemId")) generalItemId = dependsOn.getString("generalItemId");
//			int i = 0;
//			if (dependsOn.getAction() != null) i++;
////			if (scope != null) i++;
//			if (dependsOn.getGeneralItemId() != null) i++;
//			if (i == 0) return true;
//			String selection = "";
//			String selectionArgs[] = new String[i];
//			i = 0;
//			if (dependsOn.getAction() != null) {
//				selection = ACTION + " = ? ";
//				selectionArgs[i++] = dependsOn.getAction();
//			}
//			if (dependsOn.getGeneralItemId() != null){
//				if (!selection.equals("")) selection += " and ";
//				selection += GENERAL_ITEM_ID + " = ? ";
//				selectionArgs[i++] = dependsOn.getGeneralItemId();
//			}
//			Object [] objects = query(selection, selectionArgs);
//			return objects.length != 0;
////		} catch (JSONException e) {
////			e.printStackTrace();
////		}
////		return false;
//		
//	}
//	
//	public void unblockVisibleItems(long runId) {
//		Action [] allActions = query(RUNID + "= ?", new String[] { ""+ runId });
//		unblockVisibleItems(allActions, runId);
//	}
//	
//	private void unblockVisibleItems(Action [] allActions, long runId) {
//		GeneralItemAdapter giAdapter = (GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER);
//		GeneralItem gis[] = giAdapter.queryDependencyInvisibleItems(runId);
//		unblockVisibleItems(giAdapter, allActions, gis, runId);
//	}
//	
//	private void unblockVisibleItems(GeneralItemAdapter giAdapter, Action [] allActions, GeneralItem gis[], long runId) {
//		for (int i = 0; i < gis.length; i++) {
//			unblockVisibleItems(giAdapter, allActions, gis[i], runId);
//		}
//	}
//	
//	private void unblockVisibleItems(GeneralItemAdapter giAdapter, Action [] allActions, GeneralItem gi, long runId) {
//		if (gi.getDependsOn() == null) return;
//		Dependency  dep = new Dependency(gi.getDependsOn());
////		Dependency  dep = gi.getDependency();
//		if (dep.allDepenciesAreMet(allActions)) {
//			giAdapter.setDependsOnVisible(gi.getId());
//		}
//	}


}
