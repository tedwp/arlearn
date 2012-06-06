package org.celstec.arlearn2.beans.deserializer.json;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GeneralItemListDeserializer  extends BeanDeserializer{

	@Override
	public GeneralItemList toBean(JSONObject object) {
		GeneralItemList tl = new GeneralItemList();
		try {
			initBean(object, tl);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tl;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		GeneralItemList giList = (GeneralItemList) genericBean;
		if (object.has("generalItems")) giList.setGeneralItems(ListDeserializer.toBean(object.getJSONArray("generalItems"), GeneralItemList.class));
	}

}
