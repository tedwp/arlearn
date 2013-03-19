package org.celstec.arlearn2.gwtcommonlib.client.network.generalItem;

import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;

import com.google.gwt.json.client.JSONObject;

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
	
	public void getDisappearedGeneralItemsRun(long runId, final JsonCallback jcb) {
		invokeJsonGET("/runId/"+runId+"/disappeared", jcb);
	}
	
	public void getGeneralItemsGame(long gameId, final JsonCallback jcb) {
		invokeJsonGET("/gameId/"+gameId, jcb);
	}
	
	public void getGeneralItem(long gameId, long itemId, final JsonCallback jcb) {
		invokeJsonGET("/gameId/"+gameId+"/generalItem/"+itemId+"/", jcb);
	}
	
	public void createGeneralItem(JSONObject object, JsonCallback jcb) {
		invokeJsonPOST(null, object, jcb);
	}
	
	public void deleteGeneralItem(long gameId, long itemId, final JsonCallback jcb) {
		invokeJsonDELETE("/gameId/"+gameId+"/generalItem/"+itemId+"/", jcb);
	}
	
	public String getUrl() {
		return super.getUrl() + "generalItems";
	}
}

