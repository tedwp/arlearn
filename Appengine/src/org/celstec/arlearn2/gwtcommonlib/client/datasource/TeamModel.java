package org.celstec.arlearn2.gwtcommonlib.client.datasource;

public class TeamModel extends DataSourceModel {

	public static final String TEAMID_FIELD = "teamId";
	public static final String NAME_FIELD = "name";
	
	public TeamModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}

	@Override
	protected void initFields() {
		addField(STRING_DATA_TYPE, TEAMID_FIELD, false, true);
		addField(STRING_DATA_TYPE, NAME_FIELD, false, true);
	}

	@Override
	protected void registerForNotifications() {
	}

}
