package org.celstec.arlearn2.gwtcommonlib.client.datasource;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.i18.DataSourceConstants;
import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationSubscriber;
import org.celstec.arlearn2.portal.client.author.ui.game.i18.GameConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

public class GameModel extends DataSourceModel {

	public static final String GAMEID_FIELD = "gameId";
	public static final String GAME_ACCESS = "accessRights";
	public static final String GAME_ACCESS_STRING = "accessRightsString";
	public static final String GAME_TITLE_FIELD = "title";
	public static final String GAME_DESCRIPTION_FIELD = "description";
	public static final String TIME_FIELD = "time";
	public static final String DELETED_FIELD = "deleted";
	public static final String SHARING_FIELD = "sharing";
	public static final String LICENSE_CODE = "licenseCode";
	public static final String MAP_AVAILABLE = "mapAvailable";
	public static final String MAP_ICON = "mapIcon";
	
	private DataSourceConstants constants = GWT.create(DataSourceConstants.class);


	public GameModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}

	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, GAMEID_FIELD, true, true);
		addField(INTEGER_DATA_TYPE, GAME_ACCESS, false, true);
		addField(STRING_DATA_TYPE, GAME_TITLE_FIELD, false, true);
		addField(STRING_DATA_TYPE, LICENSE_CODE, false, true);
		addField(INTEGER_DATA_TYPE, TIME_FIELD, false, true);
//		addField(BOOLEAN_DATA_TYPE, DELETED_FIELD, false, true);
		addDerivedField(new DerivedFieldTask() {
			JSONObject jsonObject;
			
			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;	
			}
			
			@Override
			public Object process() {
				if (jsonObject.containsKey("config")) {
					if (jsonObject.get("config").isObject().get(MAP_AVAILABLE).isBoolean().booleanValue()) {
						return "icon_maps";
					}
				}
				return "list_icon";
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return MAP_ICON;
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
				if (jsonObject.containsKey("config")) {
					return jsonObject.get("config").isObject().get(MAP_AVAILABLE).isBoolean().booleanValue();
				}
				return false;
			}

			@Override
			public int getType() {
				return BOOLEAN_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return MAP_AVAILABLE;
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
				if (jsonObject.containsKey(DELETED_FIELD)) {
					return jsonObject.get(DELETED_FIELD).isBoolean().booleanValue();
				}
				return false;
			}

			@Override
			public int getType() {
				return BOOLEAN_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return DELETED_FIELD;
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
				if (!jsonObject.containsKey(GAME_ACCESS)) return "";
				switch ((int)jsonObject.get(GAME_ACCESS).isNumber().doubleValue()) {
				case 1:
					return constants.owner();
				case 2:
					return constants.write();
				case 3:
					return constants.read();

				default:
					break;
				}
				return "";
			}

			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public String getTargetFieldName() {
				return GAME_ACCESS_STRING;
			}
		}, false, false);
	}
	
	protected String getNotificationType() {
		return "org.celstec.arlearn2.beans.game.Game";
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
