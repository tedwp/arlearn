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
package org.celstec.arlearn2.android.asynctasks.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NetworkTaskHandler extends Handler {
	public static final int SYNC_GENERALITEMS = 1;
    public static final int SYNC_ACTIONS = 2;
    public static final int PUBLISH_ACTION = 3;
    public static final int SYNC_GAMES = 4;
    public static final int SYNC_RUNS = 5;
    public static final int SYNC_GI_MEDIA = 6;
    public static final int SYNC_USER_MEDIA = 7;
    public static final int GAME_CREATE = 8;
    public static final int GAME_DELETE = 9;
    public static final int SYNC_PARTICIPATING_GAME = 10;
    public static final int SYNC_UPLOAD_MEDIA = 11;
    public static final int GENERALITEM_CREATE = 12;
    public static final int GENERALITEM_DELETE = 13;
    public static final int GENERALITEM_QUERY = 14;



	@Override
	public void handleMessage(Message message) {
		Log.i("NWTask", "start "+System.currentTimeMillis());
		NetworkTask task = (NetworkTask) message.obj;
		long time = System.currentTimeMillis();
		try {
			task.execute();
		} catch (Exception e) {
			Log.e("exception", "in databasehandler", e);
		}
		Log.i("NWTask", "end "+task.getClass().getCanonicalName()+" "+ (System.currentTimeMillis()-time));
	}

}
