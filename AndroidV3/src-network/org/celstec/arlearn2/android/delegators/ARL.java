package org.celstec.arlearn2.android.delegators;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import daoBase.DaoConfiguration;
import de.greenrobot.event.EventBus;
import org.celstec.arlearn2.android.db.ConfigAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.gcm.GCMRegisterTask;
import org.celstec.arlearn2.android.gcm.GCMRegistration;
import org.celstec.arlearn2.client.GenericClient;
import org.celstec.dao.gen.AccountLocalObject;

import java.util.Properties;

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
public class ARL {

    public static GameDelegator games;
    public static GeneralItemDelegator generalItems;
    public static GiFileReferenceDelegator fileReferences;
    public static RunDelegator runs;
    public static AccountDelegator accounts;
    public static ResponseDelegator responses;
    public static PropertiesAdapter properties;
    public static Properties config;
    public static TimeDelegator time;
    public static StoreDelegator store;
    public static ThreadsDelegator threads;
    public static MessagesDelegator messages;
    public static EventBus eventBus = new EventBus();
    public static DaoConfiguration dao;
    private static Context ctx;

    public static void init(Context ctx) {
        ARL.ctx = ctx;
        dao= DaoConfiguration.getInstance(ctx);

        properties = PropertiesAdapter.getInstance(ctx);
        config = new ConfigAdapter(ctx).getProperties();
        GenericClient.urlPrefix = config.getProperty("arlearn_server");
        games = GameDelegator.getInstance();
        generalItems = GeneralItemDelegator.getInstance();
        runs = RunDelegator.getInstance();
        accounts = AccountDelegator.getInstance();
        time = TimeDelegator.getInstance(ctx);
        fileReferences = GiFileReferenceDelegator.getInstance();
        responses = ResponseDelegator.getInstance();
        store = StoreDelegator.getInstance();
        threads = ThreadsDelegator.getInstance();
        messages = MessagesDelegator.getInstance();

        new GCMRegisterTask().execute((Activity) ctx);

    }

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static Context getContext(){
        return ctx;
    }


}
