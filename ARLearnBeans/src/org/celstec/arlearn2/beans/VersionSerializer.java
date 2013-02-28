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
package org.celstec.arlearn2.beans;

import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class VersionSerializer extends BeanSerializer {

	@Override
	public JSONObject toJSON(Object bean) {
		Version versionBean = (Version) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			if (versionBean.getVersionCode() != null) returnObject.put("versionCode", versionBean.getVersionCode());
			if (versionBean.getServiceUrl() != null) returnObject.put("serviceUrl", versionBean.getServiceUrl());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
}
