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
package org.celstec.arlearn2.android.asynctasks;

import java.util.concurrent.CountDownLatch;

import org.celstec.arlearn2.android.asynctasks.network.NetworkTaskHandler;
import org.celstec.arlearn2.android.db.DBAdapter.DatabaseHandler;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

public class NetworkQueue {

	private static CountDownLatch startLatch;
	private static NetworkTaskHandler networkTaskHandler;

	private static Thread thread;
	
	

	public static NetworkTaskHandler getNetworkTaskHandler() {
		if (networkTaskHandler != null) return networkTaskHandler;
		getThread();
    	return getNetworkTaskHandler();
    }
	
	 public static NetworkTaskHandler getThread() {
			startLatch = new CountDownLatch(1);
	    	if (thread == null) {
	    		thread = new NeworkTaskThread();
	    		thread.start();
	    	} else {
	    		startLatch.countDown();
	    	}
	    	try {
				startLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	return networkTaskHandler;
	    	
	    }
	
	 static class NeworkTaskThread extends Thread {
		 @Override
			public void run() {
				try {
					Looper.prepare();
					networkTaskHandler = new NetworkTaskHandler();
					startLatch.countDown();
					Looper.loop();
				} catch (Throwable t) {
					Log.e("database", "network thread halted", t);
				}
			}
		 
	 }
}
