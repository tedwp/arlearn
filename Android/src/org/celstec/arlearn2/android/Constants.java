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
package org.celstec.arlearn2.android;


public class Constants {
	
	public static String SHARED_PREFERENCES = "prefs";
	public static String AUTHENTICATED = "authenticated";
	public static String USER_EMAIL = "userEmail";
	public static String ACCOUNT_Level = "accountLevel";
	public static final String PASSWORD = "password";
	public static final String PLAYING = "isPlaying";
	public static String RECORDING = "isRecording";
	public static String SCORING_ENABLED = "scoringEnabled";
	public static String MAP_ENABLED = "scoringEnabled";
	public static final String CACHE_DIR = "arlearn2";
	public static final String INCOMMING = "incomming";
	public static final String OUTGOING = "outgoing";
	public static final String RUN = "run_";
	public static final String PARTICIPATE_GAME_LAST_SYNC_DATE = "participateGameLastSyncDate";
	public static final String MY_GAME_LAST_SYNC_DATE = "myGameLastSyncDate";
	public static final String RUN_LAST_SYNC_DATE = "runLastSyncDate";
	public static final String GI_LAST_SYNC_DATE = "giLastSyncDate";
	public static final String GIVIS_LAST_SYNC_DATE = "giVisLastSyncDate";
	public static final String GIVEN_NAME = "givenName";
	public static final String PICTUREL_URL = "pictureUrl";
	public static String AUTH_TOKEN = "authToken";
	public static String CURRENT_RUN = "CurrentRun";
	public static String TOTAL_SCORE = "TotalScore";
	public static String STATUS = "status";
	
//	public static String ITEM_ID = "id";

	public static long TIME_BETWEEN_GPS_UPDATES_UNSENSITIVE = 20000; //time in milliseconds
	public static int DISTANCE_GPS_LISTENER_IN_METERES_UNSENSITIVE = 10;
	
	
	public static final String GI_TYPE_NARRATOR_ITEM = "org.celstec.arlearn2.beans.generalItem.NarratorItem";
	public static final String GI_TYPE_MULTIPLE_CHOICE = "org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest";
	public static final String GI_TYPE_MULTIPLE_CHOICE_ANSWER = "org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem";
	public static final String GI_TYPE_VIDEO_OBJECT = "org.celstec.arlearn2.beans.generalItem.VideoObject";
	public static final String GI_TYPE_YOUTUBE_MOVIE = "org.celstec.arlearn2.beans.generalItem.YoutubeObject";
	public static final String GI_TYPE_AUDIO_OBJECT = "org.celstec.arlearn2.beans.generalItem.AudioObject";
	public static final String GI_TYPE_PICTURE = "org.celstec.arlearn2.beans.generalItem.Picture";
	
	public static final Integer AUTHORING_ACTION_NONE = 0;
	public static final Integer AUTHORING_ACTION_CREATE = 1;
	public static final Integer AUTHORING_ACTION_EDIT = 2;
	
	// Creative Commons licenses
	public static final int CC_BYPD = 0;
	public static final int CC_BY = 1;
	public static final int CC_BYSA = 2;
	public static final int CC_BYND = 3;
	public static final int CC_BYNC = 4;
	public static final int CC_BYNCSA = 5;
	public static final int CC_BYNCND = 6;
	public static final int CC_BYNPD = 7;
	
	
	
}
