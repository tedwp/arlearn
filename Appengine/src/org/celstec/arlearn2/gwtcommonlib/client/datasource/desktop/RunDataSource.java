package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;

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
		((RunClient) getHttpClient()).runsParticipate(new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()));
	}

	@Override
	public void removeRecord(Object id) {
		// TODO Auto-generated method stub
		
	}

	protected String getBeanType() {
		return "runs";
	}
	
}
