package org.celstec.arlearn2.android.db;

import java.util.HashMap;

import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.Run;
//import org.celstec.arlearn2.android.db.beans.GeneralItem;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.DependsOn;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;

import org.celstec.arlearn2.android.sync.GeneralItemsSyncroniser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.sax.StartElementListener;
import android.util.Log;

public class GeneralItemAdapter extends GenericDbTable {

	public static final int NOT_INITIALISED = 0;
	public static final int NOT_YET_VISIBLE = 1;
	public static final int VISIBLE = 2;
	public static final int NO_LONGER_VISIBLE = 3;

	public static final String GENERALITEM_TABLE = "generalItems";
	public static final String ID = "id";// 0
	public static final String NAME = "name";// 1
	public static final String DESCRIPTION = "description";
	public static final String DEPENDSON = "dependsOn";// 3
	public static final String TYPE = "type";
	public static final String PAYLOAD = "payload";// 5
	public static final String RADIUS = "radius";
	public static final String SHOW_AT = "showAtTimeStamp";// 7
	public static final String LNG = "lng";
	public static final String LAT = "lat";// 9
	public static final String RUNID = "runId";//10
	public static final String VISIBILITY_STATUS = "visibilityStatus";//10
//	public static final String DEPENDENCY_VISIBLE = "dependson_visible";//11
//	public static final String TIME_VISIBLE = "time_visible";//12
	public static final String FIRST_READ = "firstRead";//13

	public GeneralItemAdapter(DBAdapter db) {
		super(db);
	}

	public String createStatement() {
		return "create table " + GENERALITEM_TABLE + " (" 
				+ ID + " text, " 
				+ NAME + " text, " 
				+ DESCRIPTION 
				+ " text, " 
				+ DEPENDSON + " text, "
				+ TYPE + " text, " 
				+ PAYLOAD + " text, " 
				+ RADIUS + " integer , " 
				+ SHOW_AT + " long , " 
				+ LNG + " double , " 
				+ LAT + " double ,"
				+ RUNID + " long ," 
//				+ DEPENDENCY_VISIBLE+" boolean, "
				+ VISIBILITY_STATUS+" int, "
				+ FIRST_READ + " long );";
	}

	protected String getTableName() {
		return GENERALITEM_TABLE;
	}

	public boolean insert(Object o) {
		boolean visible = false;
		GeneralItem gi = (GeneralItem) o;
		JsonBeanSerialiser jbs = new JsonBeanSerialiser(gi);
//		String json = gi.getSpecificPartAsJsonString();
//		if (json != null)
//			gi.setPayload(json);
//			gi.setPayload(jbs.serialiseToJson().toString());
		GeneralItem oldItem = queryById(gi.getId());
		if (oldItem == null || !oldItem.equals(gi)) {
			String giJson = jbs.serialiseToJson().toString();
			gic.remove(gi.getId());
			queryCache = null;
			delete(gi.getId());
			ContentValues initialValues = new ContentValues();
			initialValues.put(ID, gi.getId());
			initialValues.put(NAME, gi.getName());
			initialValues.put(DESCRIPTION, gi.getDescription());
			//TODO check if this is still necessary -> payload
//			initialValues.put(DEPENDSON, gi.getDependsOn());
			initialValues.put(TYPE, gi.getType());
			initialValues.put(RADIUS, gi.getRadius());
			initialValues.put(SHOW_AT, gi.getShowAtTimeStamp());
			initialValues.put(LNG, gi.getLng()==null?0:gi.getLng());
			initialValues.put(LAT, gi.getLat()==null?0:gi.getLat());
			initialValues.put(PAYLOAD, giJson );
			initialValues.put(RUNID, gi.getRunId());
			//TODO other implementation for dependencies
//			initialValues.put(DEPENDENCY_VISIBLE, visible);
			//TODO other implementation for dependencies
			initialValues.put(VISIBILITY_STATUS, NOT_INITIALISED);
			initialValues.put(FIRST_READ, System.currentTimeMillis());
			return db.getSQLiteDb().insert(getTableName(), null, initialValues) != -1;
		} 
		return false;
	}

	
	public int delete(Object o) {
		Long id = (Long) o;
		return db.getSQLiteDb().delete(getTableName(), ID + " = " + id, null);
	}
	
	public int deleteRun(long runId) {
		return db.getSQLiteDb().delete(getTableName(), RUNID + " = " + runId, null);
	}

	public GeneralItem queryById(Object id) {
		try {
			return query(ID + "= "+id, null)[0];
		} catch (Exception e) {
			return null;
		}
	}

	private static GeneralItem[] queryCache = null;
	public GeneralItem[] query() {
		if (queryCache != null) return queryCache;
		queryCache = query(null, null);
		return queryCache;
	}
	
	public GeneralItem[] query(long runId) {
		return query(RUNID + "= ?  ", new String[] { ""+runId});
		//TODO dependencies
//		return query(RUNID + "= ? and "+TIME_VISIBLE + " = ? and "+DEPENDENCY_VISIBLE + " = ? ", new String[] { ""+runId, ""+VISIBLE,"1"});
	}
	
	public GeneralItem[] queryMessages(long runId) {
		try {
			return query(LAT + "= ? and "+LNG+"= ? and "+RUNID+" = ? and "+VISIBILITY_STATUS + " = ?", new String[] { "0", "0", ""+runId, ""+VISIBLE});
		} catch (Exception e) {
			return null;
		}
	}
	
	public GeneralItem[] query(long runId, int visibility) {
		return query(RUNID + "= ? and "+VISIBILITY_STATUS + " = ? ", new String[] { ""+runId, ""+visibility});
	}
	
//	public GeneralItem[] queryDependencyInvisibleItems(long runId) {
//		return query(RUNID + "= ? and "+DEPENDENCY_VISIBLE + " = ? ", new String[] { ""+runId, "0"});
//	}
	
//	public GeneralItem[] queryNotTimeInitialised(long runId) {
//		return query(RUNID + "= ? and "+TIME_VISIBLE + " = ? and "+DEPENDENCY_VISIBLE + " = ? ", new String[] { ""+runId, ""+NOT_INITIALISED, "1"});
//	}
	
	public GeneralItem[] queryWithLocation(long runId) {
		return query(RUNID + "= ? and "+LAT + " != ? and "+LNG+" != ? and "+VISIBILITY_STATUS + " = ? ", new String[] { ""+runId, "0", "0", ""+VISIBLE});
	}
	
	public static HashMap<Long, GeneralItem> gic = new HashMap<Long, GeneralItem>();
	
	private GeneralItem[] query(String selection, String[] selectionArgs) {
		GeneralItem[] resultGenIt = null;
		try {
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null, selection, selectionArgs, null, null, FIRST_READ+" DESC", null);
			resultGenIt = new GeneralItem[mCursor.getCount()];
			int i = 0;
			while (mCursor.moveToNext()) {
				
				String jsonPayload = mCursor.getString(5);
				try {
					Long itemId = mCursor.getLong(0);
					if (gic.containsKey(itemId)) {
						resultGenIt[i++] = gic.get(itemId);
					} else {
						JsonBeanDeserializer jbd = new JsonBeanDeserializer(jsonPayload);
						GeneralItem gi = (GeneralItem) jbd.deserialize(GeneralItem.class);
						gic.put(itemId, gi);
						resultGenIt[i++] = gi;
					}
					
//					gi.setId(mCursor.getLong(0));
//					gi.setName(mCursor.getString(1));
//					gi.setDescription(mCursor.getString(2));
////					gi.setDependsOn(mCursor.getString(3));
//					gi.setType(mCursor.getString(4));
////					gi.setPayload(mCursor.getString(5));
//					gi.setRadius(mCursor.getInt(6));
//					gi.setShowAtTimeStamp(mCursor.getLong(7));
//					gi.setLng(mCursor.getDouble(8));
//					gi.setLat(mCursor.getDouble(9));
//					gi.setRunId(mCursor.getLong(10));
					
//					gi.setDependsOnVisible(mCursor.getInt(11)==1);
//					gi.setTimeVisible(mCursor.getInt(12)==1);
//					gi.setAutoId(mCursor.getInt(13));
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				

			}
			mCursor.close();
		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
		return resultGenIt;
	}

	public GeneralItem getDatabaseObject(Object id) {
		try {
			return query(ID + "= ?", new String[] { (String) id })[0];
		} catch (Exception e) {
			return null;
		}
	}

	@Deprecated
	public void unblockVisibleItem_old(Action actionObject) {
//		GeneralItem[] items = query(RUNID + "= ?  and "+VISIBILITY_STATUS + " = ? ", new String[] { ""+actionObject.getRunId(), "0"});
//		for (int i = 0; i < items.length; i++) {
//				boolean unblock = true;
//				if (!(items[i].getDependsOn() == null)) {
//					
//				
//				DependsOn dependsOn = items[i].getDependsOn();
//				String scope = null;
//				String generalItemId = null;
//				
//				if (dependsOn.getAction() != null) {
//					if (!dependsOn.getAction().equals(actionObject.getAction())) unblock = false;
//				}
//				if (dependsOn.getGeneralItemId() != null) {
//
//					if (!dependsOn.getGeneralItemId().equals(actionObject.getGeneralItemId())) unblock = false;
//				}
//				}
//				if (unblock) {
////					setDependsOnVisible(items[i]);
////					setTimeVisible(items[i].getId(), NOT_INITIALISED);
////					GeneralItemsSyncroniser.dispatchNotification(db.getContext(), items[i], null);
//				}
////				GeneralItemsSyncroniser.timeInitGeneralItems(db, db.getContext(), (Run) db.table(DBAdapter.RUN_ADAPTER).queryById(actionObject.getRunId()));
//		}
	}
	
	public void setFirstRead(Long itemId, long date) {
			ContentValues newValue = new ContentValues();
			newValue.put(FIRST_READ, date);
			queryCache = null;
			db.getSQLiteDb().update(getTableName(), newValue, ID + "=? and "+FIRST_READ +" = 0", new String[] { ""+itemId });

	}

	@Deprecated
	private void setDependsOnVisible_old(GeneralItem generalItem) {
		ContentValues newValue = new ContentValues();
//		newValue.put(DEPENDENCY_VISIBLE, true);
		db.getSQLiteDb().update(getTableName(), newValue, ID + "=?", new String[] { "" + generalItem.getId() });
		
	}
	
	@Deprecated
	public void setDependsOnVisible_old(Long id) {
		ContentValues newValue = new ContentValues();
//		newValue.put(DEPENDENCY_VISIBLE, true);
		db.getSQLiteDb().update(getTableName(), newValue, ID + "=?", new String[] { "" + id });
	}
	


	public void resetVisiblity(Long runId) {
		GeneralItem[] items = query(RUNID + "= ? ", new String[] { ""+runId, "1"});
		for (int i = 0; i < items.length; i++) {
			queryCache = null;
			ContentValues newValue = new ContentValues();
//			newValue.put(DEPENDENCY_VISIBLE, false);
			newValue.put(VISIBILITY_STATUS, NOT_INITIALISED);
			newValue.put(FIRST_READ, 0);
			db.getSQLiteDb().update(getTableName(), newValue, RUNID + "= ?  and "+ID + "=?", new String[] { ""+ runId, "" + items[i].getId() });
//			ma.checkVisibility(items[i]);
		}
	}

	@Deprecated
	public void setTimeVisible_old(Long id, int visible) {
		ContentValues newValue = new ContentValues();
//		newValue.put(TIME_VISIBLE, visible);
		db.getSQLiteDb().update(getTableName(), newValue, ID + "=?", new String[] { ""+id });	
	}
	
	public void setVisiblityStatus(Long runId, Long generalItemId, int visiblityStatus, long timeAt) {
		queryCache = null;
		ContentValues newValue = new ContentValues();
		newValue.put(VISIBILITY_STATUS, visiblityStatus);
		newValue.put(FIRST_READ, timeAt);
		db.getSQLiteDb().update(getTableName(), newValue, RUNID + "= ?  and "+ID + "=?", new String[] { ""+runId, ""+generalItemId });	

	}
}
