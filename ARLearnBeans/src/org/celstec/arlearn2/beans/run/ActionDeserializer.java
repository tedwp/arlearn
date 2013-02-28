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

public class ActionDeserializer extends RunBeanDeserializer{

	@Override
	public Action toBean(JSONObject object) {
		Action bean = new Action();
		try {
			initBean(object, bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		Action bean = (Action) genericBean;
		if (object.has("generalItemId")) bean.setGeneralItemId(object.getLong("generalItemId"));
		if (object.has("generalItemType")) bean.setGeneralItemType(object.getString("generalItemType"));
		if (object.has("userEmail")) bean.setUserEmail(object.getString("userEmail"));
		if (object.has("time")) bean.setTime(object.getLong("time"));
		if (object.has("action")) bean.setAction(object.getString("action"));

	}

}
