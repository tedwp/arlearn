package org.celstec.arlearn2.android.db.beans;

import java.io.File;

import org.celstec.arlearn2.client.GenericClient;

public class MediaCacheItem {
	
	private String itemId;
	private long runId;
	private String account;
	private String localFile;
	private String localThumbnail;
	private String remoteFile;
	private String mimetype;
	private String token;
	private boolean incomming;
	private boolean replicated;
	
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public long getRunId() {
		return runId;
	}
	public void setRunId(long runId) {
		this.runId = runId;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getLocalFile() {
		return localFile;
	}
	public void setLocalFile(String localFile) {
		this.localFile = localFile;
	}
	
	public String getLocalThumbnail() {
		return localThumbnail;
	}
	public void setLocalThumbnail(String localThumbnail) {
		this.localThumbnail = localThumbnail;
	}
	public String getRemoteFile() {
		return remoteFile;
	}
	public void setRemoteFile(String remoteFile) {
		this.remoteFile = remoteFile;
	}
	public boolean isIncomming() {
		return incomming;
	}
	public void setIncomming(boolean incomming) {
		this.incomming = incomming;
	}
	public boolean isReplicated() {
		return replicated;
	}
	public void setReplicated(boolean replicated) {
		this.replicated = replicated;
	}
	public String getMimetype() {
		return mimetype;
	}
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public String buildRemotePath() {
		File localFile = new File(getLocalFile());
		return  GenericClient.urlPrefix + "/uploadService/"+getRunId()+"/"+getAccount()+"/"+localFile.getName();
	}
	
}
