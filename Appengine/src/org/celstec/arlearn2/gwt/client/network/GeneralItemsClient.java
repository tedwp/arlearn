package org.celstec.arlearn2.gwt.client.network;

public class GeneralItemsClient extends GenericClient {
	private static GeneralItemsClient instance;
	private GeneralItemsClient() {
	}
	
	public static GeneralItemsClient getInstance() {
		if (instance == null) instance = new GeneralItemsClient();
		return instance;
	}
	
	public void getGeneralItemsRun(long runId, final JsonCallback jcb) {
		invokeJsonGET("/runId/"+runId, jcb);
	}
	
	public void getGeneralItemsGame(long gameId, final JsonCallback jcb) {
		invokeJsonGET("/gameId/"+gameId, jcb);
	}
	
	public String getUrl() {
		return super.getUrl() + "generalItems";
	}
}
