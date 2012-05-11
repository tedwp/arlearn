package org.celstec.arlearn2.android.sync;

import org.celstec.arlearn2.android.db.PropertiesAdapter;

import android.content.Context;

public abstract class GenericSyncroniser implements Runnable{
	
	protected final Context ctx;
	PropertiesAdapter pa;
	
	// value in seconds, so initially resync happens every 4 seconds
	public int delay = 4;

	
	public GenericSyncroniser(Context ctx) {
		this.ctx = ctx;
		pa = new PropertiesAdapter(ctx);
	}
	
	public void run() {
		if (pa.isAuthenticated()) runAuthenticated();
	}
	
	protected abstract void runAuthenticated();
	
	public boolean timeToExecute(long time) {
		return (time%delay)==0;
	}
	
	public void resetDelay() {
		delay = 4;
	}
	
	protected void setDelay(int seconds) {
		delay = seconds;
	}
	
	public void increaseDelay() {
		if (delay < 60) {
			delay *= 2;
		}
	}

}
