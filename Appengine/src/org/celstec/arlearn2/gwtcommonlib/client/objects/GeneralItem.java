package org.celstec.arlearn2.gwtcommonlib.client.objects;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.Canvas;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;

import java.util.LinkedHashMap;

public abstract class GeneralItem extends Bean {
	public  final static String ID = "id";

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
		} else if (type.equals(AudioObject.TYPE)) {
			return new AudioObject(object);
		} else if (type.equals(MultipleChoiceTest.TYPE)) {
			return new MultipleChoiceTest(object);
		} else if (type.equals(SingleChoiceTest.TYPE)) {
			return new SingleChoiceTest(object);
		} else if (type.equals(ScanTagObject.TYPE)) {
			return new ScanTagObject(object);
		} else if (type.equals(MultipleChoiceImage.TYPE)) {
			return new MultipleChoiceImage(object);
		} else if (type.equals(SingleChoiceImage.TYPE)) {
			return new SingleChoiceImage(object);
		} else if (type.equals(MozillaOpenBadge.TYPE)) {
			return new MozillaOpenBadge(object);
		}
		return null;
	}

	public abstract Canvas getViewerComponent();

	public abstract Canvas getMetadataExtensionEditor();
	public abstract boolean enableDataCollection();
	
	public void writeToCloud(JsonCallback jsonCallback) {
		GeneralItemsClient.getInstance().createGeneralItem(this, jsonCallback);
	}


    public LinkedHashMap<String, String> getMetadataFields() {
        LinkedHashMap<String, String> sortList = new LinkedHashMap<String, String>();
        return sortList;
    }
}
