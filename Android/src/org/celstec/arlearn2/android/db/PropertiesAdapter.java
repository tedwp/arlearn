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

import org.celstec.arlearn2.android.Constants;

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
	
	private SharedPreferences getDefaultPrefs() {
		return context.getSharedPreferences(Constants.SHARED_PREFERENCES, 0);
	}
	
	public String getFusionAuthToken() {
		return getDefaultPrefs().getString(Constants.FUSION_SERVICE+Constants.TOKEN, null);
	}
	
	public void setFusionAuthToken(String authToken) {
		SharedPreferences.Editor editor = getDefaultPrefs().edit();
		editor.putString(Constants.FUSION_SERVICE+Constants.TOKEN, authToken);
		editor.commit();
	}
	
	
	public boolean isAuthenticated() {
		return getDefaultPrefs().getBoolean(Constants.AUTHENTICATED, false);
	}
	
	public void setIsAuthenticated() {
		setAuthenticated(true);
	}
	
	public void disAuthenticate() {
		setAuthenticated(false);
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
	
	public String getUsername() {
		return getDefaultPrefs().getString(Constants.USER_EMAIL, null);
	}
	
	public void setUsername(String userName) {
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
		editor.putLong(Constants.PARTICIPATE_GAME_LAST_SYNC_DATE, time);
		editor.commit();
	}
	
	public long getMyGameLastSynchronizationDate() {
		return getDefaultPrefs().getLong(Constants.PARTICIPATE_GAME_LAST_SYNC_DATE, 0);
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
}
