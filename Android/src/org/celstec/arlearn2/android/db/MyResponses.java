package org.celstec.arlearn2.android.db;


//import org.celstec.arlearn2.android.db.beans.GeneralItem;
//import org.celstec.arlearn2.android.db.beans.Response;
//import org.celstec.arlearn2.android.db.beans.Run;

import org.celstec.arlearn2.beans.run.Response;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class MyResponses extends GenericDbTable {

	public static final String MYRESPONSES_TABLE = "myResponses";
	public static final String ACCOUNT = "account";
	public static final String GENERAL_ITEM_ID = "generalItemId";
	public static final String TIMESTAMP = "timestamp";
	public static final String RESPONSE = "responseValue";
	public static final String RUNID = "runId";
	public static final String REPLICATED = "replicated";
	public static final String REVOKED = "revoked";

	public MyResponses(DBAdapter db) {
		super(db);
	}

	@Override
	public String createStatement() {
		return "create table " + MYRESPONSES_TABLE + " (" 
				+ ACCOUNT + " text not null, " 
				+ GENERAL_ITEM_ID + " text, " 
				+ TIMESTAMP + " long not null,"
				+ RESPONSE + " text not null, " 
				+ RUNID + " long not null, " 
				+ REPLICATED + " boolean not null, "
				+ REVOKED + " boolean not null);";
	}

	@Override
	protected String getTableName() {
		return MYRESPONSES_TABLE;
	}

	@Override
	public boolean insert(Object o) {
		Response resp = (Response) o;
		ContentValues initialValues = new ContentValues();
		initialValues.put(ACCOUNT, resp.getUserEmail());
		initialValues.put(GENERAL_ITEM_ID, resp.getGeneralItemId());
		initialValues.put(TIMESTAMP, resp.getTimestamp());
		initialValues.put(RESPONSE, resp.getResponseValue());
		initialValues.put(RUNID, resp.getRunId());
		initialValues.put(REPLICATED, false);
		initialValues.put(REVOKED, false);
    	return db.getSQLiteDb().insert(getTableName(), null, initialValues) != -1;	
	}

	
	@Override
	public int delete(Object o) {
		return 0;
	}
	
	public int deleteRun(long runId) {
		return db.getSQLiteDb().delete(getTableName(), RUNID + " = " + runId, null);
	}
	
	
	public Object[] query() {
		Response[] responses = null;
		try {
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null,
					REPLICATED+ " = 0 and "+REVOKED +" = 0", null, null, null, null, null);
			responses= new Response[mCursor.getCount()];
			int i = 0;
			while (mCursor.moveToNext()) {
				Response r = new Response();
				r.setUserEmail(mCursor.getString(0));
				r.setGeneralItemId(mCursor.getLong(1));
				r.setTimestamp(mCursor.getLong(2));
				r.setResponseValue(mCursor.getString(3));
				r.setRunId(mCursor.getLong(4));
				r.setRevoked(mCursor.getInt(6)==1);
				responses[i++] = r;
			}
			mCursor.close();

		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
		return responses;
	}
	
	public Object[] queryRevoked() {
		Response[] responses = null;
		try {
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null,
					REPLICATED+ " = 0 ", null, null, null, null, null);
			responses= new Response[mCursor.getCount()];
			int i = 0;
			while (mCursor.moveToNext()) {
				Response r = new Response();
				r.setUserEmail(mCursor.getString(0));
				r.setGeneralItemId(mCursor.getLong(1));
				r.setTimestamp(mCursor.getLong(2));
				r.setResponseValue(mCursor.getString(3));
				r.setRunId(mCursor.getLong(4));
				r.setRevoked(mCursor.getInt(6)==1);
				responses[i++] = r;
			}
			mCursor.close();

		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
		return responses;
	}
	
	
	
	public Response[] query(long runId, Long generalItemId) {
		try {
			return query(RUNID + "= ? and " + GENERAL_ITEM_ID + " = ? and "+REVOKED +" = 0", new String[] { ""+runId, ""+generalItemId });
		} catch (Exception e) {
			return null;
		}
		
	}
	//
	
	private Response[] query(String selection, String[] selectionArgs) {
		Response[] resultRuns = null;
		try {
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null, selection, selectionArgs, null, null, null, null);
			resultRuns = new Response[mCursor.getCount()];
			int i = 0;
			
			while (mCursor.moveToNext()) {
				Response r = new Response();
				r.setUserEmail(mCursor.getString(0));
				r.setGeneralItemId(mCursor.getLong(1));
				r.setTimestamp(mCursor.getLong(2));
				r.setResponseValue(mCursor.getString(3));
				r.setRunId(mCursor.getLong(4));
				
				resultRuns[i++] = r;
			}
			mCursor.close();

		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
		return resultRuns;
	}	

	public Object queryById(Object id) {
		return null;
	}
	
	public void confirmReplicated(Response response) {
		ContentValues newValue = new ContentValues();
		newValue.put(REPLICATED, true);
		db.getSQLiteDb().update(getTableName(), newValue, TIMESTAMP + "=?", new String[] { "" + response.getTimestamp() });

	}
	
	public void revoke(Response response) {
		ContentValues newValue = new ContentValues();
		newValue.put(REVOKED, true);
		newValue.put(REPLICATED, false);
		db.getSQLiteDb().update(getTableName(), newValue, TIMESTAMP + "=?", new String[] { "" + response.getTimestamp() });
	}

	public Response[] queryWithLocation(long runId) {
		try {
			return query(RUNID + "= ?  and "+GENERAL_ITEM_ID +" is NULL and "+REVOKED +" = 0", new String[] { ""+runId }); // and " + GENERAL_ITEM_ID + " = null
		} catch (Exception e) {
			return null;
		}
	}

}
