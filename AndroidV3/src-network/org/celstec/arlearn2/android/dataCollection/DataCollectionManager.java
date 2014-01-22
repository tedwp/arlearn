package org.celstec.arlearn2.android.dataCollection;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import daoBase.DaoConfiguration;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.dao.gen.AccountLocalObject;
import org.celstec.dao.gen.GeneralItemLocalObject;
import org.celstec.dao.gen.ResponseLocalObject;

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
public  abstract class DataCollectionManager {
    public static final int PICTURE_RESULT = 1;
    public static final int AUDIO_RESULT = 2;
    public static final int VIDEO_RESULT = 3;
    public static final int TEXT_RESULT = 4;

    protected Activity ctx;
    protected ResponseLocalObject response;

    protected DataCollectionManager(Activity ctx) {
        this.ctx = ctx;
        response = new ResponseLocalObject();
    }

    public abstract void takeDataSample();

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    public void setRunId(long runId){
        response.setRunId(runId);
    }

    public void setGeneralItem(long giId) {
        response.setGeneralItem(giId);
    }

    public void setGeneralItem(GeneralItemLocalObject giLocalObject) {
        response.setGeneralItemLocalObject(giLocalObject);
    }

    protected void saveResponseForSyncing() {
        response.setTimeStamp(ARL.time.getServerTime());
        response.setAccountLocalObject(ARL.accounts.getLoggedInAccount());
        response.setIsSynchronized(false);
        response.setNextSynchronisationTime(0l);
        setLocationDetails();
        DaoConfiguration.getInstance().getResponseLocalObjectDao().insertOrReplace(response);
    }

    private void setLocationDetails() {
        LocationManager locationManager = (LocationManager) ctx.getSystemService(ctx.LOCATION_SERVICE);

        String locationProviderNetwork = LocationManager.NETWORK_PROVIDER;
        String locationProviderGPS = LocationManager.GPS_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProviderGPS);
        if (lastKnownLocation == null) {
            lastKnownLocation =locationManager.getLastKnownLocation(locationProviderNetwork);
        }
        if (lastKnownLocation != null) {
            response.setLat(lastKnownLocation.getLatitude());
            response.setLng(lastKnownLocation.getLongitude());
        }
    }
}
