package org.celstec.arlearn2.gwt.client.network.action;

import org.celstec.arlearn2.gwt.client.network.GenericClient;

public class ActionClient extends GenericClient {

	private static ActionClient instance;
	private ActionClient() {
	}
	
	public static ActionClient getInstance() {
		if (instance == null) instance = new ActionClient();
		return instance;
	}
	
	public String getUrl() {
		return super.getUrl() + "actions";
	}
	
}
