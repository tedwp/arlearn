package org.celstec.arlearn2.gwtcommonlib.client.datasource;

public class UserModel extends DataSourceModel {

	public static final String EMAIL_FIELD = "email";
	public static final String FULL_EMAIL_FIELD = "fullEmail";
	public static final String NAME_FIELD = "name";
	public static final String ROLES_FIELD = "roles";
	
	public UserModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}

	@Override
	protected void initFields() {
		addField(STRING_DATA_TYPE, TeamModel.TEAMID_FIELD, false, true);
		addField(STRING_DATA_TYPE, EMAIL_FIELD, false, true);
		addField(STRING_DATA_TYPE, FULL_EMAIL_FIELD, false, true);
		addField(STRING_DATA_TYPE, NAME_FIELD, false, true);
		addField(INTEGER_DATA_TYPE, RunModel.RUNID_FIELD, true, true);
		addField(ENUM_DATA_TYPE, ROLES_FIELD, true, true);

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
