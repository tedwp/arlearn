package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class AudioObjectDeserializer extends NarratorItemDeserializer {
	
	@Override
	public AudioObject toBean(JSONObject object) {
		AudioObject gi = new AudioObject();
		try {
			initBean(object, gi);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return gi;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		AudioObject nItem = (AudioObject) genericBean;
		if (object.has("audioFeed")) nItem.setAudioFeed(object.getString("audioFeed"));
	}

}
