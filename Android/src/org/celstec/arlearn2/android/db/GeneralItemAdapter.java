package org.celstec.arlearn2.android.db;

import java.util.HashMap;

import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.Run;
//import org.celstec.arlearn2.android.db.beans.GeneralItem;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;

import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.sync.GeneralItemsSyncroniser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Message;
import android.sax.StartElementListener;
import android.util.Log;

public class GeneralItemAdapter extends GenericDbTable {



	public static final String GENERALITEM_TABLE = "generalItems";
	public static final String ID = "id";// 0
	public static final String GAMEID = "gameId";//10
	public static final String PAYLOAD = "payload";// 5
	public static final String DELETED = "deleted";
	public static final String LAST_MODIFICATION_DATE = "lastModificationDate";//15
	public static final String SORTKEY = "sortKey";//16
	
	public GeneralItemAdapter(DBAdapter db) {
		super(db);
	}

	public String createStatement() {
		return "create table " + GENERALITEM_TABLE + " (" 
				+ ID + " text, " //0
				+ GAMEID + " long, "
				+ PAYLOAD + " text, " 
				+ DELETED + " boolean, "
				+ LAST_MODIFICATION_DATE + " long, "
				+ SORTKEY + " long );";
	}

	protected String getTableName() {
		return GENERALITEM_TABLE;
	}

	public boolean insert(Object o) {
		GeneralItem gi = (GeneralItem) o;
		JsonBeanSerialiser jbs = new JsonBeanSerialiser(gi);
		GeneralItem oldItem = queryById(gi.getId());
		GeneralItemsCache.getInstance().put(gi);
		if (oldItem == null || !oldItem.equals(gi)) {
			String giJson = jbs.serialiseToJson().toString();
			delete(gi.getId(), gi.getGameId());
			ContentValues initialValues = new ContentValues();
			initialValues.put(ID, gi.getId());
			initialValues.put(PAYLOAD, giJson );
			if (gi.getDeleted() == null) {
				initialValues.put(DELETED, false);
			} else {
				initialValues.put(DELETED, gi.getDeleted());
			}
			initialValues.put(LAST_MODIFICATION_DATE, gi.getLastModificationDate());
			if (gi.getSortKey() == null) gi.setSortKey(0);
			initialValues.put(SORTKEY, gi.getSortKey());
			return db.getSQLiteDb().insert(getTableName(), null, initialValues) != -1;
		} 
		return false;
	}

	
	public int delete(Object o, long gameId) {
		Long id = (Long) o;
		return db.getSQLiteDb().delete(getTableName(), ID + " = " + id+ " and "+GAMEID + " = "+gameId, null);
	}
	
	public int delete(long id) {
		return db.getSQLiteDb().delete(getTableName(), ID + " = " + id, null);
	}
	
	
	public GeneralItem queryById(Object id) {
		try {
			return query(ID + "= "+id , null)[0];
		} catch (Exception e) {
			return null;
		}
	}

//	private static GeneralItem[] queryCache = null;
//	public GeneralItem[] query() {
//		if (queryCache != null) return queryCache;
//		queryCache = query(null, null);
//		return queryCache;
//	}
	
//	public GeneralItem[] query(long runId) {
//		return query(RUNID + "= ?  and " +DELETED + " = ?", new String[] { ""+runId, "0"});
//	}

	public GeneralItem[] queryByGameId(long gameId) {
		return query(GAMEID + "= ?  and " +DELETED + " = ?", new String[] { ""+gameId, "0"});
	}

	
//	public GeneralItem[] queryMessages(long runId) {
//		try {
//			return query(LAT + "= ? and "+LNG+"= ? and "+RUNID+" = ? and "+VISIBILITY_STATUS + " = ? and " +DELETED + " = ?", new String[] { "0", "0", ""+runId, ""+VISIBLE, "0"});
//		} catch (Exception e) {
//			return null;
//		}
//	}
	
//	public GeneralItem[] query(long runId, int visibility) {
//		return query(RUNID + "= ? and "+VISIBILITY_STATUS + " = ? and " +DELETED + " = ?", new String[] { ""+runId, ""+visibility, "0"});
//	}
	
//	public GeneralItem[] queryDependencyInvisibleItems(long runId) {
//		return query(RUNID + "= ? and "+DEPENDENCY_VISIBLE + " = ? ", new String[] { ""+runId, "0"});
//	}
	
//	public GeneralItem[] queryNotTimeInitialised(long runId) {
//		return query(RUNID + "= ? and "+TIME_VISIBLE + " = ? and "+DEPENDENCY_VISIBLE + " = ? ", new String[] { ""+runId, ""+NOT_INITIALISED, "1"});
//	}
	
//	public GeneralItem[] queryWithLocation(long runId) {
//		return query(RUNID + "= ? and "+LAT + " != ? and "+LNG+" != ? and "+VISIBILITY_STATUS + " = ? and " +DELETED + " = ?", new String[] { ""+runId, "0", "0", ""+VISIBLE, "0"});
//	}
	
	private GeneralItem[] query(String selection, String[] selectionArgs) {
		GeneralItem[] resultGenIt = null;
		Cursor mCursor = null;
		try {
			mCursor = db.getSQLiteDb().query(true, getTableName(), null, selection, selectionArgs, null, null, SORTKEY+" DESC", null);
			resultGenIt = new GeneralItem[mCursor.getCount()];
			int i = 0;
			while (mCursor.moveToNext()) {
				
				String jsonPayload = mCursor.getString(2);
				try {
					Long itemId = mCursor.getLong(0);
					
						JsonBeanDeserializer jbd = new JsonBeanDeserializer(jsonPayload);
						GeneralItem gi = (GeneralItem) jbd.deserialize(GeneralItem.class);
						resultGenIt[i++] = gi;
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				

			}
			
		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		} finally {
			if (mCursor != null)  mCursor.close();
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

//	@Deprecated
//	public void unblockVisibleItem_old(Action actionObject) {
//	}
	
//	public void setFirstRead(Long itemId, long date) {
//			ContentValues newValue = new ContentValues();
//			newValue.put(FIRST_READ, date);
////			queryCache = null;
//			db.getSQLiteDb().update(getTableName(), newValue, ID + "=? and "+FIRST_READ +" = 0", new String[] { ""+itemId });
//
//	}

//	@Deprecated
//	private void setDependsOnVisible_old(GeneralItem generalItem) {
//		ContentValues newValue = new ContentValues();
////		newValue.put(DEPENDENCY_VISIBLE, true);
//		db.getSQLiteDb().update(getTableName(), newValue, ID + "=?", new String[] { "" + generalItem.getId() });
//		
//	}
	
//	@Deprecated
//	public void setDependsOnVisible_old(Long id) {
//		ContentValues newValue = new ContentValues();
////		newValue.put(DEPENDENCY_VISIBLE, true);
//		db.getSQLiteDb().update(getTableName(), newValue, ID + "=?", new String[] { "" + id });
//	}
	


//	public void resetVisiblity(Long runId) {
//		GeneralItem[] items = query(RUNID + "= ? ", new String[] { ""+runId, "1"});
//		for (int i = 0; i < items.length; i++) {
//			ContentValues newValue = new ContentValues();
//			newValue.put(VISIBILITY_STATUS, NOT_INITIALISED);
//			newValue.put(FIRST_READ, 0);
//			db.getSQLiteDb().update(getTableName(), newValue, RUNID + "= ?  and "+ID + "=?", new String[] { ""+ runId, "" + items[i].getId() });
//		}
//	}

//	@Deprecated
//	public void setTimeVisible_old(Long id, int visible) {
//		ContentValues newValue = new ContentValues();
////		newValue.put(TIME_VISIBLE, visible);
//		db.getSQLiteDb().update(getTableName(), newValue, ID + "=?", new String[] { ""+id });	
//	}
	
//	public void setVisiblityStatus(Long runId, Long generalItemId, int visiblityStatus, long timeAt) {
////		queryCache = null;
//		ContentValues newValue = new ContentValues();
//		newValue.put(VISIBILITY_STATUS, visiblityStatus);
//		newValue.put(FIRST_READ, timeAt);
//		db.getSQLiteDb().update(getTableName(), newValue, RUNID + "= ?  and "+ID + "=?", new String[] { ""+runId, ""+generalItemId });	
//	}
	
//	public int getVisiblityStatus(Long runId, Long generalItemId) {
//		try {
//			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null, RUNID + "= ?  and "+ID + "=?", new String[] { ""+runId, ""+generalItemId }, null, null, FIRST_READ+" DESC", null);
//			
//			
//			int i = 0;
//			if (mCursor.moveToNext()) {
//				int result = mCursor.getInt(11); 
//				mCursor.close();
//				return result; 
//
//			}
//			mCursor.close();
//		} catch (SQLException e) {
//			Log.e("sqlex", "ex", e);
//		}
//		return -1;
//	}
	
	public void queryAll(DBAdapter db) {
		try {
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null, null, null, null, null, null, null);
			int i = 0;
			while (mCursor.moveToNext()) { //TODO merge with other query method
				String jsonPayload = mCursor.getString(2);
				try {
						JsonBeanDeserializer jbd = new JsonBeanDeserializer(jsonPayload);
						GeneralItem gi = (GeneralItem) jbd.deserialize(GeneralItem.class);
						GeneralItemsCache.getInstance().put(gi);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				

			}
			mCursor.close();
		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
		
	}
	

	public interface GeneralItemResults {
		public void onResults(GeneralItem[] gi);
	}

	public class GeneralItemQuery implements DBAdapter.DatabaseTask{
		
		public GeneralItemResults results;
		
		@Override
		public void execute(DBAdapter db) {
			
		}
		
	}
	
}
