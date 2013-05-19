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
package org.celstec.arlearn2.android.sync;

import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MyLocations;
import org.celstec.arlearn2.beans.run.LocationList;
import org.celstec.arlearn2.beans.run.Location;
import org.celstec.arlearn2.client.UserClient;

import android.content.Context;
import android.util.Log;

public class MyLocationSyncronizer extends GenericSyncroniser {

	private final int myLocationSyncronizerDelay = 10;
	
	public MyLocationSyncronizer(Context ctx) {
		super(ctx);
		setDelay(myLocationSyncronizerDelay);
	}
	
	@Override
	protected void runAuthenticated() {
//		DBAdapter db = new DBAdapter(ctx);
//		db.openForWrite();
//		Location locs[] = (Location[]) ((MyLocations)db.table(DBAdapter.MYLOCATIONS_ADAPTER)).query();
////		if (locs.length > 0) Log.e("locat lat", ""+locs[0].getLat());
//		if (locs.length == 0) {
//			increaseDelay();
//		} else {
//			setDelay(myLocationSyncronizerDelay);
//			publishLocation(db, locs);
//		}
//		db.close();
	}
	
	private void publishLocation(DBAdapter db, Location[] loc) {
		LocationList ll = new LocationList();
		ll.addLocations(loc);
		ll.setTimestamp(System.currentTimeMillis());
		try {
			LocationList listResult = UserClient.getUserClient().publishLocations(pa.getAuthToken(), ll, pa.getCurrentRunId());
	
			MyLocations dbAdapter = ((MyLocations) db.table(DBAdapter.MYLOCATIONS_ADAPTER));
			dbAdapter.confirmReplicated(listResult.getLocations());

		} catch (Exception e) {
			Log.e("exception", e.getMessage(), e);
			increaseDelay();
		}
		
	}

}
