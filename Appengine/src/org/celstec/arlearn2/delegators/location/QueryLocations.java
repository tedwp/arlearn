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
