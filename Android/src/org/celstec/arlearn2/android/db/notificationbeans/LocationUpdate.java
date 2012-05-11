package org.celstec.arlearn2.android.db.notificationbeans;

public class LocationUpdate extends NotificationBean {
	
	private String user;
	private Double lat;
	private Double lng;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	
	public boolean requiresBroadcast() {
		//TODO set true
		return false;
	}
	
	
}
