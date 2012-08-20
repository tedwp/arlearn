package org.celstec.arlearn2.gwt.client.network.response;

import org.celstec.arlearn2.gwt.client.network.DerivedFieldTask;
import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.GenericDataSource;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ResponseDataSource extends GenericDataSource {
	
	public static final String GENERALITEMID = "generalItemId";
	public static final String RUNID = "runId";
	public static final String ACCOUNT = "userEmail";
	public static final String RESPONSEVALUE = "responseValue";
	public static final String RESPONSEITEMID = "responseItemId";
	public static final String TIMESTAMP = "timestamp";
	public static ResponseDataSource instance;

	public static ResponseDataSource getInstance() {
		if (instance == null)
			instance = new ResponseDataSource();
		return instance;
	}

	private ResponseDataSource() {
		super();
		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.run.Response", responseNotitificationHandler);

	}
	
	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, RUNID, false, false);
		addField(LONG_DATA_TYPE, GENERALITEMID, false, false);
		addField(LONG_DATA_TYPE, "timestamp", false, false);
		addField(STRING_DATA_TYPE, ACCOUNT, false, false);
		addField(STRING_DATA_TYPE, RESPONSEVALUE, false, false);
		addField(STRING_DATA_TYPE, RESPONSEITEMID, false, false);
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
	}
	
	protected GenericClient getHttpClient() {
		return ResponseClient.getInstance();
	}

	@Override
	protected String getBeanType() {
		return "responses";
	}
	
	public void deleteRunAccount(long runId,String userEmail) {
		ListGridRecord rec = new ListGridRecord();
		rec.setAttribute("runId", runId);
		rec.setAttribute("userEmail", userEmail);
		removeData(rec);
	}
	
	public NotificationHandler responseNotitificationHandler = new NotificationHandler() {

		@Override
		public void onNotification(JSONObject bean) {
			addBean(bean);
			
		}

	};

}
