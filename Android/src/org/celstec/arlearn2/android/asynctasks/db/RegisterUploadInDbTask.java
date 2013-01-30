package org.celstec.arlearn2.android.asynctasks.db;

import org.celstec.arlearn2.android.asynctasks.DatabaseTask;
import org.celstec.arlearn2.android.asynctasks.GenericTask;
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
		runAfterTasks(db.getContext()); //TODO do this in the thread that did invokes the execute
	}
	
	

}
