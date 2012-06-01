package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class VideoObjectDeserializer extends NarratorItemDeserializer {
	
	@Override
	public VideoObject toBean(JSONObject object) {
		VideoObject gi = new VideoObject();
		try {
			initBean(object, gi);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return gi;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		VideoObject nItem = (VideoObject) genericBean;
		if (object.has("videoFeed")) nItem.setVideoFeed(object.getString("videoFeed"));
	}


}
