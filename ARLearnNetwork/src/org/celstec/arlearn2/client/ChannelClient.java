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

import org.celstec.arlearn2.beans.run.Run;

public class ChannelClient extends GenericClient{
	
	private static ChannelClient instance;

	private ChannelClient() {
		super("/channelAPI");
	}

	public static ChannelClient getChannelClient() {
		if (instance == null) {
			instance = new ChannelClient();
		}
		return instance;
	}
	
	public void pong(long runId, String token, String from, String to, int requestType, String response, long origTimeStamp) {
		executePost(getUrlPrefix()+"/pong/"+from+"/"+to+"/"+requestType+"/"+origTimeStamp, token, response, null);
	}

}
