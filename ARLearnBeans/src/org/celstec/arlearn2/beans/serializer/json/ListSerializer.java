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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONArray;

public class ListSerializer {
	private static final Logger logger = Logger.getLogger(ListSerializer.class.getName());

	public static JSONArray toJSON(Object bean) {
		List list = (List) bean;
		JSONArray array = new JSONArray();
		for (Object b: list) {
			if (b == null) {
				logger.log(Level.SEVERE, "b is null for list "+bean.getClass());
			} else {
				array.put(JsonBeanSerialiser.serialiseToJson(b));	
			}
		}
		return array;
	}
	
	public static JSONArray toStringList(List<String> list) {
		JSONArray array = new JSONArray();
		for (String b: list) {
				array.put(b);
		}
		return array;
	}

}
