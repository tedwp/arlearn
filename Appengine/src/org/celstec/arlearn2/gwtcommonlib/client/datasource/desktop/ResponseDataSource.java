package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.ResponseModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.response.ResponseClient;

public class ResponseDataSource extends GenericDataSource {

public static ResponseDataSource instance;

	
	public static ResponseDataSource getInstance() {
		if (instance == null)
			instance = new ResponseDataSource();
		return instance;
	}
	
	private ResponseDataSource() {
		super();
		setDataSourceModel(new ResponseModel(this));
	}
	
	public GenericClient getHttpClient() {
		return ResponseClient.getInstance();
	}
	
	@Override
	public void loadDataFromWeb() {
		// TODO Auto-generated method stub
		
	}
	
	public void loadDataFromWeb(long runId, String account) {
		((ResponseClient) getHttpClient()).getResponses(runId, account, new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()));
	}

	@Override
	public void removeRecord(Object id) {
		
	}

	protected String getBeanType() {
		return "responses";
	}

	
}
