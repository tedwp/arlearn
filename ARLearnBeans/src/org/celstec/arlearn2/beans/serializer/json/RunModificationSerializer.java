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

import org.celstec.arlearn2.beans.notification.RunModification;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RunModificationSerializer extends BeanSerializer {

	@Override
	public JSONObject toJSON(Object bean) {
		RunModification runBean = (RunModification) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (runBean.getModificationType() != null) returnObject.put("modificationType", runBean.getModificationType());
			if (runBean.getRun() != null) returnObject.put("run", JsonBeanSerialiser.serialiseToJson(runBean.getRun()));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
