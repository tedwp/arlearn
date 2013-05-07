package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import com.google.gwt.json.client.JSONObject;

public class UserModel extends DataSourceModel {

	public static final String EMAIL_FIELD = "email";
	public static final String FULL_EMAIL_FIELD = "fullEmail";
	public static final String NAME_FIELD = "name";
	public static final String ROLES_FIELD = "roles";
	public static final String PICTURE_FIELD = "picture";
	
	public UserModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}

	@Override
	protected void initFields() {
		addField(STRING_DATA_TYPE, TeamModel.TEAMID_FIELD, false, true);
		addField(STRING_DATA_TYPE, EMAIL_FIELD, false, true);
		addField(STRING_DATA_TYPE, FULL_EMAIL_FIELD, false, true);
		addField(STRING_DATA_TYPE, NAME_FIELD, false, true);
		addField(STRING_DATA_TYPE, PICTURE_FIELD, false, true);
		addField(INTEGER_DATA_TYPE, RunModel.RUNID_FIELD, false, true);
		addField(ENUM_DATA_TYPE, ROLES_FIELD, false, true);
		
		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				long runId = (long) jsonObject.get(RunModel.RUNID_FIELD).isNumber().doubleValue();
				String teamId = "";
				if (jsonObject.containsKey(TeamModel.TEAMID_FIELD)) teamId = jsonObject.get(TeamModel.TEAMID_FIELD).isString().stringValue();
				String account = jsonObject.get(EMAIL_FIELD).isString().stringValue();
				
				return runId+":"+teamId+"account";
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return "pkField";
			}
		}, true, false);

	}

	@Override
	protected void registerForNotifications() {
	}

	public Object getPrimaryKey(AbstractRecord record) {
		long runId = (long) record.getCorrespondingJsonObject().get(RunModel.RUNID_FIELD).isNumber().doubleValue();
		String userEmail = record.getCorrespondingJsonObject().get(FULL_EMAIL_FIELD).isString().stringValue();
		return createKey(runId, userEmail);
	}
	
	public static String createKey(long runId, String userEmail) {
		return runId+":"+userEmail;
	}
}
