package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationSubscriber;

import com.google.gwt.json.client.JSONObject;

public class GeneralItemModel extends DataSourceModel {
	
	public final static int CREATED = 1;
	public final static int DELETED = 2;
	public final static int ALTERED = 3;
	public final static int VISIBLE = 4;
	public final static int DISAPPEARED = 5;
	
	public GeneralItemModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}
	
	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, "id", true, true);
		addField(INTEGER_DATA_TYPE, "sortKey", false, true);
		addField(INTEGER_DATA_TYPE, "runId", false, true);
		addField(DOUBLE_DATA_TYPE, "lat", false, true);
		addField(DOUBLE_DATA_TYPE, "lng", false, true);
		addField(STRING_DATA_TYPE, "name", false, false);
		addField(STRING_DATA_TYPE, "description", false, false);
		addField(STRING_DATA_TYPE, "richText", false, false);
		addField(STRING_DATA_TYPE, "type", false, false);
		addField(STRING_DATA_TYPE, "account", false, false);
		addField(STRING_DATA_TYPE, "answer", false, false);
		addField(STRING_AR_DATA_TYPE, "roles", false, false);
		addField(BOOLEAN_DATA_TYPE, "deleted", false, true);
		addField(BOOLEAN_DATA_TYPE, "read", false, false);
		addField(BOOLEAN_DATA_TYPE, "correct", false, false);
		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				System.out.println(jsonObject);
				String firstValue = jsonObject.get("type").isString().stringValue();
				if (firstValue.contains("AudioObject")) return "Audio Object";
				if (firstValue.contains("VideoObject")) return "Video Object";
				if (firstValue.contains("MultipleChoiceTest")) return "Multiple Choice Test";
				if (firstValue.contains("SingleChoiceTest")) return "Single Choice Test";
				if (firstValue.contains("NarratorItem")) return "Narrator Item";
				if (firstValue.contains("OpenUrl")) return "Open URL";
				if (firstValue.contains("YoutubeObject")) return "Youtube movie";
				if (firstValue.contains("OpenBadge")) return "Mozilla Open Badge";
				if (firstValue.contains("ScanTag")) return "Scan Tag";
				return firstValue;
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return "simpleName";
			}
		}, false, false);
	}
	
	@Override
	protected void registerForNotifications() {
//		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.notification.GeneralItemModification",new NotificationHandler() {
//			
//			@Override
//			public void onNotification(JSONObject bean) {
//				
//				switch ((int) bean.get("modificationType").isNumber().doubleValue()) {
//				case DISAPPEARED:
////					removeObject((long) bean.get("generalItem").isObject().get("id").isNumber().doubleValue());
//					break;
//
//				case CREATED:
//					addJsonObject(bean.get("generalItem").isObject());
//					break;
//					
//				case VISIBLE:
//					addJsonObject(bean.get("generalItem").isObject());
//					break;
//				case DELETED:
//					removeObject((long) bean.get("generalItem").isObject().get("id").isNumber().doubleValue());
//					break;
//				default:
//					break;
//				}
////				addJsonObject(bean.get("generalItem").isObject());
//			}
//		});	
	}
}
