package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GeneralItemModificationDeserializer extends BeanDeserializer{

	@Override
	public GeneralItemModification toBean(JSONObject object) {
		GeneralItemModification bean = new GeneralItemModification();
		try {
			initBean(object, bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		GeneralItemModification bean = (GeneralItemModification) genericBean;
		if (object.has("modificationType")) bean.setModificationType(object.getInt("modificationType"));
		if (object.has("runId")) bean.setRunId(object.getLong("runId"));
		if (object.has("generalItem")) bean.setGeneralItem((GeneralItem) JsonBeanDeserializer.deserialize(GeneralItem.class, object.getJSONObject("generalItem")));

	}
}

