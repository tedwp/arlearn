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
package org.celstec.arlearn2.android.db.beans;

import java.io.File;

import org.celstec.arlearn2.client.GenericClient;

import android.net.Uri;

public class MediaCacheItem {
	
	private Long itemId;
	private String localId;
	private long runId;
	private String account;
	private String localFile;
	private Uri uri;
	
	private String localThumbnail;
	private String remoteFile;
	private String mimetype;
	private String token;
	private boolean incomming;
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
	
	public Uri getUri() {
		return uri;
	}
	public void setUri(Uri uri) {
		this.uri = uri;
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
	public int getReplicated() {
		return replicated;
	}
	public void setReplicated(int replicated) {
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
	
	public static String getVideoId(long runId, long time){
		return runId + ":vid:" + time;
	}
	
	public static String getImageId(long runId, long time){
		return runId + ":img:" + time;
	}
	
	public static String getAudioId(long runId, long time){
		return runId + ":audio:" + time;
	}
	
	public String buildRemotePath(Uri uri) {
		if (uri == null) {
			File localFile = new File(getLocalFile());
			return  GenericClient.urlPrefix + "/uploadService/"+getRunId()+"/"+getAccount()+"/"+localFile.getName();
		}
		return  GenericClient.urlPrefix + "/uploadService/"+getRunId()+"/"+getAccount()+"/"+uri.getLastPathSegment();
	}
	
}
