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
package org.celstec.arlearn2.android.db;

import android.accounts.AccountManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import org.celstec.arlearn2.android.delegators.AccountDelegator;
import org.celstec.arlearn2.beans.account.Account;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PropertiesAdapter {

    private Context context;
    private static PropertiesAdapter pa;

    public PropertiesAdapter(Context context) {
        if (context == null) throw new NullPointerException("context passed to a properties adapter cannot be null");
        this.context = context;
    }

    public static PropertiesAdapter getInstance(Context context) {
        if (pa == null)  pa = new PropertiesAdapter(context);
        return pa;
    }

    public static PropertiesAdapter getInstance() {
        return pa;
    }


    private SharedPreferences getDefaultPrefs() {
        return context.getSharedPreferences(Constants.SHARED_PREFERENCES, 0);
    }

    public String getAuthToken() {
        return getDefaultPrefs().getString(Constants.AUTH_TOKEN, null);
    }

    public void setAuthToken(String authToken) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putString(Constants.AUTH_TOKEN, authToken);
        editor.commit();
    }


    public boolean isAuthenticated() {
        return getDefaultPrefs().getBoolean(Constants.AUTHENTICATED, false);
    }

    @Deprecated
    public void setIsAuthenticated() {
        setAuthenticated(true);
    }

    @Deprecated
    public void disAuthenticate() {
        setAuthenticated(false);
        setFullName(null);
        setPicture(null);
    }

    public void setAuthenticated(boolean auth) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putBoolean(Constants.AUTHENTICATED, auth);
        editor.commit();
    }

    public long getCurrentRunId() {
        return getDefaultPrefs().getLong(Constants.CURRENT_RUN, -1);
    }

    public void setCurrentRunId(Long runId) {
        Editor editor = getDefaultPrefs().edit();
        editor.putLong(Constants.CURRENT_RUN, runId);
        editor.commit();
    }

    public String getFullId() {
        return getDefaultPrefs().getString(Constants.USER_EMAIL, null);
    }

    public void setFullId(String userName) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putString(Constants.USER_EMAIL, userName);
        editor.commit();
    }

    public String getPassword() {
        return getDefaultPrefs().getString(Constants.PASSWORD, null);
    }

    public void setPassword(String password) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putString(Constants.PASSWORD, password);
        editor.commit();
    }

    public void setIsRecording(boolean status) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putBoolean(Constants.RECORDING, status);
        editor.commit();

    }

    public boolean isRecording() {
        return getDefaultPrefs().getBoolean(Constants.RECORDING, false);
    }

    public Long getRunStart(long id) {
        return getDefaultPrefs().getLong(Constants.RUN+id, 0);
    }

    public void setRunStart(long id, long time) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putLong(Constants.RUN+id, time);
        editor.commit();
    }

    public Long getTotalScore() {
        return getDefaultPrefs().getLong(Constants.TOTAL_SCORE, Long.MIN_VALUE);
    }

    public void setTotalScore(long score) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putLong(Constants.TOTAL_SCORE, score);
        editor.commit();
    }

    public Boolean isScoringEnabled() {
        return getDefaultPrefs().getBoolean(Constants.SCORING_ENABLED, false);
    }

    public void setScoringEnabled (boolean scoringEnabled) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putBoolean(Constants.SCORING_ENABLED, scoringEnabled);
        editor.commit();
    }

    public void setStatus(int status) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putInt(Constants.STATUS, status);
        editor.commit();
    }

    public int getStatus() {
        return getDefaultPrefs().getInt(Constants.STATUS, 0);
    }

    public void setParticipateGameLastSynchronizationDate(long time) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putLong(Constants.PARTICIPATE_GAME_LAST_SYNC_DATE, time);
        editor.commit();
    }

    public long getParticipateGameLastSynchronizationDate() {
        return getDefaultPrefs().getLong(Constants.PARTICIPATE_GAME_LAST_SYNC_DATE, 0);
    }

    public void setMyGameLastSynchronizationDate(long time) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putLong(Constants.MY_GAME_LAST_SYNC_DATE, time);
        editor.commit();
    }

    public long getMyGameLastSynchronizationDate() {
        return getDefaultPrefs().getLong(Constants.MY_GAME_LAST_SYNC_DATE, 0);
    }



    public void setRunLastSynchronizationDate(long time) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putLong(Constants.RUN_LAST_SYNC_DATE, time);
        editor.commit();
    }

    public long getRunLastSynchronizationDate() {
        return getDefaultPrefs().getLong(Constants.RUN_LAST_SYNC_DATE, 0);
    }

    public void databaseReset() {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        setParticipateGameLastSynchronizationDate(0);
        setMyGameLastSynchronizationDate(0);
        setRunLastSynchronizationDate(0);
    }

    public void setGeneralItemsLastSynchronizationDate(long time, long gameId) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putLong(Constants.GI_LAST_SYNC_DATE+gameId, time);
        editor.commit();
    }

    public long getGeneralItemLastSynchronizationDate(Long gameId) {
        return getDefaultPrefs().getLong(Constants.GI_LAST_SYNC_DATE+gameId, 0);
    }

    public void setGeneralItemsVisibilityLastSynchronizationDate(long time, long runId) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putLong(Constants.GIVIS_LAST_SYNC_DATE+runId, time);
        editor.commit();
    }

    public long getGeneralItemsVisibilityLastSynchronizationDate(Long runId) {
        return getDefaultPrefs().getLong(Constants.GIVIS_LAST_SYNC_DATE+runId, 0);
    }

    @Deprecated
    public void setFullName(String givenName) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putString(Constants.GIVEN_NAME, givenName);
        editor.commit();
    }

    @Deprecated
    public String getFullName() {
        return getDefaultPrefs().getString(Constants.GIVEN_NAME, null);
    }

    @Deprecated
    public void setPicture(String picture) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putString(Constants.PICTUREL_URL, picture);
        editor.commit();
    }

    @Deprecated
    public String getPicture() {
        return getDefaultPrefs().getString(Constants.PICTUREL_URL, null);
    }

    public void setAccountLevel(Integer accountLevel) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putInt(Constants.ACCOUNT_Level, accountLevel);
        editor.commit();
    }

    public int getAccountLevel() {
        return getDefaultPrefs().getInt(Constants.ACCOUNT_Level, Account.USER);
    }

    public void setAccount(Long accountId) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putLong(Constants.CURRENT_ACCOUNT, accountId);
        editor.commit();
    }

    public long getAccount() {
        return getDefaultPrefs().getLong(Constants.CURRENT_ACCOUNT, 0);
    }

    public void setTimeDifferenceWithServer(long timeDiff) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putLong("timeDifferenceWithServer", timeDiff);
        editor.commit();
    }

    public long getTimeDifferenceWithServer() {
        return getDefaultPrefs().getLong("timeDifferenceWithServer", 0);

    }

    public void storeGCMKey(String key) {
        SharedPreferences.Editor editor = getDefaultPrefs().edit();
        editor.putString("GCM_REG_KEY:"+ getAppVersion(),key);
        editor.commit();
    }

    public String getGCMKey() {
        return getDefaultPrefs().getString("GCM_REG_KEY:"+ getAppVersion(), null);
    }


    private int getAppVersion() {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
