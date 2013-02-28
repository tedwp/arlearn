/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
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
		if (object.has("gameId")) bean.setGameId(object.getLong("gameId"));
		if (object.has("itemId")) bean.setItemId(object.getLong("itemId"));
		if (object.has("generalItem")) bean.setGeneralItem((GeneralItem) JsonBeanDeserializer.deserialize(GeneralItem.class, object.getJSONObject("generalItem")));

	}
}

