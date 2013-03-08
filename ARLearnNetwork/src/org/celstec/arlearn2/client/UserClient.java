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
package org.celstec.arlearn2.client;

import org.celstec.arlearn2.beans.run.LocationList;
import org.celstec.arlearn2.beans.run.User;

public class UserClient extends GenericClient{
	

	private static UserClient instance;

	private UserClient() {
		super("/users");
	}

	public static UserClient getUserClient() {
		if (instance == null) {
			instance = new UserClient();
		}
		return instance;
	}

	public LocationList publishLocations(String token, LocationList locations, Long runId) {
		return (LocationList) executePost(getUrlPrefix()+ "/locations/runId/"+runId, token, locations, LocationList.class);
	}
	
	public User getUser(String token, Long runId, String userEmail) {
		return (User)  executeGet(getUrlPrefix()+"/loggedInUser/"+runId+"/email/"+userEmail, token, User.class);
	}
	
	public User createUser(String token, User user) {
		return (User) executePost(getUrlPrefix(), token, user, User.class);
	}
	
	public User deleteUser(String token, long runId){
		return (User) executeDelete(getUrlPrefix()+"/runId/"+runId, token, User.class);
	}
}
