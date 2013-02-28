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

import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;

import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
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
				+ ID + " text primary key, " //0
				+ GAMEID + " long not null, "
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
			initialValues.put(GAMEID, gi.getGameId());
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
		GeneralItemsCache.getInstance().removeItem(GeneralItemsCache.getInstance().getGeneralItems(id));
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

	
	public void queryAll(DBAdapter db, long gameId) {
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
