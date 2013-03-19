package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import com.google.gwt.json.client.JSONObject;
//import org.celstec.arlearn2.mobileclient.client.common.datasource.mobile.GameDataSource;

public class RunModel extends DataSourceModel {

	public final static int CREATED = 1;
	public final static int DELETED = 2;
	public final static int ALTERED = 3;
	
	public RunModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}

	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, "runId", true, true);
		addField(INTEGER_DATA_TYPE, "gameId", false, true);
		addField(STRING_DATA_TYPE, "title", false, true);
		addField(STRING_DATA_TYPE, "owner", false, true);
		addField(BOOLEAN_DATA_TYPE, "deleted", false, true);
		
	}

	@Override
	protected void registerForNotifications() {
//		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.notification.RunModification",new NotificationHandler() {
//			
//			@Override
//			public void onNotification(JSONObject bean) {
//				switch ((int) bean.get("modificationType").isNumber().doubleValue()) {
//				case CREATED:
//					addJsonObject(bean.get("run").isObject());
//					break;
//				case DELETED:
//					removeObject((long) bean.get("run").isObject().get("runId").isNumber().doubleValue());
//					break;
//				case ALTERED:
//					addJsonObject(bean.get("run").isObject());
//					break;
//				default:
//					break;
//				}
//
//			}
//		});	
	}
	
	@Override
	protected AbstractRecord createRecord(JSONObject object) {
		AbstractRecord record = super.createRecord(object);
//		if (object.containsKey("game") &&  object.get("game").isObject()!=null) {
//			GameDataSource.getInstance().addJsonObject(object.get("game").isObject());
//		}
		return record;
	}
	
	
}
