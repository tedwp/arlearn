package org.celstec.arlearn2.gwtcommonlib.client.network;

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
	
	public void getTeams(long runId, final JsonCallback jcb) {
		invokeJsonGET("/runId/"+runId, jcb);
	}
	
}
