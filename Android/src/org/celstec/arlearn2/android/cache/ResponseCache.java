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
	
//	public void reloadFromDb(final long runId, final long generalItemId, final Context ctx) {
//		DBAdapter.getAdapter(ctx).getMyResponses().query(runId, generalItemId, new ResponseResults() {
//			
//			@Override
//			public void onResults(Response[] responses) {
//				String key = getKey(runId, generalItemId);
//				if (responseMap.get(key) == null) {
//					responseMap.put(key, new TreeSet<Response>());	
//				}
//				for (Response response : responses){
//					responseMap.get(key).add(response);
//				}
//			}
//		}); 
//	}
	
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
}
