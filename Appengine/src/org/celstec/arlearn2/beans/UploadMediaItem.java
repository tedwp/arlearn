package org.celstec.arlearn2.beans;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name="Response", namespace="http://celstec.org/arlearn2")
public class UploadMediaItem extends RunBean {

	private String itemId;
	private String userEmail;
	private String imgagePath;
	private String audioPath;
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getImgagePath() {
		return imgagePath;
	}
	public void setImgagePath(String imgagePath) {
		this.imgagePath = imgagePath;
	}
	public String getAudioPath() {
		return audioPath;
	}
	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}
}
