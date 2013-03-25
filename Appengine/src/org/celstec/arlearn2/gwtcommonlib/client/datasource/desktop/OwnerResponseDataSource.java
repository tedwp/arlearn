package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonResumptionListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.ResponseModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.response.ResponseClient;

import com.google.gwt.json.client.JSONObject;

public class OwnerResponseDataSource extends GenericDataSource {

	public static OwnerResponseDataSource instance;

	public static OwnerResponseDataSource getInstance() {
		if (instance == null)
			instance = new OwnerResponseDataSource();
		return instance;
	}
	
	private OwnerResponseDataSource() {
		super();
		setDataSourceModel(new ResponseModel(this));
	}
	
	public GenericClient getHttpClient() {
		return ResponseClient.getInstance();
	}
	
	@Override
	public void loadDataFromWeb() {
	}
	
	public void loadDataFromWeb(final long runId) {
		JsonResumptionListCallback callback = new JsonResumptionListCallback(getBeanType(), this.getDataSourceModel(), 0l){

			@Override
			public void nextCall() {
				((ResponseClient) getHttpClient()).getResponses(runId, 0, resumptionToken, this);
				
			}
			
		};
		((ResponseClient) getHttpClient()).getResponses(runId, 0, null, callback);
	}

	protected String getBeanType() {
		return "responses";
	}

	@Override
	public void processNotification(JSONObject bean) {
	}
	
	public void setServerTime(long serverTime) {}
}
