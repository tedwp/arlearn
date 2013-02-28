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
package org.celstec.arlearn2.android.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemVisibility;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Response;

import android.content.Context;

public class ResponseCache {
	
	private static ResponseCache instance;
	
	private HashMap<String, TreeSet<Response>> responseMap = new HashMap<String, TreeSet<Response>>();
	private HashMap<Long, TreeSet<Response>> locatedResponses = new HashMap<Long, TreeSet<Response>>();
	
	private ResponseCache() {
		
	}
	
	public static ResponseCache getInstance() {
		if (instance == null) {
			instance = new ResponseCache();
		}
		return instance;
	}
	
	public void empty() {
		 responseMap = new HashMap<String, TreeSet<Response>>();
		 locatedResponses = new HashMap<Long, TreeSet<Response>>();
	}

	
	public TreeSet<Response> getLocatedResponses(long runId, Context ctx) {
		if (!locatedResponses.containsKey(runId)) return null;
		return (TreeSet<Response>) locatedResponses.get(runId).clone();
	}
	
	public TreeSet<Response> getResponses(long runId, long generalItemId) {
		return responseMap.get(runId+"*"+generalItemId);
	}

	
	private String getKey(Response response) {
		return response.getRunId()+"*"+response.getGeneralItemId();
	}
	private String getKey(final long runId, final long generalItemId) {
		return runId+"*"+generalItemId;
	}

	public void put(Response response) {
		String key = getKey(response);
		synchronized (responseMap) {
			if (responseMap.get(key) == null) {
				responseMap.put(key, new TreeSet<Response>());
			}
			responseMap.get(key).add(response);
		}
	}
	
	public void remove(long runId) {
		synchronized (responseMap) {
			for (String key : responseMap.keySet()) {
				if (key.startsWith(runId + "*")) {
					responseMap.remove(key);
				}
			}
		}
		synchronized (locatedResponses) {
			locatedResponses.remove(runId);
		}
	}
}
