package org.celstec.arlearn2.beans.dependencies;

import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ProximityDependency extends Dependency {

	private Double lng;
	private Double lat;
	private Integer radius;
	
	public ProximityDependency() {
		setType(ProximityDependency.class.getName());
	}
	
	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	@Override
	public long satisfiedAt(List<Action> actionList) {
		long minSatisfiedAt = Long.MAX_VALUE;
		String latString = ""+((double)(long)(lat*1000000)/1000000);
		String lngString = ""+((double)(long)(lng*1000000)/1000000);
		String compString ="geo:"+latString+":"+lngString+":"+radius;
		for (Iterator iterator = actionList.iterator(); iterator.hasNext();) {
			Action action = (Action) iterator.next();
			if ((compString).equals(action.getAction())){
				minSatisfiedAt = Math.min(minSatisfiedAt, (action.getTime()==null)?0:action.getTime());
			}
		}
		if (minSatisfiedAt == Long.MAX_VALUE) minSatisfiedAt = -1;
		return minSatisfiedAt;
	}

	@Override
	public boolean equals(Object obj) {
		ProximityDependency other = (ProximityDependency ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getLng(), other.getLng()) &&
			nullSafeEquals(getLat(), other.getLat()) &&
			nullSafeEquals(getRadius(), other.getRadius()) ; 

	}
	
	public static class Deserializer extends BeanDeserializer{

		@Override
		public ProximityDependency toBean(JSONObject object) {
			ProximityDependency bean = new ProximityDependency();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			ProximityDependency bean = (ProximityDependency) genericBean;
			if (object.has("lng")) bean.setLng(object.getDouble("lng"));
			if (object.has("lat")) bean.setLat(object.getDouble("lat"));
			if (object.has("radius")) bean.setRadius(object.getInt("radius"));
		}
	}
	
	public static class Serializer extends BeanSerializer{
		@Override
		public JSONObject toJSON(Object bean) {
			ProximityDependency dep = (ProximityDependency) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (dep.getLng() != null) returnObject.put("lng", dep.getLng());
				if (dep.getLat() != null) returnObject.put("lat", dep.getLat());
				if (dep.getRadius() != null) returnObject.put("radius", dep.getRadius());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}

	}
}
