package org.celstec.arlearn.delegators;

import android.util.Log;
import daoBase.DaoConfiguration;
import de.greenrobot.dao.query.QueryBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.client.BadgesClient;
import org.celstec.dao.gen.*;
import org.celstec.events.BadgeEvent;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.InputStream;
import java.util.List;

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
public class BadgesDelegator {

    private static BadgesDelegator instance;

    private BadgesDelegator() {
        ARL.eventBus.register(this);
    }

    public static BadgesDelegator getInstance() {
        if (instance == null) {
            instance = new BadgesDelegator();
        }
        return instance;
    }

    public void syncBadges(int accountType, String accountLocalId) {
        ARL.eventBus.post(new SyncBadges(accountType, accountLocalId));
    }

    public void syncBadges() {
        AccountLocalObject account = ARL.accounts.getLoggedInAccount();
        if (account != null) {
            ARL.eventBus.post(new SyncBadges(account.getAccountType(), account.getLocalId()));
        }
    }


    private void onEventAsync(SyncBadges syncBadges) {
        String badges = BadgesClient.getBadgesClient().getUserBadges(syncBadges.getWespotId());

        AccountLocalObject account = createOrRetrieveAccount(syncBadges.getAccountType(), syncBadges.getAccountLocalId());
        if (account == null) return;
        JSONArray array = null;
        try {
            array = new JSONArray(badges);

            for (int i = 0; i< array.length(); i++) {
                JSONObject inqJsonObject = array.getJSONObject(i);
                BadgeLocalObject badge = new BadgeLocalObject();
                badge.setId(inqJsonObject.getLong("id"));
                if (inqJsonObject.has("context")) {
                    try {
                        long inqId = Long.parseLong(inqJsonObject.getString("context"));
                        badge.setInquiryId(inqId);
                    } catch (NumberFormatException e) {

                    }
                }

                if (inqJsonObject.has("jsonBadge")) {
                    JSONObject jsonBadge = inqJsonObject.getJSONObject("jsonBadge");
                    if (jsonBadge.has("badge")) {
                        JSONObject badgeJSON = jsonBadge.getJSONObject("badge");
                        if (badgeJSON.has("name")) {
                            badge.setTitle(badgeJSON.getString("name"));
                            badge.setDescription(badgeJSON.getString("description"));
                            badge.setBadgeIcon(downloadImage(badgeJSON.getString("image")));
                        }

                    }
                }

                badge.setAccountLocalObject(account);
                DaoConfiguration.getInstance().getSession().getBadgeLocalObjectDao().insertOrReplace(badge);
                ARL.eventBus.post(new BadgeEvent());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private byte[] downloadImage(String imageUrl){
        try {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(imageUrl);
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        int imageLength = (int)(entity.getContentLength());
        InputStream is = entity.getContent();

        byte[] imageBlob = new byte[imageLength];
        int bytesRead = 0;
        while (bytesRead < imageLength) {
            int n = is.read(imageBlob, bytesRead, imageLength - bytesRead);
            if (n <= 0)
                ; // do some error handling
            bytesRead += n;
        }
            return imageBlob;
        }catch (Exception e) {
            return null;
        }
    }


    public AccountLocalObject createOrRetrieveAccount(int accountType, String accountLocalId) {
        DaoConfiguration daoConfiguration= DaoConfiguration.getInstance();
        try {
        QueryBuilder<AccountLocalObject> qb =daoConfiguration.getSession().queryBuilder(AccountLocalObject.class);
        qb.where(AccountLocalObjectDao.Properties.Name.eq("Jose"));
        List<AccountLocalObject> accounts = qb.list();
        for (AccountLocalObject ac : accounts) {
            return ac;
        }
        }catch (NullPointerException e) {

        }

        AccountLocalObject account = new AccountLocalObject();
        account.setLocalId(accountLocalId);
        account.setAccountType(accountType);
        account.setName(accountLocalId);

        DaoConfiguration.getInstance().getAccountLocalObjectDao().insertOrReplace(account);
        return account;
    }

    private class SyncBadges{
        private int accountType;
        private String accountLocalId;

        private SyncBadges(int accountType, String accountLocalId) {
            this.accountType = accountType;
            this.accountLocalId = accountLocalId;
        }

        public int getAccountType() {
            return accountType;
        }

        public void setAccountType(int accountType) {
            this.accountType = accountType;
        }

        public String getAccountLocalId() {
            return accountLocalId;
        }

        public void setAccountLocalId(String accountLocalId) {
            this.accountLocalId = accountLocalId;
        }

        public String getWespotId() {
            String returnAccount = null;
            switch (accountType) {
                case 1:
                    returnAccount = "facebook_";
                    break;
                case 2:
                    returnAccount = "google_";
                    break;
                default:
                returnAccount = "_";
            }

            returnAccount+=accountLocalId;


            return returnAccount;
        }
    }
}
