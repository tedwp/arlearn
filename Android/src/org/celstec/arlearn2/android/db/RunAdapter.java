package org.celstec.arlearn2.android.db;

//import org.celstec.arlearn2.genericBeans.Run;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Run;

//import org.celstec.arlearn2.android.db.beans.GeneralItem;
//import org.celstec.arlearn2.android.db.beans.Run;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class RunAdapter extends GenericDbTable {

	public static final String RUN_TABLE = "run";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String START_TIME = "startTime";
	public static final String SERVER_CREATE_TIME = "serverCreateTime";
	public static final String GAMEID = "gameId";
	public static final String ROLES = "roles";

	public RunAdapter(DBAdapter db) {
		super(db);
	}
	
	public String createStatement() {
		return "create table " + RUN_TABLE + " (" 
				+ ID + " long primary key, " 
				+ TITLE + " text not null, " 
				+ SERVER_CREATE_TIME + " long not null, "
				+ START_TIME + " long not null, "
				+ GAMEID + " long, "
				+ ROLES + " text);";
	}
	
	protected String getTableName() {
		return RUN_TABLE;
	}
	
	@Deprecated
	public boolean insert (List<Run> runs) {
		if (runs.isEmpty()){
			return true;
		}
		boolean returnValue = false;
		Run[] oldRunsArray = query();
		HashSet<Long> oldRunIds = new HashSet<Long>();
//		HashSet<Long> updatedRunIds = new HashSet<Long>();
		HashMap<Long, Run> newRuns = new HashMap<Long, Run>();
		HashMap<Long, Run> oldRuns = new HashMap<Long, Run>();
		
		for (Run newRun : runs) {
			newRuns.put(newRun.getRunId(), newRun);
		}
		for (Run oldRun : oldRunsArray) {
			oldRunIds.add(oldRun.getRunId());
			oldRuns.put(oldRun.getRunId(), oldRun);
		}
		for (Iterator iterator = newRuns.keySet().iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			Run newRun = newRuns.get(id);
			if (oldRunIds.contains(id)) {
				if (newRun.getServerCreationTime().equals(oldRuns.get(id).getServerCreationTime())){
					oldRunIds.remove(id);
//					delete(id);
					returnValue = true;
//					insert(newRun);
				} 
				
//				returnValue = updateRun(newRuns.get(id)) || returnValue;
//				
			} else {
				insert(newRun);
				returnValue = true;
			}
		}
		deleteOldRuns(oldRunIds);
		return returnValue;
	}
	
	

//	private boolean updateRun(Run r) {
//		
//		return false;
//	}
	
	public boolean insert(Object o, List<String> roles) {
		Run r = (Run) o;
		Run oldRun =  (Run) queryById(r.getRunId());
		if (oldRun != null) {
			if (!oldRun.equals(r)) {
				return update(r, roles);
			} else {
				return false;	
			}
		}
		
		delete(r.getRunId());
    	ContentValues initialValues = new ContentValues();
		initialValues.put(ID, r.getRunId());
        initialValues.put(TITLE, r.getTitle());
        initialValues.put(START_TIME, r.getStartTime());
        initialValues.put(SERVER_CREATE_TIME, r.getServerCreationTime());
        initialValues.put(ROLES, roles.toString());
    	return db.getSQLiteDb().insert(getTableName(), null, initialValues) != -1;	
	}
	
	public boolean insert(Object o) {
		Run r = (Run) o;
		Run oldRun =  (Run) queryById(r.getRunId());
		if (oldRun != null) {
			if (!oldRun.equals(r)) {
				return update(r, null);
			} else {
				return false;	
			}
		}
//		delete(r.getRunId());
    	ContentValues initialValues = new ContentValues();
		initialValues.put(ID, r.getRunId());
        initialValues.put(TITLE, r.getTitle());
        initialValues.put(START_TIME, r.getStartTime());
        initialValues.put(SERVER_CREATE_TIME, r.getServerCreationTime());
    	return db.getSQLiteDb().insert(getTableName(), null, initialValues) != -1;	
		
	}
	
	private boolean update(Run r, List<String> roles) {
		ContentValues initialValues = new ContentValues();
        initialValues.put(TITLE, r.getTitle());
        initialValues.put(START_TIME, r.getStartTime());
        initialValues.put(GAMEID, r.getGameId());
        if (roles != null)  initialValues.put(ROLES, roles.toString());

        
		return db.getSQLiteDb().update(getTableName(), initialValues, ID+" = "+r.getRunId(), null) != -1;
	}

	private void deleteOldRuns(HashSet<Long> oldRunIds) {
		for (Iterator iterator = oldRunIds.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			delete(id);
		}
	}
	
	public int delete(Object o) {
		Long id = (Long) o;
		return delete(id);
	}
	
	public int delete(Long id) {
		((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).deleteRun(id);
		((MyActions) db.table(DBAdapter.MYACTIONS_ADAPTER)).deleteRun(id);
		//todo switch following line on
		((MediaCache) db.table(DBAdapter.MEDIA_CACHE)).deleteRun(id);
		return db.getSQLiteDb().delete(getTableName(), ID+" = "+id, null);
	}
	


	private Run[] query(String selection, String[] selectionArgs) {
		Run[] resultRuns = null;
		try {
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null, selection, selectionArgs, null, null, null, null);
			resultRuns = cursorToArray(mCursor);
		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
		return resultRuns;
	}
	
	private Run[] cursorToArray(Cursor mCursor) {
		Run[] resultRuns = null;
		resultRuns = new Run[mCursor.getCount()];
		int i = 0;
		
		while (mCursor.moveToNext()) {
			Run r = new Run();
			r.setRunId(mCursor.getLong(0));
			r.setTitle(mCursor.getString(1));
			r.setStartTime(mCursor.getLong(2));
			r.setServerCreationTime(mCursor.getLong(3));
			r.setGameId(mCursor.getLong(4));
			resultRuns[i++] = r;
		}
		mCursor.close();
		return resultRuns;
	}
	
	public Run[] query() {
		return query(null, null);
	}
	
	public String queryRoles(long id) {
		String selection = ID + " = ?";
		String[] selectionArgs = new String[] { ""+ id };
		try {
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null, selection, selectionArgs, null, null, null, null);
			if (mCursor.moveToNext()) {
				return mCursor.getString(5);
			}
		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
		return "";
	}
	
	public Object queryById(Object id) {
		try {
			return query(ID + " = ?", new String[] { ""+ id })[0];
		} catch (Exception e) {
			return null;
		}
	}

	public Run[] runsWithoutGame() {
		try {
			return query(GAMEID + " IS NULL", new String[] {});
		} catch (Exception e) {
			return null;
		}
	}
	
	public Run[] runsWithGame() {
		String query = "SELECT * FROM "+getTableName()+" r INNER JOIN "+GameAdapter.GAME_TABLE+" g ON r."+GAMEID+"=g."+GameAdapter.ID;
		try {
			Cursor mCursor = db.getSQLiteDb().rawQuery(query, new String[]{});
			return cursorToArray(mCursor);
		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
		return new Run[]{};
	}

}
