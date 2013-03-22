package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonObjectListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.TeamModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.TeamClient;

public class TeamDataSource extends GenericDataSource {

	public static TeamDataSource instance;

	public static TeamDataSource getInstance() {
		if (instance == null)
			instance = new TeamDataSource();
		return instance;
	}
	
	private TeamDataSource() {
		super();
		setDataSourceModel(new TeamModel(this));
	}
	
	public GenericClient getHttpClient() {
		return TeamClient.getInstance();
	}
	
	@Override
	public void loadDataFromWeb() {
		// TODO Auto-generated method stub
		
	}
	
	public void loadDataFromWeb(long runId) {
		((TeamClient) getHttpClient()).getTeams(runId, new JsonObjectListCallback(getBeanType(), this.getDataSourceModel()));
	}

	@Override
	public void removeRecord(Object id) {
		
	}

	protected String getBeanType() {
		return "teams";
	}
}
