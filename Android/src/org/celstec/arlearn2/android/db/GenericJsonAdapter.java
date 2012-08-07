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
			mCursor = db.getSQLiteDb().rawQuery("select * from "+tableName, null);
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

	@Override
	public boolean insert(Object o) {
		return false;
	}
	
	public String eraseAllStatement() {
		return null;
	}
	
	public String dropStatement() {
		return null;
	}

}
