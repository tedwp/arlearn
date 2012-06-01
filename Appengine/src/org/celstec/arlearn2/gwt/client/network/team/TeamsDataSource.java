package org.celstec.arlearn2.gwt.client.network.team;

import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.GenericDataSource;

public class TeamsDataSource extends GenericDataSource {
	
	public static TeamsDataSource instance;

	public static TeamsDataSource getInstance() {
		if (instance == null)
			instance = new TeamsDataSource();
		return instance;
	}

	private TeamsDataSource() {
		super();
	}
	
	@Override
	protected void initFields() {
		addField(STRING_DATA_TYPE, "teamId", true, true);
		addField(STRING_DATA_TYPE, "name", false, true);
		addField(INTEGER_DATA_TYPE, "runId", false, true);
	}
	
	protected GenericClient getHttpClient() {
		return TeamClient.getInstance();
	}
	
	@Override
	protected String getBeanType() {
		return "teams";
	}

}
