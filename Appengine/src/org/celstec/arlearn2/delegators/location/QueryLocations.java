package org.celstec.arlearn2.delegators.location;

import org.celstec.arlearn2.beans.run.Location;
import org.celstec.arlearn2.beans.run.LocationList;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.jdo.manager.LocationManager;
import org.celstec.arlearn2.cache.LocationCache;

import com.google.gdata.util.AuthenticationException;

public class QueryLocations extends GoogleDelegator {

	public QueryLocations(GoogleDelegator gd) {
		super(gd);
	}
	
	public QueryLocations(String authtoken) throws AuthenticationException {
		super(authtoken);
	}

	public Location getLastLocation(String userId, Long runIdentifier) {
		
		return LocationManager.getLastLocation(runIdentifier, userId);
	}

}
