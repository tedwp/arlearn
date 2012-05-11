package org.celstec.arlearn2.android.db.notificationbeans;

import java.io.Serializable;

import android.content.Context;

public class NotificationBean implements Serializable{

	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean requiresBroadcast() {
		return false;
	}
	
	public void run(Context ctx) {
		
	}
	
}
