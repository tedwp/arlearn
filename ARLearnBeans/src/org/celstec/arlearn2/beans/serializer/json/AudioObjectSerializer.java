package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class AudioObjectSerializer extends NarratorItemSerializer {

	@Override
	public JSONObject toJSON(Object bean) {
		AudioObject gi = (AudioObject) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (gi.getAudioFeed() != null) returnObject.put("audioFeed", gi.getAudioFeed());			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
