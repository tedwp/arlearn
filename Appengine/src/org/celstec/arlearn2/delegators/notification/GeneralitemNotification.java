package org.celstec.arlearn2.delegators.notification;

import java.util.HashMap;

import org.celstec.arlearn2.tasks.beans.NotifyItemVisible;

public class GeneralitemNotification extends Notification {
	public static final String ITEMID = "ITEMID";

	public GeneralitemNotification(int scope, long runId, String authToken) {
		super(scope, runId, authToken);
	}
	
	public GeneralitemNotification(String scope, long runId, String authToken) {
		super(scope, runId, authToken);
	}
	
	public void itemDeleted(String itemId) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("action", "deleted");
		hm.put("itemId", itemId);
		notify("GeneralItem", hm);
	}
	
	
	public void itemVisible(NotifyItemVisible niv) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("action", "visible");
		hm.put("itemId", ""+niv.getGeneralItemId());
		hm.put("name", niv.getName());
		if (niv.getLat()!=null) hm.put("lat", ""+niv.getLat());
		if (niv.getLng()!=null) hm.put("lng", ""+niv.getLng());
		hm.put("runId", ""+niv.getRunId());
		notify("GeneralItem", hm);
	}
	
}
