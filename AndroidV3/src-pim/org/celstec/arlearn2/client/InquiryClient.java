package org.celstec.arlearn2.client;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn.delegators.INQ;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.client.exception.ARLearnException;
import org.celstec.dao.gen.AccountLocalObject;
import org.celstec.dao.gen.InquiryLocalObject;
import org.codehaus.jettison.json.JSONObject;

import java.net.URLEncoder;

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

    public final static int CLOSED_MEMBERSHIP=0;
    public final static int OPEN_MEMBERSHIP=2;

    public final static int VIS_INQUIRY_MEMBERS_ONLY=0;
    public final static int VIS_LOGGED_IN_USERS=1;
    public final static int VIS_PUBLIC=2;
//    public static String url = "http://dev.inquiry.wespot.net/services/api/rest/json/?";

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
        if (INQ.accounts.getLoggedInAccount() == null) {
            return null;
        }
        String url = getUrlPrefix();
        url += "&api_key="+INQ.config.getProperty("elgg_api_key")+"&oauthId="+INQ.accounts.getLoggedInAccount().getLocalId()+
                "&oauthProvider="+providerIdToElggName(INQ.accounts.getLoggedInAccount().getAccountType())+"&method=user.inquiries";
        HttpResponse response = conn.executeGET(url, null, "application/json");
        try {
            return EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            if (e instanceof ARLearnException) throw (ARLearnException) e;

        }
        return "error";
    }

    public long getArlearnRunId(long inquiryId) {
        String url = getUrlPrefix();
        url+= "&api_key="+INQ.config.getProperty("elgg_api_key")+"&method=inquiry.arlearnrun&inquiryId="+inquiryId;
        HttpResponse response = conn.executeGET(url, null, "application/json");
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
        HttpResponse response = conn.executeGET(getUrlPrefix()+"&api_key="+INQ.config.getProperty("elgg_api_key")+
                "&method=inquiry.hypothesis&inquiryId="+inquiryId, null, "application/json");
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

    public void createInquiry(InquiryLocalObject inquiry, AccountLocalObject account, int visibility, int membership) {
        String provider = providerIdToElggName(account.getAccountType());
        try {
        String postBody = "method=inquiry.create" +
                "&name=" + URLEncoder.encode(inquiry.getTitle(), "UTF8") +
                "&description=" + URLEncoder.encode(inquiry.getDescription(), "UTF8") +
                "&interests=" + URLEncoder.encode("pim interests dummy value", "UTF8") +
                "&membership=" + membership +
                "&vis=" + visibility +
                "&wespot_arlearn_enable=no" +
                "&group_multiple_admin_allow_enable=no" +
                "&provider=" + provider +
                "&user_uid=" + provider + "_" + account.getLocalId() +
                "&api_key="+INQ.config.getProperty("elgg_api_key");

            HttpResponse response = conn.executePOST(getUrlPrefix()
                    , null, "application/json", postBody, "application/x-www-form-urlencoded");
            JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
            Log.e("ARLearn", "return after creating inquiry " + json.toString());
        } catch (Exception e) {
            if (e instanceof ARLearnException) throw (ARLearnException) e;

        }
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
        return INQ.config.getProperty("wespot_server");
    }

    private String providerIdToElggName(int id){
        switch (id){
            case 1:
                return  "Facebook";
            case 2:
                return  "Google";
            case 3:
                return  "LinkedId";
            case 4:
                return  "Twitter";
            case 5:
                return  "weSPOT";
        }
        return "idNotMappedToProviderName";
    }
}
