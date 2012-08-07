package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.CustomDeserializer;
import org.celstec.arlearn2.beans.game.GameBean;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class OpenQuestionDeserializer extends BeanDeserializer{

	@Override
	public OpenQuestion toBean(JSONObject object) {
		OpenQuestion returnObject = new OpenQuestion();
		try {
			initBean(object, returnObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		OpenQuestion oq = (OpenQuestion) genericBean;
		if (object.has("withAudio"))
			oq.setWithAudio(object.getBoolean("withAudio"));
		if (object.has("withPicture"))
			oq.setWithPicture(object.getBoolean("withPicture"));
		if (object.has("withVideo"))
			oq.setWithVideo(object.getBoolean("withVideo"));

		
	}
}
