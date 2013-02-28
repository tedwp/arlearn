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
package org.celstec.arlearn2.beans.serializer.json;

import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GeneralItemModificationSerializer extends BeanSerializer {

	@Override
	public JSONObject toJSON(Object bean) {
		GeneralItemModification runBean = (GeneralItemModification) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (runBean.getModificationType() != null) returnObject.put("modificationType", runBean.getModificationType());
			if (runBean.getRunId() != null) returnObject.put("runId", runBean.getRunId());
			if (runBean.getGameId() != null) returnObject.put("gameId", runBean.getGameId());
			if (runBean.getItemId() != null) returnObject.put("itemId", runBean.getItemId());
			if (runBean.getGeneralItem() != null) returnObject.put("generalItem", JsonBeanSerialiser.serialiseToJson(runBean.getGeneralItem()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}

