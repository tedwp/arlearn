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
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.RunBeanDeserializer;
import org.celstec.arlearn2.beans.game.Game;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RunDeserializer extends RunBeanDeserializer{
	
	private static final BeanDeserializer rcd = new BeanDeserializer () {

		@Override
		public RunConfig toBean(JSONObject object) {
			RunConfig bean = new RunConfig();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		@Override
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			RunConfig bean = (RunConfig) genericBean;
			if (object.has("selfRegistration")) bean.setSelfRegistration(object.getBoolean("selfRegistration"));	
			if (object.has("nfcTag")) bean.setNfcTag(object.getString("nfcTag"));	
		}
		
	};

	@Override
	public Run toBean(JSONObject object) {
		Run bean = new Run();
		try {
			initBean(object, bean);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void initBean(JSONObject object, Bean genericBean) throws JSONException {
		super.initBean(object, genericBean);
		Run bean = (Run) genericBean;
		if (object.has("gameId")) bean.setGameId(object.getLong("gameId"));	
		if (object.has("title")) bean.setTitle(object.getString("title"));	
		if (object.has("owner")) bean.setOwner(object.getString("owner"));
		if (object.has("tagId")) bean.setOwner(object.getString("tagId"));
		if (object.has("startTime")) bean.setStartTime(object.getLong("startTime"));
		if (object.has("serverCreationTime")) bean.setServerCreationTime(object.getLong("serverCreationTime"));
		if (object.has("lastModificationDate")) bean.setLastModificationDate(object.getLong("lastModificationDate"));
		if (object.has("game")) bean.setGame((Game) JsonBeanDeserializer.deserialize(Game.class, object.getJSONObject("game")));
		if (object.has("runConfig")) bean.setRunConfig((RunConfig) JsonBeanDeserializer.deserialize(RunConfig.class, object.getJSONObject("runConfig"), rcd));
		
	}
	
	
}
