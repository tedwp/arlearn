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
package org.celstec.arlearn2.delegators;

import java.util.logging.Logger;
import com.google.gdata.util.AuthenticationException;

public class GoogleDelegator {
	protected static final Logger logger = Logger.getLogger(GoogleDelegator.class.getName());

	protected String authToken;

	public GoogleDelegator(String authToken) throws AuthenticationException {
		if (authToken == null) {
			this.authToken = null;
		} else {
			authToken = authToken.substring(authToken.indexOf("auth=") + 5);
			this.authToken = authToken;
		}
	}

	public GoogleDelegator(GoogleDelegator gd) {
		this.authToken = gd.authToken;
	}

	public GoogleDelegator() {
	}

	public String getAuthToken() {
		return authToken;
	}

}
