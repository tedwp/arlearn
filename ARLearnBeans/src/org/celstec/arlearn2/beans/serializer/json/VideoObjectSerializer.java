package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class VideoObjectSerializer extends NarratorItemSerializer {

	@Override
	public JSONObject toJSON(Object bean) {
		VideoObject gi = (VideoObject) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (gi.getVideoFeed() != null) returnObject.put("videoFeed", gi.getVideoFeed());			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
