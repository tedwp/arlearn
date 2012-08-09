package org.celstec.arlearn2.gwt.client.network.team;

import java.util.HashMap;

import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.GenericDataSource;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class TeamsDataSource extends GenericDataSource {
	
	public static TeamsDataSource instance;
	private HashMap<String, String> teamNameMap = new HashMap<String, String>();

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
	
	protected void processRecord(ListGridRecord rec) {
		String name = rec.getAttributeAsString("name");
		String team = rec.getAttributeAsString("teamId");
		teamNameMap.put(team, name);
	}
	
	public String getTeamName(String teamId) {
		if (teamId == null) return null;
		return teamNameMap.get(teamId);
	}

}
