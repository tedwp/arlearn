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

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

public class ListDeserializer {

	
	public static List toBean(JSONArray object, Class c) throws JSONException {
		ArrayList returnList = new ArrayList();
		for (int i = 0; i < object.length();i++) {
			returnList.add( JsonBeanDeserializer.deserialize(c, object.getJSONObject(i)));
			
		}
		return returnList;
	}
	
	public static List toStringList(JSONArray object) throws JSONException {
		ArrayList returnList = new ArrayList();
		for (int i = 0; i < object.length();i++) {
			returnList.add(object.getString(i));
		}
		return returnList;
	}

}
