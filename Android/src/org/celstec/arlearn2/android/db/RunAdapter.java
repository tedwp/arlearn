package org.celstec.arlearn2.android.db;

//import org.celstec.arlearn2.genericBeans.Run;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.android.activities.ListExcursionsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.asynctasks.NetworkQueue;
import org.celstec.arlearn2.android.asynctasks.network.NetworkTask;
import org.celstec.arlearn2.android.cache.GameCache;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.RunCache;
import org.celstec.arlearn2.android.db.GeneralItemVisibility.VisibilityTask;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.client.RunClient;
import org.celstec.arlearn2.client.UserClient;
import org.celstec.arlearn2.client.exception.ARLearnException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONString;

//import org.celstec.arlearn2.android.db.beans.GeneralItem;
//import org.celstec.arlearn2.android.db.beans.Run;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Message;
import android.util.Log;

public class RunAdapter extends GenericDbTable {

	public static final String RUN_TABLE = "run";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String START_TIME = "startTime";
	public static final String SERVER_CREATE_TIME = "serverCreateTime";
	public static final String GAMEID = "gameId";
	public static final String ROLES = "roles";
	public static final String DELETED = "deleted";
	public static final String BEAN = "runBean";


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
				+ ROLES + " text, "
				+ DELETED + " boolean, "
				+ BEAN + " text);";
	}
	
	protected String getTableName() {
		return RUN_TABLE;
	}
	
	
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
        if (roles != null) {
        	JSONArray array = new JSONArray();
        	for (final String role: roles) {
        		array.put(role);
        	}
        	System.out.println("roles "+array);
        	initialValues.put(ROLES, array.toString());
        }
        initialValues.put(BEAN, r.toString());
        
        if (r.getDeleted() == null) {
			initialValues.put(DELETED, false);
		} else {
			initialValues.put(DELETED, r.getDeleted());
		}
    	return db.getSQLiteDb().insert(getTableName(), null, initialValues) != -1;	
	}
	
	
	
	private boolean update(Run r, List<String> roles) {
		ContentValues initialValues = new ContentValues();
        initialValues.put(TITLE, r.getTitle());
        initialValues.put(START_TIME, r.getStartTime());
        initialValues.put(GAMEID, r.getGameId());
        initialValues.put(BEAN, r.toString());

        if (roles != null)  {
        	JSONArray array = new JSONArray();
        	for (final String role: roles) {
        		array.put(role);
        	}
        	System.out.println("roles "+array);
        	initialValues.put(ROLES, array.toString());
        }
        if (r.getDeleted() == null) {
			initialValues.put(DELETED, false);
		} else {
			initialValues.put(DELETED, r.getDeleted());
		}
        RunCache.getInstance().put(r);
		return db.getSQLiteDb().update(getTableName(), initialValues, ID+" = "+r.getRunId(), null) != -1;
	}
	
	

	private void deleteOldRuns(HashSet<Long> oldRunIds) {
		for (Iterator iterator = oldRunIds.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			delete(id);
		}
	}
	
//	public int delete(Object o) {
//		Long id = (Long) o;
//		return delete(id);
//	}
	
	
	

	
	

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
			try {
				String json = mCursor.getString(7);
				if (json != null) {
					r = (Run) JsonBeanDeserializer.deserialize(mCursor.getString(7));
				} else {
					System.out.println("json null");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
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
		return query(DELETED + " = ?", new String[] { "0"});
	}
	
	public String queryRoles(long id) {
		String selection = ID + " = ?";
		String[] selectionArgs = new String[] { ""+ id };
		try {
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null, selection, selectionArgs, null, null, null, null);
			if (mCursor.moveToNext()) {
				String result = mCursor.getString(5);
				mCursor.close();
				return result;
			}
			mCursor.close();
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
	
	public void queryAll(RunResults results) {
		RunQuery query = new RunQuery();
		query.results = results;
		Message m = Message.obtain(DBAdapter.getDatabaseThread(db.getContext()));
		m.obj = query;
		m.sendToTarget();
	}
	
	public void queryAll() {
		try {
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null, DELETED + " = ?", new String[] { "0"}, null, null, null, null);
			for (Run r :cursorToArray(mCursor)) {
				RunCache.getInstance().put(r);
			}
		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
	}
	
	public interface RunResults {
		
		public void onResults(Run[] runs);
	}
	
	public class RunQuery implements DBAdapter.DatabaseTask{
		
		public RunResults results;
		
		@Override
		public void execute(DBAdapter db) {
			Run[] resultRuns = null;
			try {
				Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null, DELETED + " = ?", new String[] { "0"}, null, null, null, null);
				results.onResults(cursorToArray(mCursor));
			} catch (SQLException e) {
				Log.e("sqlex", "ex", e);
			}
		}
		
	}
	
	public void delete(Long id) {
		

		RunDelete task = new RunDelete();
		task.runId = id;
		
		Message m = Message.obtain(DBAdapter.getDatabaseThread(db.getContext()));
		m.obj = task;
		m.sendToTarget();
		
		//TODO extend
//		return db.getSQLiteDb().delete(getTableName(), ID+" = "+id, null);
	}
	
	public class RunDelete implements DBAdapter.DatabaseTask{
		
		public Long runId;
		
		@Override
		public void execute(DBAdapter db) {
			Run[] resultRuns = null;
			try {
				db.getGeneralItemVisibility().deleteRun(runId);
//				((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).deleteRun(id);
				((MyActions) db.table(DBAdapter.MYACTIONS_ADAPTER)).deleteRun(runId);
				((MediaCache) db.table(DBAdapter.MEDIA_CACHE)).deleteRun(runId);
				((MyResponses) db.table(DBAdapter.MYRESPONSES_ADAPTER)).deleteRun(runId);
				db.getSQLiteDb().delete(getTableName(), ID+" = "+runId, null);
				RunCache.getInstance().delete(runId);
				db.getGeneralItemVisibility().deleteRun(runId);
				GeneralItemVisibilityCache.getInstance().remove(runId);
				ActivityUpdater.updateActivities(db.getContext(), ListExcursionsActivity.class.getCanonicalName());
			} catch (SQLException e) {
				Log.e("sqlex", "ex", e);
			}
		}
		
	}
	
	public boolean insert(Object o) {
		
		InsertRunTask task = new InsertRunTask();
		task.run = (Run)  o;
		
		Message m = Message.obtain(DBAdapter.getDatabaseThread(db.getContext()));
		m.obj = task;
		m.sendToTarget();
		return true;
	}
	
	public class InsertRunTask implements DBAdapter.DatabaseTask{
		
		public Run run;
		
		@Override
		public void execute(DBAdapter db) {
			Run cachedRun = RunCache.getInstance().getRun(run.getRunId());
			if (run.equals(cachedRun)) return;
			Run oldRun = (Run) queryById(run.getRunId());
			if (oldRun != null) {
				if (!oldRun.equals(run)) {
					update(run, null);
				} else {
					RunCache.getInstance().put(run);
					return;	
				}
			}
			
			ContentValues initialValues = new ContentValues();
			initialValues.put(ID, run.getRunId());
	        initialValues.put(TITLE, run.getTitle());
	        initialValues.put(START_TIME, run.getStartTime());
	        initialValues.put(SERVER_CREATE_TIME, run.getServerCreationTime());
	        initialValues.put(BEAN, run.toString());

	        if (run.getDeleted() == null) {
				initialValues.put(DELETED, false);
			} else {
				initialValues.put(DELETED, run.getDeleted());
			}
	    	db.getSQLiteDb().insert(getTableName(), null, initialValues);
	    	updateGame(db, run);
			RunCache.getInstance().put(run);
			ActivityUpdater.updateActivities(db.getContext(), ListExcursionsActivity.class.getCanonicalName());
		}
		
		private void updateGame(DBAdapter db, Run r) {
			Game g = r.getGame();
			PropertiesAdapter pa  = PropertiesAdapter.getInstance(db.getContext());
			if (g == null) {
				r = RunClient.getRunClient().getRun(r.getRunId(), pa.getFusionAuthToken());
				g = r.getGame();
			}
			if (g != null) {
				db.getGameAdapter().insert(g);
				GameCache.getInstance().putGame(g.getGameId(), g);
			}
			User u = UserClient.getUserClient().getUser(pa.getFusionAuthToken(), r.getRunId(), pa.getUsername());
			db.getRunAdapter().insert(r, u.getRoles());
			
		}
		
	}
	
	
		protected void setStatusToLogout(Context context) {
			PropertiesAdapter.getInstance(context).disAuthenticate();
			Intent updateIntent = new Intent();
			updateIntent.setAction("org.celstec.arlearn.updateActivities");
			updateIntent.putExtra(ListExcursionsActivity.class.getCanonicalName(), true);
			updateIntent.putExtra(ListMessagesActivity.class.getCanonicalName(), true);
			context.sendBroadcast(updateIntent);
		}
		
	
	
	

}
