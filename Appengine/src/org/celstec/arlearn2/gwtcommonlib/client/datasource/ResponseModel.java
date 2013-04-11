package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationSubscriber;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

public class ResponseModel extends DataSourceModel {

	public static final String TIMESTAMP_FIELD = "timestamp";
	public static final String USEREMAIL_FIELD = "userEmail";
	public static final String DELETED_FIELD = "deleted";
	public static final String GENERALITEMID_FIELD = "generalItemId";
	public static final String RESPONSE_VALUE_FIELD = "responseValue";
	public static final String ROLE_VALUE_FIELD = "role";
	public static final String WIDTH_FIELD = "width";
	public static final String HEIGHT_FIELD = "height";

	public static final String AUDIO_DEFAULT = "/images/music.png";
	public static final String VIDEO_DEFAULT = "/images/movie.png";
	public static final String TEXT_DEFAULT = "/images/Document.png";
	public static final String OTHER_DEFAULT = "/images/help.png";
	
	public ResponseModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}

	@Override
	protected void initFields() {
		//missing
		//team
		//read
		//correct
		//role
		
		addField(INTEGER_DATA_TYPE, TIMESTAMP_FIELD, true, true);
		addField(INTEGER_DATA_TYPE, RunModel.RUNID_FIELD, true, true);
		addField(BOOLEAN_DATA_TYPE, DELETED_FIELD, false, true);
		addField(INTEGER_DATA_TYPE, GENERALITEMID_FIELD, true, true);
		addField(STRING_DATA_TYPE, USEREMAIL_FIELD, false, true);
		addField(STRING_DATA_TYPE, RESPONSE_VALUE_FIELD, false, true);
		addField(STRING_DATA_TYPE, TeamModel.TEAMID_FIELD, false, true);
		addField(ENUM_DATA_TYPE, ROLE_VALUE_FIELD, false, true);
		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				
				String firstValue = jsonObject.get("responseValue").isString().stringValue();
				JSONObject answer = JSONParser.parseStrict(firstValue).isObject();
				if (answer.containsKey("imageUrl")) {
					return  answer.get("imageUrl").isString().stringValue();
				}else if (answer.containsKey("audioUrl")) {
					return  AUDIO_DEFAULT;
				}else if (answer.containsKey("videoUrl")) {
					return  VIDEO_DEFAULT;
				}
				else if (answer.containsKey("text")) {
					return  TEXT_DEFAULT;
				}
				return OTHER_DEFAULT;
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return "picture";
			}
		}, false, false);
		
		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				
				String firstValue = jsonObject.get("responseValue").isString().stringValue();
				JSONObject answer = JSONParser.parseStrict(firstValue).isObject();
				if (answer.containsKey("audioUrl")) {
					return  answer.get("audioUrl").isString().stringValue();
				}
				return "";
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return "audio";
			}
		}, false, false);
		
		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				
				String firstValue = jsonObject.get("responseValue").isString().stringValue();
				JSONObject answer = JSONParser.parseStrict(firstValue).isObject();
				if (answer.containsKey("videoUrl")) {
					return  answer.get("videoUrl").isString().stringValue();
				}
				return "";
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return "video";
			}
		}, false, false);
		
		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				
				String firstValue = jsonObject.get("responseValue").isString().stringValue();
				JSONObject answer = JSONParser.parseStrict(firstValue).isObject();
				if (answer.containsKey("text")) {
					return  answer.get("text").isString().stringValue();
				}
				return "";
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return "text";
			}
		}, false, false);
		
		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				
				String firstValue = jsonObject.get("responseValue").isString().stringValue();
				JSONObject answer = JSONParser.parseStrict(firstValue).isObject();
				if (answer.containsKey("width")) {
					return (int) answer.get("width").isNumber().doubleValue();
				}
				return 0;
			}

			@Override
			public int getType() {
				return INTEGER_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return WIDTH_FIELD;
			}
		}, false, false);
		
		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				
				String firstValue = jsonObject.get("responseValue").isString().stringValue();
				JSONObject answer = JSONParser.parseStrict(firstValue).isObject();
				if (answer.containsKey("height")) {
					return (int) answer.get("height").isNumber().doubleValue();
				}
				return 0;
			}

			@Override
			public int getType() {
				return INTEGER_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return HEIGHT_FIELD;
			}
		}, false, false);
		
		
//		addDerivedField(new DerivedFieldTask() {
//			JSONObject jsonObject;
//			
//			@Override
//			public void setJsonSource(JSONObject jsonObject) {
//				this.jsonObject = jsonObject;	
//			}
//			
//			@Override
//			public Object process() {
//				return new String[] {"student role", "teacher role"};
//			}
//
//			@Override
//			public int getType() {
//				return ENUM_DATA_TYPE;
//			}
//
//			@Override
//			public String getTargetFieldName() {
//				return ROLE_VALUE_FIELD;
//			}
//		}, false, false);
		
		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				return Boolean.TRUE;
			}

			@Override
			public int getType() {
				return BOOLEAN_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return "read";
			}
		}, false, false);
		
		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				return Boolean.TRUE;
			}

			@Override
			public int getType() {
				return BOOLEAN_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return "correct";
			}
		}, false, false);
		
	}

//	@Override
//	protected void registerForNotifications() {
//		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.run.Response", new NotificationHandler() {
//
//			@Override
//			public void onNotification(JSONObject bean) {
//				processNotification(bean);
//			}
//		});
//	}
	
	protected String getNotificationType() {
		return "org.celstec.arlearn2.beans.run.Response";
	}
	
}
