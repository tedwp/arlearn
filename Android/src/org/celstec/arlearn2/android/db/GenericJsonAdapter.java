package org.celstec.arlearn2.android.db;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class GenericJsonAdapter extends GenericDbTable {

	public GenericJsonAdapter(DBAdapter db) {
		super(db);
	}


	public JSONObject queryAll(String tableName) {
		JSONObject returnJSON = new JSONObject();
		
		Cursor mCursor = null;
		try {
			String queryString = "select * from "+tableName;
			if (tableName.equals("myActions")) queryString += " where replicated = 0";
			if (tableName.equals("run")) queryString += " where deleted = 0";
			mCursor = db.getSQLiteDb().rawQuery(queryString, null);
//			mCursor = db.getSQLiteDb().query(true, getTableName(), null, null, null, null, null, null, null);
			int i = 0;
			String columnNames[] = mCursor.getColumnNames();
			JSONArray array = new JSONArray();
			for (int j = 0; j < columnNames.length; j++) {
				array.put(columnNames[j]);

			}
			returnJSON.put("columns", array);
			JSONArray rowArray = new JSONArray();
			while (mCursor.moveToNext()) {
				JSONObject row = new JSONObject();
				for (int j = 0; j < columnNames.length; j++) {
					String value =mCursor.getString(mCursor.getColumnIndex(columnNames[j]));
					row.put(columnNames[j], value);

				}
				rowArray.put(row);
			}
			returnJSON.put("rows", rowArray);

		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		} catch (JSONException e) {
			Log.e("sqlex", "ex", e);
		} finally {
			if (mCursor != null)  mCursor.close();
		}
		
		return returnJSON;
	}
	
	@Override
	public String createStatement() {
		return null;
	}

	@Override
	protected String getTableName() {
		return null;
	}
	
	public String eraseAllStatement() {
		return null;
	}
	
	public String dropStatement() {
		return null;
	}

}
