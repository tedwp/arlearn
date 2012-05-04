package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class NarratorItemDeserializer extends GeneralItemDeserializer {
	
	@Override
	public NarratorItem toBean(JSONObject object) {
		NarratorItem gi = new NarratorItem();
		try {
			initBean(object, gi);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return gi;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		NarratorItem nItem = (NarratorItem) genericBean;
		if (object.has("videoUrl")) nItem.setVideoUrl(object.getString("videoUrl"));
		if (object.has("audioUrl")) nItem.setAudioUrl(object.getString("audioUrl"));
		if (object.has("imageUrl")) nItem.setImageUrl(object.getString("imageUrl"));
		if (object.has("text")) nItem.setText(object.getString("text"));
		if (object.has("richText")) nItem.setRichText(object.getString("richText"));
		if (object.has("openQuestion")) nItem.setOpenQuestion((OpenQuestion) JsonBeanDeserializer.deserialize(OpenQuestion.class, object.getJSONObject("openQuestion")));
	}
}
