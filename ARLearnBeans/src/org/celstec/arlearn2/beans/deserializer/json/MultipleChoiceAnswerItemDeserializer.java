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

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MultipleChoiceAnswerItemDeserializer extends BeanDeserializer{

	@Override
	public MultipleChoiceAnswerItem toBean(JSONObject object) {
		MultipleChoiceAnswerItem mcai = new MultipleChoiceAnswerItem();
		try {
			initBean(object, mcai);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mcai;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		MultipleChoiceAnswerItem mcai = (MultipleChoiceAnswerItem) genericBean;
		if (object.has("isCorrect")) mcai.setIsCorrect(object.getBoolean("isCorrect"));
		if (object.has("answer")) mcai.setAnswer(object.getString("answer"));
		if (object.has("nfcTag")) mcai.setNfcTag(object.getString("nfcTag"));
		if (object.has("id")) mcai.setId(object.getString("id"));
	}
}
