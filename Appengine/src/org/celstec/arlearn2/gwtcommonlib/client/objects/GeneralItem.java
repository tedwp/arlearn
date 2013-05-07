package org.celstec.arlearn2.gwtcommonlib.client.objects;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class GeneralItem extends Bean {

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
	
}
