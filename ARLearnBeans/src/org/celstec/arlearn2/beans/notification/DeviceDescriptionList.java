package org.celstec.arlearn2.beans.notification;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class DeviceDescriptionList extends Bean{

	private List<DeviceDescription> devices = new ArrayList<DeviceDescription>();

	public DeviceDescriptionList() {

	}
	
	public List<DeviceDescription> getDevices() {
		return devices;
	}

	public void setDevices(List<DeviceDescription> devices) {
		this.devices = devices;
	}
	
	public void addDevice(DeviceDescription device) {
		this.devices.add(device);
	}
	
	public void addDevices(List<DeviceDescription> devices) {
		this.devices.addAll(devices);
	}
	
	public static class Deserializer extends BeanDeserializer{

		@Override
		public DeviceDescriptionList toBean(JSONObject object) {
			DeviceDescriptionList ul = new DeviceDescriptionList();
			try {
				initBean(object, ul);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return ul;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			DeviceDescriptionList userlist = (DeviceDescriptionList) genericBean;
			if (object.has("devices")) userlist.setDevices(ListDeserializer.toBean(object.getJSONArray("devices"), DeviceDescription.class));
		}

	}
	public static class Serializer extends BeanSerializer{

		@Override
		public JSONObject toJSON(Object bean) {
			DeviceDescriptionList ul = (DeviceDescriptionList) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (ul.getDevices() != null) returnObject.put("devices", ListSerializer.toJSON(ul.getDevices()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}

	}
}
