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
package org.celstec.arlearn2.android.asynctasks.db;

import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
import org.celstec.arlearn2.android.broadcast.NetworkSwitcher;
import org.celstec.arlearn2.android.cache.MediaUploadCache;
import org.celstec.arlearn2.android.db.DBAdapter;

import android.content.Context;
import android.net.Uri;
import android.os.Message;

public class RegisterUploadInDbTask  extends GenericTask implements  DatabaseTask {

	private Long itemId;
	private String localId;
	private Long gameId;
	private Long runId;
	private String username;
	private Uri localFile;
	private String mimetype;
	
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Uri getLocalFile() {
		return localFile;
	}

	public void setLocalFile(Uri localFile) {
		this.localFile = localFile;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public static RegisterUploadInDbTask uploadFile(Long runId, String localId, String username, Uri localFile, String mimetype) {
		RegisterUploadInDbTask task = new RegisterUploadInDbTask();
		task.setUsername(username);
		task.setLocalFile(localFile);
		task.setMimetype(mimetype);
		task.setLocalId(localId);
		task.setRunId(runId);
		
		return task;
	}
	
	public void run(Context ctx) {
		Message m = Message.obtain(DBAdapter.getDatabaseThread(ctx));
		m.obj = this;
		m.sendToTarget();
	}
	
	@Override
	public void execute(DBAdapter db) {
		db.getMediaCacheUpload().addFileToUpload(getItemId(), getLocalId(), getGameId(), getRunId(), getUsername(), getLocalFile(), getMimetype());
		if (NetworkSwitcher.isOnline(db.getContext())) runAfterTasks(db.getContext()); //TODO do this in the thread that did invokes the execute
	}
	
	

}
