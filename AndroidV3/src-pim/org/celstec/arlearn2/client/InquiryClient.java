package org.celstec.arlearn2.client;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.account.Account;
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
public class InquiryClient extends GenericClient{

    public static InquiryClient instance;
    public static String url = "http://wespot.kmi.open.ac.uk/services/api/rest/json/?api_key=27936b77bcb9bb67df2965c6518f37a77a7ab9f8";

    private InquiryClient() {
        super("");
    }

    public static InquiryClient getInquiryClient() {
        if (instance == null) {
            instance = new InquiryClient();
        }
        return instance;
    }

    public String userInquiries() {
        HttpResponse response = conn.executeGET(getUrlPrefix(), null, "application/json");
        try {
            return EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            if (e instanceof ARLearnException) throw (ARLearnException) e;

        }
        return "error";
    }

    public long getArlearnRunId(long inquiryId) {
        HttpResponse response = conn.executeGET("http://wespot.kmi.open.ac.uk/services/api/rest/json/?api_key=27936b77bcb9bb67df2965c6518f37a77a7ab9f8&method=inquiry.arlearnrun&inquiryId="+inquiryId, null, "application/json");
        try {
//            return EntityUtils.toString(response.getEntity());
            JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
            return json.getLong("result");

        } catch (Exception e) {
            if (e instanceof ARLearnException) throw (ARLearnException) e;

        }
        return 0l;
    }

    public Hypothesis getInquiryHypothesis(long inquiryId) {
        HttpResponse response = conn.executeGET(url+"&method=inquiry.hypothesis&inquiryId="+inquiryId, null, "application/json");
        try {
//            return EntityUtils.toString(response.getEntity());
            JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
            if (json.has("result")) {
                org.codehaus.jettison.json.JSONArray resultJson = json.getJSONArray("result");
                if (resultJson.length()>0){
                    JSONObject hypoJson = resultJson.getJSONObject(0);
                    return new Hypothesis(hypoJson.getString("title"), hypoJson.getString("description"));
                }
            }
        } catch (Exception e) {
            if (e instanceof ARLearnException) throw (ARLearnException) e;

        }
        return null;
    }

    public class Hypothesis {
        private String title;
        private String description;

        public Hypothesis(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }


    public String getUrlPrefix() {
        return "http://wespot.kmi.open.ac.uk/services/api/rest/json/?api_key=27936b77bcb9bb67df2965c6518f37a77a7ab9f8&oauthId=116743449349920850150&oauthProvider=Google&method=user.inquiries";
    }
}