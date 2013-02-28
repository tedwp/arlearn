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

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.beans.game.ScoreDefinition;
import org.celstec.arlearn2.network.ConnectionFactory;

public class LoginClient extends GenericClient{

	public static LoginClient instance;

	private LoginClient() {
		super("/login");
	}

	public static LoginClient getLoginClient() {
		if (instance == null) {
			instance = new LoginClient();
		}
		return instance;
	}

	public AuthResponse authenticate(String login, String password) {
		HttpResponse response = ConnectionFactory.getConnection().executePOST(getUrlPrefix(), null, "application/json", login + "\n" + password, "text/plain");
		try {
			String entry = EntityUtils.toString(response.getEntity(), "utf-8");
			return (AuthResponse) jsonDeserialise(entry, AuthResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			AuthResponse rl = new AuthResponse();
			rl.setError("exception " + e.getMessage());
			return rl;
		}

	}
	
	public void instructions(String token, String account) {
		executePost(getUrlPrefix()+"/instructions/"+account, token, null, null);
	}

}
