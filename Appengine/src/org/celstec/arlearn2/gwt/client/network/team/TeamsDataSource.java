/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
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
