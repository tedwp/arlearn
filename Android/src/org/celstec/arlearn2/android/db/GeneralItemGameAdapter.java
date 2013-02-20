package org.celstec.arlearn2.android.db;

import java.util.HashMap;

import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class GeneralItemGameAdapter extends GenericDbTable {
	
	private String CLASSNAME = this.getClass().getName();

	public static final int NOT_INITIALISED = 0;
	public static final int NOT_YET_VISIBLE = 1;
	public static final int VISIBLE = 2;
	public static final int NO_LONGER_VISIBLE = 3;

	public static final String GENERALITEM_GAME_TABLE = "generalItemsGame";
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String GAMEID = "gameId";
	public static final String TYPE = "type";
	public static final String VISIBILITY_STATUS = "visibilityStatus";
	public static final String JSON = "payLoad";	
	public static final String FIRST_READ = "firstRead";	
	public static final String SORTKEY = "sortKey";

	public GeneralItemGameAdapter(DBAdapter db) {
		super(db);
	}

	public String createStatement() {
		return "create table " + GENERALITEM_GAME_TABLE + " (" 
				+ ID + " text, "
				+ NAME + " text, " 
				+ GAMEID + " text, " 
				+ TYPE + " text, "				
				+ VISIBILITY_STATUS+" int, "
				+ JSON + " text, "				
				+ FIRST_READ + " long, "
				+ SORTKEY + " long );";
	}

	protected String getTableName() {
		return GENERALITEM_GAME_TABLE;
	}

	public boolean insert(Object o) {
		GeneralItem gi = (GeneralItem) o;
		JsonBeanSerialiser jbs = new JsonBeanSerialiser(gi);
		
		GeneralItem oldItem = queryById(gi.getId(), gi.getGameId());
		if (oldItem == null || !oldItem.equals(gi)) {
			String giJson = jbs.serialiseToJson().toString();
			gic.remove(gi.getId());
			queryCache = null;
			delete(gi.getId(), gi.getGameId());
			ContentValues initialValues = new ContentValues();
			initialValues.put(ID, gi.getId());
			initialValues.put(NAME, gi.getName());		
			initialValues.put(GAMEID, gi.getGameId());
			initialValues.put(TYPE, gi.getType());
			initialValues.put(VISIBILITY_STATUS, NOT_INITIALISED);
			initialValues.put(JSON, giJson );
			initialValues.put(FIRST_READ, System.currentTimeMillis());
			
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
	
	public void emptyTable() {
		db.getSQLiteDb().execSQL(this.eraseAllStatement());
	}	
	
	public int deleteRun(long gameId) {
		return db.getSQLiteDb().delete(getTableName(), GAMEID + " = " + gameId, null);
	}

	public GeneralItem queryById(Object id, long gameId) {
		try {
			return query(ID + "= "+id + " and "+GAMEID + " = "+gameId, null)[0];
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
	
	public GeneralItem[] queryByGameId(long gameId) {
	
		Log.d(CLASSNAME, "Querying table "+getTableName()+" game "+gameId);
		GeneralItem[] items = query(GAMEID + "= ? ", new String[] { ""+gameId });
//		for (int i = 0; i < items.length; i++) {
//			Log.d(CLASSNAME, "Results from query "+ gameId +" > Gen item id" + items[i].getId());
//		}
		return items;

	}
	
	
	public GeneralItem[] query(long gameId, int visibility) {
		return query(GAMEID + "= ? and "+VISIBILITY_STATUS + " = ? ", new String[] { ""+gameId, ""+visibility });
	}
	
	public static HashMap<Long, GeneralItem> gic = new HashMap<Long, GeneralItem>();
	
	private GeneralItem[] query(String selection, String[] selectionArgs) {
		GeneralItem[] resultGenIt = null;
		Cursor mCursor = null;
		try {
			mCursor = db.getSQLiteDb().query(true, getTableName(), null, selection, selectionArgs, null, null, SORTKEY+" DESC, "+FIRST_READ+" DESC", null);
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

	
	
	public void setFirstRead(Long itemId, long date) {
		ContentValues newValue = new ContentValues();
		newValue.put(FIRST_READ, date);
		queryCache = null;
		db.getSQLiteDb().update(getTableName(), newValue, ID + "=? and "+FIRST_READ +" = 0", new String[] { ""+itemId });

	}
	
	public void resetVisiblity(Long gameId) {
		GeneralItem[] items = query(GAMEID + "= ? ", new String[] { ""+gameId, "1"});
		for (int i = 0; i < items.length; i++) {
			queryCache = null;
			ContentValues newValue = new ContentValues();
			newValue.put(VISIBILITY_STATUS, NOT_INITIALISED);
			newValue.put(FIRST_READ, 0);
			db.getSQLiteDb().update(getTableName(), newValue, GAMEID + "= ?  and "+ID + "=?", new String[] { ""+ gameId, "" + items[i].getId() });
		}
	}

	
	public void setVisiblityStatus(Long gameId, Long generalItemId, int visiblityStatus, long timeAt) {
		queryCache = null;
		ContentValues newValue = new ContentValues();
		newValue.put(VISIBILITY_STATUS, visiblityStatus);
		newValue.put(FIRST_READ, timeAt);
		db.getSQLiteDb().update(getTableName(), newValue, GAMEID + "= ?  and "+ID + "=?", new String[] { ""+gameId, ""+generalItemId });	
	}
	
	public int getVisiblityStatus(Long gameId, Long generalItemId) {
		try {
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null, GAMEID + "= ?  and "+ID + "=?", new String[] { ""+gameId, ""+generalItemId }, null, null, FIRST_READ+" DESC", null);
			
			if (mCursor.moveToNext()) {
				int result = mCursor.getInt(11); 
				mCursor.close();
				return result; 

			}
			mCursor.close();
		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
		return -1;
	}
}
