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

import java.util.List;

import org.celstec.arlearn2.beans.run.Location;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class MyLocations  extends GenericDbTable {

	public static final String MYLOCATIONS_TABLE = "myLocations";
	public static final String ACCOUNT = "account";
	public static final String LAT = "lat";
	public static final String LNG = "lng";
	public static final String TIMESTAMP = "timestamp";
	public static final String REPLICATED = "replicated";

	public MyLocations(DBAdapter db) {
		super(db);
	}
	
	public String createStatement() {
		return "create table " + MYLOCATIONS_TABLE + " (" 
				+ ACCOUNT + " text not null, " 
				+ LAT + " double not null, " 
				+ LNG + " double not null, " 
				+ REPLICATED + " boolean not null, " 
				+ TIMESTAMP + " long not null);";
	}

	protected String getTableName() {
		return MYLOCATIONS_TABLE;
	}

	
	public boolean insert(Location loc) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(ACCOUNT, loc.getAccount());
        initialValues.put(LAT, loc.getLat());
        initialValues.put(LNG, loc.getLng());
        initialValues.put(REPLICATED, false);
        initialValues.put(TIMESTAMP, loc.getTimestamp());
    	return db.getSQLiteDb().insert(getTableName(), null, initialValues) != -1;	
	}
	
	

	@Override
	public int delete(Object o) {
		return 0;
	}

	public Object[] query() {
		Location[] locations = null;
		try {
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null,
					REPLICATED+ " = 0", null, null, null, null, null);
			locations= new Location[mCursor.getCount()];
			int i = 0;
			while (mCursor.moveToNext()) {
				Location l = new Location();
				l.setAccount(mCursor.getString(0));
				l.setLat(mCursor.getDouble(1));
				l.setLng(mCursor.getDouble(2));
				l.setTimestamp(mCursor.getLong(4));
				locations[i++] = l;
			}
			mCursor.close();

		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
		return locations;
	}

	public Object queryById(Object id) {
		return null;
	}

	public void confirmReplicated(List<Location> locations) {
		ContentValues newValue = new ContentValues();
        newValue.put(REPLICATED, true);
        for (Location loc: locations) {
        	db.getSQLiteDb().update(getTableName(), newValue, TIMESTAMP+"=?" , new String[]{""+loc.getTimestamp()});
        }
		
	}

}
