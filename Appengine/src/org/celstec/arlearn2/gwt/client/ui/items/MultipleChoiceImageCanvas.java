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
package org.celstec.arlearn2.gwt.client.ui.items;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class MultipleChoiceImageCanvas extends SingleChoiceImageCanvas{
	public static final String type = "org.celstec.arlearn2.beans.generalItem.MultipleChoiceImageTest";

	public MultipleChoiceImageCanvas(String[] roles) {
		super(roles);
	}

	public JSONObject generateObject() {
		JSONObject result = super.generateObject();
		result.put("type", new JSONString(type));
		return result;
	}

}
