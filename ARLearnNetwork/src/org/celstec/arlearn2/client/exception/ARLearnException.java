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
package org.celstec.arlearn2.client.exception;

import org.celstec.arlearn2.beans.Bean;

public class ARLearnException extends RuntimeException {

	public static final int RUNNOTFOUND = 1;
	public static final int NOT_AUTHENTICATED = 2;

	
	private int code;
	
	public ARLearnException(int code) {
		this.code = code;
	}
	
	public ARLearnException(String messsage) {
		super(messsage);
	}
	
	public boolean invalidCredentials() {
		return this.code == Bean.INVALID_CREDENTIALS;
	}
}
