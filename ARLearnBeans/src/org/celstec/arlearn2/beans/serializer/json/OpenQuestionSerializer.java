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

import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class OpenQuestionSerializer extends BeanSerializer{

	@Override
	public JSONObject toJSON(Object bean) {
		OpenQuestion openQuestion = (OpenQuestion) bean;
		JSONObject returnObject = super.toJSON(bean);
		try {
			returnObject.put("withAudio", openQuestion.isWithAudio());
			returnObject.put("withText", openQuestion.isWithText());
			returnObject.put("withPicture", openQuestion.isWithPicture());
			returnObject.put("withVideo", openQuestion.isWithVideo());
			returnObject.put("withValue", openQuestion.isWithValue());
			returnObject.put("textDescription", openQuestion.getTextDescription());
			returnObject.put("valueDescription", openQuestion.getValueDescription());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

}
