package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GeneralItemListSerializer extends BeanSerializer{

	@Override
	public JSONObject toJSON(Object bean) {
		GeneralItemList tl = (GeneralItemList) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (tl.getGeneralItems() != null) returnObject.put("generalItems", ListSerializer.toJSON(tl.getGeneralItems()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
