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
