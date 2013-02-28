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
import org.celstec.arlearn2.beans.deserializer.CustomDeserializer;
import org.celstec.arlearn2.beans.game.GameBean;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class OpenQuestionDeserializer extends BeanDeserializer{

	@Override
	public OpenQuestion toBean(JSONObject object) {
		OpenQuestion returnObject = new OpenQuestion();
		try {
			initBean(object, returnObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnObject;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		OpenQuestion oq = (OpenQuestion) genericBean;
		if (object.has("withAudio"))
			oq.setWithAudio(object.getBoolean("withAudio"));
		if (object.has("withText"))
			oq.setWithText(object.getBoolean("withText"));
		if (object.has("withPicture"))
			oq.setWithPicture(object.getBoolean("withPicture"));
		if (object.has("withVideo"))
			oq.setWithVideo(object.getBoolean("withVideo"));

		
	}
}
