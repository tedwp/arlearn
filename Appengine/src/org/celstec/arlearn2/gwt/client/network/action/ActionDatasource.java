package org.celstec.arlearn2.gwt.client.network.action;

import org.celstec.arlearn2.gwt.client.network.DerivedFieldTask;
import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.GenericDataSource;
import org.celstec.arlearn2.gwt.client.network.team.TeamsDataSource;
import org.celstec.arlearn2.gwt.client.network.user.UsersDataSource;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ActionDatasource extends GenericDataSource {
	
	public static ActionDatasource instance;

	public static ActionDatasource getInstance() {
		if (instance == null)
			instance = new ActionDatasource();
		return instance;
	}

	private ActionDatasource() {
		super();
	}
	
	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, "runId", false, false);
		addField(STRING_DATA_TYPE, "userEmail", false, false);
		addField(STRING_DATA_TYPE, "action", false, false);
		addField(LONG_DATA_TYPE, "timestamp", true, false);
		addDerivedField(new DerivedFieldTask() {
			
			@Override
			public String processValue(String... value) {
				return value[0]+value[1];
			}
			
			@Override
			public String getTargetFieldName() {
				return "pk";
			}
			
			@Override
			public String[] getSourceFieldName() {
				return new String[] {"userEmail", "timestamp"};
			}
			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}
		}, true, false);
		addDerivedField(new DerivedFieldTask() {
			
			@Override
			public String processValue(String... value) {
				return TeamsDataSource.getInstance().getTeamName(UsersDataSource.getInstance().getTeam(Long.parseLong(value[0]), value[1]));
			}
			
			@Override
			public String getTargetFieldName() {
				return "team";
			}
			
			@Override
			public String[] getSourceFieldName() {
				return new String[] {"runId", "userEmail"};
			}
			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}
		}, false, false);
	}
	
	protected GenericClient getHttpClient() {
		return ActionClient.getInstance();
	}

	@Override
	protected String getBeanType() {
		return "actions";
	}

	@Override
	protected void processRecord(ListGridRecord rec) {
		System.out.println("process action rec"+rec.getAttributeAsLong("timestamp"));
	}
	public void deleteRun(long runId) {
		ListGridRecord rec = new ListGridRecord();
		rec.setAttribute("runId", runId);
		removeData(rec);
	}
	
	public void deleteAccount(String userEmail) {
		ListGridRecord rec = new ListGridRecord();
		rec.setAttribute("userEmail", userEmail);
		removeData(rec);
	}

	
	
}
