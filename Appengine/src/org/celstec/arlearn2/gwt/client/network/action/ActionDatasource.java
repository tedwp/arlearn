package org.celstec.arlearn2.gwt.client.network.action;

import org.celstec.arlearn2.gwt.client.network.DerivedFieldTask;
import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.GenericDataSource;
import org.celstec.arlearn2.gwt.client.network.game.GameNotificationHandler;
import org.celstec.arlearn2.gwt.client.network.team.TeamsDataSource;
import org.celstec.arlearn2.gwt.client.network.user.UsersDataSource;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ActionDatasource extends GenericDataSource {
	public static final String ACTION = "action";
	public static final String GENERALITEMID = "generalItemId";
	public static final String RUNID = "runId";
	public static final String ACCOUNT = "userEmail";
	public static final String TIMESTAMP = "timestamp";

	public static ActionDatasource instance;

	public static ActionDatasource getInstance() {
		if (instance == null)
			instance = new ActionDatasource();
		return instance;
	}

	private ActionDatasource() {
		super();
		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.run.Action", actionNotitificationHandler);

	}
	
	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, RUNID, false, false);
		addField(STRING_DATA_TYPE, ACCOUNT, false, false);
		addField(STRING_DATA_TYPE, ACTION, false, false);
		addField(LONG_DATA_TYPE, TIMESTAMP, true, false);
		addField(LONG_DATA_TYPE, GENERALITEMID, true, false);
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
				return new String[] {ACCOUNT, TIMESTAMP};
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
				return new String[] {RUNID, ACCOUNT};
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

	public void deleteRun(long runId) {
		ListGridRecord rec = new ListGridRecord();
		rec.setAttribute("runId", runId);
		removeData(rec);
	}
	
	public void deleteRunAccount(long runId,String userEmail) {
		ListGridRecord rec = new ListGridRecord();
		rec.setAttribute("runId", runId);
		rec.setAttribute("userEmail", userEmail);
		removeData(rec);
	}
	
	public void deleteAccount(String userEmail) {
		ListGridRecord rec = new ListGridRecord();
		rec.setAttribute("userEmail", userEmail);
		removeData(rec);
	}
	
	public NotificationHandler actionNotitificationHandler = new NotificationHandler() {

		@Override
		public void onNotification(JSONObject bean) {
			addBean(bean);
			
		}
		

		
	};

	
	
}
