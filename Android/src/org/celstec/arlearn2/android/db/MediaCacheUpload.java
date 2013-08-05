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

import java.io.File;

import org.celstec.arlearn2.android.cache.MediaUploadCache;
import org.celstec.arlearn2.android.db.MediaCacheUpload.UploadItem;
import org.celstec.arlearn2.android.db.beans.MediaCacheItem;
import org.celstec.arlearn2.client.GenericClient;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

public class MediaCacheUpload extends GenericDbTable {
	
	
	public static final String MEDIACACHE_UPLOAD = "mediaCacheUpload";

	public static final String ITEM_ID = "itemId";
	public static final String LOCAL_ID = "localId";
	public static final String GAME_ID = "gameId";
	public static final String RUN_ID = "runId";
	
	public static final String ACCOUNT = "account";
	
	public static final String URI = "localUri";
	public static final String REMOTE_FILE = "remoteFile";
	public static final String REPLICATED = "replicated";
	public static final String MIMETYPE = "mimetype";
	
	public static final int REP_STATUS_TODO = 0;
	public static final int REP_STATUS_SYNCING = 1;
	public static final int REP_STATUS_DONE = 2;
	
	public MediaCacheUpload(DBAdapter db) {
		super(db);
	}

	@Override
	public String createStatement() {
		return "create table " + MEDIACACHE_UPLOAD + " (" 
				+ ITEM_ID + " long, " // 0
				+ LOCAL_ID + " text, "
				+ GAME_ID + " long, "
				+ RUN_ID + " long, " 
				
				+ ACCOUNT + " text, " //4
				
				+ URI + " text, "
				+ REMOTE_FILE + " text, " 
				+ REPLICATED + " integer, "// 7
				+ MIMETYPE + " text);";
	}

	@Override
	protected String getTableName() {
		return MEDIACACHE_UPLOAD;
	}
	
	private UploadItem[] query(String selection, String[] selectionArgs, int amount) {
		UploadItem[] resultGenIt = null;
		Cursor mCursor = null;
		try {
			mCursor = db.getSQLiteDb().query(true, getTableName(), null, selection, selectionArgs, null, null, null, null);
			if (amount > mCursor.getCount())
				amount = mCursor.getCount();
			if (amount == 0)
				amount = mCursor.getCount();

			resultGenIt = new UploadItem[amount];
			int i = 0;

			while (mCursor.moveToNext() && i < amount) {
				
				resultGenIt[i++] = cursorToUploadItem(mCursor);
			}

		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		} finally {
			if (mCursor != null)
				mCursor.close();
		}
		return resultGenIt;
	}
	
	private UploadItem cursorToUploadItem(Cursor mCursor ) {
		UploadItem mci = new UploadItem();
		mci.setItemId(mCursor.getLong(0));
		mci.setLocalId(mCursor.getString(1));
		mci.setGameId(mCursor.getLong(2));
		mci.setRunId(mCursor.getLong(3));
		mci.setUserId(mCursor.getString(4));
		String uri = mCursor.getString(5);
		if (uri != null && !"".equals(uri))
			mci.setUri(Uri.parse(uri));
		mci.setRemoteUrl(mCursor.getString(6));
		mci.setReplicated(mCursor.getInt(7));
		mci.setMimetype(mCursor.getString(8));
		return mci;
	}
	
	
	
	public void addFileToUpload(Long itemId, String localId, Long gameId, Long runId, String account, Uri uri, String mimeType) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(ITEM_ID, itemId);
		initialValues.put(LOCAL_ID, localId);
		initialValues.put(GAME_ID, gameId);
		initialValues.put(RUN_ID, runId);
		initialValues.put(ACCOUNT, account);
		initialValues.put(URI, uri.toString());
		initialValues.put(REPLICATED, REP_STATUS_TODO);
		initialValues.put(MIMETYPE, mimeType);
		
		UploadItem ui = new UploadItem();
		ui.setRunId(runId);
		ui.setUserId(account);
		ui.setUri(uri);
		ui.setRemoteUrl(ui.buildRemotePath());
		MediaUploadCache.getInstance(runId).put(ui);

		db.getSQLiteDb().insert(getTableName(), null, initialValues);	
	}

	public void deleteRun(Long runId) {
		db.getSQLiteDb().delete(getTableName(), RUN_ID + " = " + runId, null);
	}

	public UploadItem getNextItemToUpload() {
		UploadItem[] allItems = query(REPLICATED + " = ? ", new String[] { "" + REP_STATUS_TODO }, 1);
		if (allItems.length == 0) return null;
		return allItems[0];
	}
	
	public UploadItem[] allItems() {
		return query(null, null, 1);
	}
	
	public UploadItem[] allItemsForRun(long runId) {
		return query(RUN_ID + " = ?", new String[] {""+runId}, 0);
	}
	
	
	public void writeUploadStatus(UploadItem uploadItem, int status) {
		ContentValues newValue = new ContentValues();
		newValue.put(REPLICATED	, status);
		String remote = uploadItem.buildRemotePath();
		uploadItem.setRemoteUrl(remote);
		if (remote != null && status == REP_STATUS_DONE) {
			newValue.put(REMOTE_FILE, remote);
		}
		String parameter = "";
		String args[] = new String[]{};
		if (uploadItem.getItemId() != null && uploadItem.getItemId() != 0) {
			parameter = addParameter(parameter, ITEM_ID);
			args = addArg(args, ""+uploadItem.getItemId());
		}
		if (uploadItem.getLocalId() != null) {
			parameter = addParameter(parameter, LOCAL_ID);
			args = addArg(args, ""+uploadItem.getLocalId());
		}
		if (uploadItem.getRunId() != null && uploadItem.getRunId() != 0) {
			parameter = addParameter(parameter, RUN_ID);
			args = addArg(args, ""+uploadItem.getRunId());
		}
		if (uploadItem.getUserId() != null) {
			parameter = addParameter(parameter, ACCOUNT);
			args = addArg(args, ""+uploadItem.getUserId());
		}
		db.getSQLiteDb().update(getTableName(), newValue, parameter, args);
		MediaUploadCache.getInstance(uploadItem.getRunId()).put(uploadItem);
	}
	
	private String addParameter(String parameter, String column) {
		if (!"".equals(parameter)) {
			parameter += " and ";
		}
		parameter += column + " = ?";
		return parameter;
	}
	private String[] addArg(String[] argsSoFar, String newArg) {
		String [] returnString = new String[argsSoFar.length+1];
		for (int i = 0; i< argsSoFar.length; i++) {
			returnString[i] = argsSoFar[i];
		}
		returnString[argsSoFar.length] =newArg;
		return returnString;
	}
	
	public class UploadItem {
		
		private Long itemId;
		private String localId;
		private Long gameId;
		private Long runId;
		private String userId;
		private String remoteUrl;
		private Uri uri;
		private String mimetype;
		private int replicated;
		
		public Long getItemId() {
			return itemId;
		}
		public void setItemId(Long itemId) {
			this.itemId = itemId;
		}
		public String getLocalId() {
			return localId;
		}
		public void setLocalId(String localId) {
			this.localId = localId;
		}
		public Long getGameId() {
			return gameId;
		}
		public void setGameId(Long gameId) {
			this.gameId = gameId;
		}
		public Long getRunId() {
			return runId;
		}
		public void setRunId(Long runId) {
			this.runId = runId;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public Uri getUri() {
			return uri;
		}
		public void setUri(Uri uri) {
			this.uri = uri;
		}
		public String getMimetype() {
			return mimetype;
		}
		public void setMimetype(String mimetype) {
			this.mimetype = mimetype;
		}
		
		public String getRemoteUrl() {
			return remoteUrl;
		}
		public void setRemoteUrl(String remoteUrl) {
			this.remoteUrl = remoteUrl;
		}
		public int getReplicated() {
			return replicated;
		}
		public void setReplicated(int replicated) {
			this.replicated = replicated;
		}
		public String buildRemotePath() {
			if (getRunId() != null && getUserId() != null) {
				return  GenericClient.urlPrefix + "/uploadService/"+getRunId()+"/"+getUserId()+"/"+getUri().getLastPathSegment();
			}
			return  null;
		}
	}

	
	
}
