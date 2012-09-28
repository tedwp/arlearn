package org.celstec.arlearn2.beans.game;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MapRegion extends Bean {

	private double latUp;
	private double latDown;
	private double lngLeft;
	private double lngRight;
	
	private int minZoom;
	private int maxZoom;
	
	public MapRegion() {
		
	}

	public double getLatUp() {
		return latUp;
	}

	public void setLatUp(double latUp) {
		this.latUp = latUp;
	}

	public double getLatDown() {
		return latDown;
	}

	public void setLatDown(double latDown) {
		this.latDown = latDown;
	}

	public double getLngLeft() {
		return lngLeft;
	}

	public void setLngLeft(double lngLeft) {
		this.lngLeft = lngLeft;
	}

	public double getLngRight() {
		return lngRight;
	}

	public void setLngRight(double lngRight) {
		this.lngRight = lngRight;
	}

	public int getMinZoom() {
		return minZoom;
	}

	public void setMinZoom(int minZoom) {
		this.minZoom = minZoom;
	}

	public int getMaxZoom() {
		return maxZoom;
	}

	public void setMaxZoom(int maxZoom) {
		this.maxZoom = maxZoom;
	}
	
	@Override
	public boolean equals(Object obj) {
		MapRegion other = (MapRegion ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getLatUp(), other.getLatUp()) && 	
			nullSafeEquals(getLatDown(), other.getLatDown()) && 	
			nullSafeEquals(getLngLeft(), other.getLngLeft()) && 
			nullSafeEquals(getLngRight(), other.getLngRight()) && 
			nullSafeEquals(getMinZoom(), other.getMinZoom()) && 
			nullSafeEquals(getMaxZoom(), other.getMaxZoom()) ; 
	}
	
	public static class MapRegionDeserializer extends BeanDeserializer{

		@Override
		public MapRegion toBean(JSONObject object) {
			MapRegion bean = new MapRegion();
			try {
				initBean(object, bean);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return bean;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			MapRegion bean = (MapRegion) genericBean;
			if (object.has("latUp")) bean.setLatUp(object.getDouble("latUp"));
			if (object.has("latDown")) bean.setLatDown(object.getDouble("latDown"));
			if (object.has("lngLeft")) bean.setLngLeft(object.getDouble("lngLeft"));
			if (object.has("lngRight")) bean.setLngRight(object.getDouble("lngRight"));
			
			
			if (object.has("minZoom")) bean.setMinZoom(object.getInt("minZoom"));
			if (object.has("maxZoom")) bean.setMaxZoom(object.getInt("maxZoom"));
		}

	}
	
	public static class MapRegionSerializer extends BeanSerializer{

		
		@Override
		public JSONObject toJSON(Object bean) {
			MapRegion mapRegion = (MapRegion) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				returnObject.put("latUp", mapRegion.getLatUp());
				returnObject.put("latDown", mapRegion.getLatDown());
				returnObject.put("lngLeft", mapRegion.getLngLeft());
				returnObject.put("lngRight", mapRegion.getLngRight());

				returnObject.put("minZoom", mapRegion.getMinZoom());
				returnObject.put("maxZoom", mapRegion.getMaxZoom());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}

	}
}
