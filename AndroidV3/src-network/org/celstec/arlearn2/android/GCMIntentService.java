package org.celstec.arlearn2.android;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import org.celstec.arlearn2.android.gcm.handlers.GCMHandler;

import java.util.HashMap;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
public class GCMIntentService extends IntentService {


    static final String TAG = "GCMDemo";


    public GCMIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        Log.e(TAG, "messageType " + messageType);

        for (String key: intent.getExtras().keySet()) {
            Log.e(TAG, "key: " + key);
            if ("type".equals(key) || "gameId".equals(key))  Log.e(TAG, "value: " + intent.getExtras().getString(key));

        }

        HashMap map = new HashMap<String, String>();
        for (String key: intent.getExtras().keySet()) {

            if ("type".equals(key) || "gameId".equals(key)) map.put(key, intent.getExtras().getString(key));


        }

        GCMHandler handler = GCMHandler.createHandler(this, map);
        if (handler != null) handler.handle();

        Log.e(TAG, "Received: " + extras.toString());
        GCMWakefulReceiver.completeWakefulIntent(intent);

    }

}
