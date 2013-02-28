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
package org.celstec.arlearn2.gwt.client.network.user;

import java.util.HashMap;

import org.celstec.arlearn2.gwt.client.network.DerivedFieldTask;
import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.GenericDataSource;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class UsersDataSource extends GenericDataSource {
	
	public static UsersDataSource instance;
	public static HashMap<String, String> rolesMapping = new HashMap<String, String>();
	private HashMap<String, String> userTeamMap = new HashMap<String, String>();

	public static UsersDataSource getInstance() {
		if (instance == null)
			instance = new UsersDataSource();
		return instance;
	}

	private UsersDataSource() {
		super();
	}
	
//	DataSourceTextField pkField = new DataSourceTextField("pk");
//	pkField.setHidden(true);
//	pkField.setPrimaryKey(true);

	
	@Override
	protected void initFields() {
		addField(STRING_DATA_TYPE, "teamId", false, true);
		addField(STRING_DATA_TYPE, "email", false, true);
		addField(STRING_DATA_TYPE, "roles", false, true);
		addField(STRING_DATA_TYPE, "name", false, true);
		addField(INTEGER_DATA_TYPE, "runId", false, true);
		addField(BOOLEAN_DATA_TYPE, "deleted", false, true);
		addDerivedField(new DerivedFieldTask() {
			
			@Override
			public String processValue(String... value) {
				return "status_icon_red";
			}
			
			@Override
			public String getTargetFieldName() {
				return "status";
			}
			
			@Override
			public String[] getSourceFieldName() {
				return new String[] {};
			}
			
			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}
			@Override
			public void setJsonSource(JSONObject jsonObject) {}
			
		}, false, false);
		addDerivedField(new DerivedFieldTask() {
			
			@Override
			public String processValue(String... value) {
				if (value == null || "".equals(value)) return "";
				String result = value[0];
				for (int i = 1; i < value.length; i++) {
					result += ":"+value[i];
				}
				return result;
			}
			
			@Override
			public String getTargetFieldName() {
				return "pk";
			}
			
			@Override
			public String[] getSourceFieldName() {
				return new String[] {"runId", "teamId", "email"};
			}
			
			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}
			@Override
			public void setJsonSource(JSONObject jsonObject) {}
			
		}, true, false);
	}
	
	protected void processRecord(ListGridRecord rec) {
		rolesMapping.put(rec.getAttribute("pk"), rec.getAttribute("roles"));
		long runId = rec.getAttributeAsLong("runId");
		String user = rec.getAttributeAsString("email");
		String team = rec.getAttributeAsString("teamId");
		userTeamMap.put(runId+":"+user, team);
	}
	
	public String getRole(String runId, String teamId, String email) {
		return rolesMapping.get(runId+":"+teamId+":"+email);
	}
	
	protected GenericClient getHttpClient() {
		return UserClient.getInstance();
	}
	
	@Override
	protected String getBeanType() {
		return "users";
	}
	
	
	public String getTeam(long runId, String user) {
		return userTeamMap.get(runId+":"+user);
	}

}
