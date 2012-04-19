package org.celstec.arlearn2.gwt.client.network.team;

import org.celstec.arlearn2.gwt.client.network.GenericClient;

public class TeamClient extends GenericClient {
	
	private static TeamClient instance;
	private TeamClient() {
	}
	
	public static TeamClient getInstance() {
		if (instance == null) instance = new TeamClient();
		return instance;
	}
	
	public String getUrl() {
		return super.getUrl() + "team";
	}
}
