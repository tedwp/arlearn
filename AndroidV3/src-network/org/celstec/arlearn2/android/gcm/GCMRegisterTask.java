package org.celstec.arlearn2.android.gcm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.beans.notification.GCMDeviceDescription;
import org.celstec.arlearn2.client.NotificationClient;

import java.io.IOException;

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
public class GCMRegisterTask extends AsyncTask<Activity, Long, Void> {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static final String TAG = "GCM";
    public static final String SENDER_ID = "594104153413";

    @Override
    protected Void doInBackground(Activity... c) {
        if (checkPlayServices(c[0])) {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(c[0]);
            try {
                String registrationId = ARL.properties.getGCMKey();
                if (registrationId == null) {
                    registrationId = gcm.register(SENDER_ID);
                    ARL.properties.storeGCMKey(registrationId);
                }

                Log.e(TAG, "deviceId " + getDeviceId());
                Log.e(TAG, "regid "+registrationId);
                sendRegistrationToBackend(c[0], getDeviceId(), registrationId);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(),e);
            }
        }
        return null;
    }

    private void sendRegistrationToBackend(Context context, String deviceId, String registrationId) {
        GCMDeviceDescription desc = new GCMDeviceDescription();
        desc.setAccount(PropertiesAdapter.getInstance(context).getFullId());
        desc.setDeviceUniqueIdentifier(deviceId);
        desc.setRegistrationId(registrationId);
        NotificationClient.getOauthClient().gcm(PropertiesAdapter.getInstance(context).getAuthToken(), desc);
    }

    private boolean checkPlayServices(Activity ctx) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(ctx);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                try {
//                    GooglePlayServicesUtil.getErrorDialog(resultCode, ctx,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } catch (NoClassDefFoundError e) {
                    Log.i(TAG, "This device is not supported.",e);
                }
            } else {
                Log.i(TAG, "This device is not supported.");

            }
            return false;
        }
        return true;
    }

    private String getDeviceId () {
        return "35" +
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                Build.USER.length()%10 ; //13 digits

    }

    @Override
    protected void onProgressUpdate(Long... delta) {

    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

}
