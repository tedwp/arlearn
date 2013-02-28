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
package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class SingleChoiceImageTest extends SingleChoiceTest {

	private String audioQuestion;
	
	public String getAudioQuestion() {
		return audioQuestion;
	}

	public void setAudioQuestion(String audioQuestion) {
		this.audioQuestion = audioQuestion;
	}

	public static GeneralItemDeserializer deserializer = new GeneralItemDeserializer(){

		@Override
		public SingleChoiceImageTest toBean(JSONObject object) {
			SingleChoiceImageTest mct = new SingleChoiceImageTest();
			try {
				initBean(object, mct);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return mct;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			SingleChoiceImageTest mctItem = (SingleChoiceImageTest) genericBean;
			if (object.has("richText")) mctItem.setRichText(object.getString("richText"));
			if (object.has("text")) mctItem.setText(object.getString("text"));
			if (object.has("audioQuestion")) mctItem.setAudioQuestion(object.getString("audioQuestion"));
			if (object.has("answers")) mctItem.setAnswers(ListDeserializer.toBean(object.getJSONArray("answers"), MultipleChoiceImageAnswerItem.class));
		}
	};

	public static GeneralItemSerializer serializer = new GeneralItemSerializer(){

		@Override
		public JSONObject toJSON(Object bean) {
			SingleChoiceImageTest mct = (SingleChoiceImageTest) bean;
			JSONObject returnObject = SingleChoiceTest.serializer.toJSON(bean);
			try {
				if (mct.getAudioQuestion() != null) returnObject.put("audioQuestion", mct.getAudioQuestion());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
		
	};
}