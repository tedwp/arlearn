package org.celstec.arlearn2.gwt.client.network.user;

import java.util.HashMap;

import org.celstec.arlearn2.gwt.client.network.DerivedFieldTask;
import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.GenericDataSource;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class UsersDataSource extends GenericDataSource {
	
	public static UsersDataSource instance;
	public static HashMap<String, String> rolesMapping = new HashMap<String, String>();

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
		}, true, false);
	}
	
	protected void processRecord(ListGridRecord rec) {
		rolesMapping.put(rec.getAttribute("pk"), rec.getAttribute("roles"));
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

}
