package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.generalItem.GeneralItemSerializer;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class NarratorItemSerializer extends GeneralItemSerializer {

	@Override
	public JSONObject toJSON(Object bean) {
		NarratorItem gi = (NarratorItem) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (gi.getVideoUrl() != null) returnObject.put("videoUrl", gi.getVideoUrl());
			if (gi.getAudioUrl() != null) returnObject.put("audioUrl", gi.getAudioUrl());
			if (gi.getImageUrl() != null) returnObject.put("imageUrl", gi.getImageUrl());
			if (gi.getText() != null) returnObject.put("text", gi.getText());
			if (gi.getRichText() != null) returnObject.put("richText", gi.getRichText());
			if (gi.getOpenQuestion() != null) returnObject.put("openQuestion", JsonBeanSerialiser.serialiseToJson(gi.getOpenQuestion()));			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
}
