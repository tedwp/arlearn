package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

public class ResponseModel extends DataSourceModel {

	
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
		
		addField(INTEGER_DATA_TYPE, "timestamp", false, true);
		addField(INTEGER_DATA_TYPE, "runId", true, true);
		addField(BOOLEAN_DATA_TYPE, "deleted", false, true);
		addField(INTEGER_DATA_TYPE, "generalItemId", true, true);
		addField(STRING_DATA_TYPE, "userEmail", false, true);
		addField(STRING_DATA_TYPE, "responseValue", false, true);
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
				}
				return "";
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
				if (answer.containsKey("imageUrl")) {
					return  answer.get("imageUrl").isString().stringValue();
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
				if (answer.containsKey("imageUrl")) {
					return  answer.get("imageUrl").isString().stringValue();
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
				if (answer.containsKey("imageUrl")) {
					return  answer.get("imageUrl").isString().stringValue();
				}
				return "";
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return "document";
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
				return "Blue";
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return "team";
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
				return "student role";
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return "role";
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

	@Override
	protected void registerForNotifications() {
	}
	
}
