package org.celstec.arlearn2.gwtcommonlib.client.datasource;

public class UserModel extends DataSourceModel {

	public static final String EMAIL_FIELD = "email";
	public static final String FULL_EMAIL_FIELD = "fullEmail";
	public static final String NAME_FIELD = "name";
	
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

	}

	@Override
	protected void registerForNotifications() {
	}

}
