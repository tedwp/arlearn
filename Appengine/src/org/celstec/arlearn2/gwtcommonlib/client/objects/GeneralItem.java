package org.celstec.arlearn2.gwtcommonlib.client.objects;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.widgets.Canvas;

public abstract class GeneralItem extends Bean {

	public GeneralItem(JSONObject json) {
		super(json);
	}
	
	public GeneralItem(){
		super();
	}

	public String getTitle() {
		if (jsonRep.containsKey(GeneralItemModel.NAME_FIELD)) {
			return jsonRep.get(GeneralItemModel.NAME_FIELD).isString().stringValue();
		}
		return "";
	}
	
	public String getRichText() {
		return getString(GeneralItemModel.RICH_TEXT_FIELD);
	}
	
	public void linkToGame(Game game) {
		jsonRep.put(GameModel.GAMEID_FIELD, new JSONNumber(game.getGameId()));
	}

	public String getHumanReadableName() {
		return "general Item";
	}

	public static GeneralItem createObject(JSONObject object) {
		String type = object.get("type").isString().stringValue();
		if (type.equals(VideoObject.TYPE)) {
			return new VideoObject(object);
		} else if (type.equals(NarratorItem.TYPE)) {
			return new NarratorItem(object);
		} else if (type.equals(YoutubeObject.TYPE)) {
			return new YoutubeObject(object);
		}
		return null;
	}

	public abstract Canvas getViewerComponent();

	public abstract Canvas getMetadataExtensionEditor();
	
}
