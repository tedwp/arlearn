package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationSubscriber;

import com.google.gwt.json.client.JSONObject;

public class GameModel extends DataSourceModel {

	public static final String GAMEID_FIELD = "gameId";
	public static final String GAME_TITLE_FIELD = "title";
	public static final String TIME_FIELD = "time";
	public static final String DELETED_FIELD = "deleted";

	public GameModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}

	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, GAMEID_FIELD, true, true);
		addField(STRING_DATA_TYPE, GAME_TITLE_FIELD, false, true);
		addField(INTEGER_DATA_TYPE, TIME_FIELD, false, true);
		addField(BOOLEAN_DATA_TYPE, DELETED_FIELD, false, true);
	}

	@Override
	protected void registerForNotifications() {
		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.notification.GameModification", new NotificationHandler() {

			@Override
			public void onNotification(JSONObject bean) {
				processNotification(bean);
			}
		});
	}

	@Override
	protected AbstractRecord createRecord(JSONObject object) {
		AbstractRecord record = super.createRecord(object);
		record.setAttribute("mapAvailable", true);
		if (object.containsKey("config")) {
			JSONObject config = object.get("config").isObject();
			if (config != null && config.containsKey("mapAvailable") && config.get("mapAvailable").isBoolean() != null) {
				record.setAttribute("mapAvailable", config.get("mapAvailable").isBoolean().booleanValue());
			}
		}
		return record;
	}
}
