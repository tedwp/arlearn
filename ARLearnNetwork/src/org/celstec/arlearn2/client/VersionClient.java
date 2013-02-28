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

import org.celstec.arlearn2.beans.Version;

public class VersionClient extends GenericClient{
	
	private static VersionClient instance;

	private VersionClient() {
		super("/version");
	}

	public static VersionClient getVersionClient() {
		if (instance == null) {
			instance = new VersionClient();
		}
		return instance;
	}

	public Version getVersionInfo(int versionCode) {
		return (Version)  executeGet(getUrlPrefix()+"/"+versionCode, null, Version.class);
	}
	
	public Version createVersion(Version version) {
		return (Version) executePost(getUrlPrefix(), null, version, Version.class);
	}
}
