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
package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.RunBeanDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ResponseDeserializer extends RunBeanDeserializer{

	@Override
	public Response toBean(JSONObject object) {
		Response bean = new Response();
		try {
			initBean(object, bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		Response bean = (Response) genericBean;
		if (object.has("generalItemId")) bean.setGeneralItemId(object.getLong("generalItemId"));
		if (object.has("userEmail")) bean.setUserEmail(object.getString("userEmail"));
		if (object.has("responseValue")) bean.setResponseValue(object.getString("responseValue"));
		if (object.has("responseItemId")) bean.setResponseItemId(object.getString("responseItemId"));
		if (object.has("timestamp")) bean.setTimestamp(object.getLong("timestamp"));
		if (object.has("revoked")) bean.setRevoked(object.getBoolean("revoked"));

	}

}
