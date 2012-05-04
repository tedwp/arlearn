package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.deserializer.CustomDeserializer;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class OpenQuestionDeserializer extends CustomDeserializer {

	@Override
	public OpenQuestion toBean(JSONObject object) {
		OpenQuestion returnObject = new OpenQuestion();
		try {
			if (object.has("withAudio"))
				returnObject.setWithAudio(object.getBoolean("withAudio"));
			if (object.has("withPicture"))
				returnObject.setWithPicture(object.getBoolean("withPicture"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
}
