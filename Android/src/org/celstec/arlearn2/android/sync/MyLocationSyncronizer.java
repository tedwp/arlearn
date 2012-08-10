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
		DBAdapter db = new DBAdapter(ctx);
		db.openForWrite();
		Location locs[] = (Location[]) ((MyLocations)db.table(DBAdapter.MYLOCATIONS_ADAPTER)).query();
//		if (locs.length > 0) Log.e("locat lat", ""+locs[0].getLat());
		if (locs.length == 0) {
			increaseDelay();
		} else {
			setDelay(myLocationSyncronizerDelay);
			publishLocation(db, locs);
		}
		db.close();
	}
	
	private void publishLocation(DBAdapter db, Location[] loc) {
		LocationList ll = new LocationList();
		ll.addLocations(loc);
		ll.setTimestamp(System.currentTimeMillis());
		try {
			LocationList listResult = UserClient.getUserClient().publishLocations(pa.getFusionAuthToken(), ll, pa.getCurrentRunId());
	
			MyLocations dbAdapter = ((MyLocations) db.table(DBAdapter.MYLOCATIONS_ADAPTER));
			dbAdapter.confirmReplicated(listResult.getLocations());

		} catch (Exception e) {
			Log.e("exception", e.getMessage(), e);
			increaseDelay();
		}
		
	}

}
