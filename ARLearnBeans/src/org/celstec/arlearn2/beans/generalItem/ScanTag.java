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
import org.celstec.arlearn2.beans.deserializer.json.NarratorItemDeserializer;
import org.celstec.arlearn2.beans.serializer.json.NarratorItemSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ScanTag extends NarratorItem {
	
	public Boolean autoLaunchQrReader;

	public Boolean getAutoLaunchQrReader() {
		return autoLaunchQrReader;
	}

	public void setAutoLaunchQrReader(Boolean autoLaunchQrReader) {
		this.autoLaunchQrReader = autoLaunchQrReader;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		ScanTag other = (ScanTag ) obj;
		return true; 
	}

	public static NarratorItemSerializer serializer = new NarratorItemSerializer(){

		@Override
		public JSONObject toJSON(Object bean) {
			ScanTag ou = (ScanTag) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (ou.getAutoLaunchQrReader() != null) returnObject.put("autoLaunchQrReader", ou.getAutoLaunchQrReader());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};
	
	public static NarratorItemDeserializer deserializer = new NarratorItemDeserializer(){
		
		@Override
		public ScanTag toBean(JSONObject object) {
			ScanTag gi = new ScanTag();
			try {
				initBean(object, gi);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return gi;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			ScanTag gi = (ScanTag) genericBean;
			if (object.has("autoLaunchQrReader")) gi.setAutoLaunchQrReader(object.getBoolean("autoLaunchQrReader"));

		}

	};
}
