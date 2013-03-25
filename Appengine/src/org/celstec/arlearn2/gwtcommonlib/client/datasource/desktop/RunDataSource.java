package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;

import com.google.gwt.json.client.JSONObject;

public class RunDataSource extends GenericDataSource {

	public static RunDataSource instance;

	public static RunDataSource getInstance() {
		if (instance == null)
			instance = new RunDataSource();
		return instance;
	}
	
	private RunDataSource() {
		super();
		setDataSourceModel(new RunModel(this));
	}
	
	public GenericClient getHttpClient() {
		return RunClient.getInstance();
	}
	
	public void loadDataFromWeb() {
		((RunClient) getHttpClient()).runsParticipate(getLastSyncDate(), new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()));
	}

	protected String getBeanType() {
		return "runs";
	}
	
	@Override
	public void processNotification(JSONObject bean) {
		loadDataFromWeb();
	}
	
}
