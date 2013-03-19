package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import com.google.gwt.json.client.JSONObject;

public class GameModel extends DataSourceModel {

	
	public GameModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}

	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, "gameId", true, true);
		addField(STRING_DATA_TYPE, "title", false, true);
		addField(INTEGER_DATA_TYPE, "time", false, true);
		addField(BOOLEAN_DATA_TYPE, "deleted", false, true);
	}

	@Override
	protected void registerForNotifications() {
		
	}

	@Override
	protected AbstractRecord createRecord(JSONObject object) {
		AbstractRecord record = super.createRecord(object);
		record.setAttribute("mapAvailable", true);
		if (object.containsKey("config")) {
			JSONObject config = object.get("config").isObject();
			if (config != null && config.containsKey("mapAvailable")&& config.get("mapAvailable").isBoolean() !=null) {
				record.setAttribute("mapAvailable", config.get("mapAvailable").isBoolean().booleanValue());
			}
		}
		return record;
	}
}
