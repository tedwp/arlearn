package org.celstec.arlearn2.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.client.exception.ARLearnException;
import org.codehaus.jettison.json.JSONObject;

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
public class BadgesClient extends GenericClient{

    public static BadgesClient instance;

    private BadgesClient() {
        super("");
    }

    public static BadgesClient getBadgesClient() {
        if (instance == null) {
            instance = new BadgesClient();
        }
        return instance;
    }

    public String getUserBadges(String account) {
        account = account.replace("2:", "google_");
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(getUrlPrefix().replace("***",account));
        request.setHeader("Accept", "application/json");
        request.setHeader("Authorization", "ufnhsj6ojdunf3ms4j807vrnks");
        try {
        HttpResponse response = httpClient.execute(request);
//        HttpResponse response = conn.executeGET(getUrlPrefix().replace("***",account), "test", "application/json");

            return EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            if (e instanceof ARLearnException) throw (ARLearnException) e;
        }

        return "error";
    }




    public String getUrlPrefix() {
//        return "http://ariadne.cs.kuleuven.be/wespot-dev-ws/rest/getBadges/";
        return "https://openbadgesapi.appspot.com/rest/badges/user/***/awarded";
    }
}