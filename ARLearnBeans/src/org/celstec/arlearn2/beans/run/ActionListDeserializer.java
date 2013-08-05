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
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ActionListDeserializer extends RunBeanDeserializer{

	@Override
	public ActionList toBean(JSONObject object) {
		ActionList al = new ActionList();
		try {
			initBean(object, al);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return al;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		ActionList actionList = (ActionList) genericBean;
		if (object.has("actions")) actionList.setActions(ListDeserializer.toBean(object.getJSONArray("actions"), Action.class));
        if (object.has("serverTime")) actionList.setServerTime(object.getLong("serverTime"));
        if (object.has("resumptionToken")) actionList.setResumptionToken(object.getString("resumptionToken"));
	}

}
