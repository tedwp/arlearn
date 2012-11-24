package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;

public class OpenQuestion extends Bean {

	private boolean withAudio;
	private boolean withText;
	private boolean withPicture;
	private boolean withVideo;
	
	public boolean isWithAudio() {
		return withAudio;
	}
	public void setWithAudio(boolean withAudio) {
		this.withAudio = withAudio;
	}
	public boolean isWithText() {
		return withText;
	}
	public void setWithText(boolean withText) {
		this.withText = withText;
	}
	public boolean isWithPicture() {
		return withPicture;
	}
	public void setWithPicture(boolean withPicture) {
		this.withPicture = withPicture;
	}
	
	public boolean isWithVideo() {
		return withVideo;
	}
	public void setWithVideo(boolean withVideo) {
		this.withVideo = withVideo;
	}
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		OpenQuestion other = (OpenQuestion ) obj;
		return 
				 nullSafeEquals(isWithVideo(), other.isWithVideo()) && 
				 nullSafeEquals(isWithPicture(), other.isWithPicture()) && 
				 nullSafeEquals(isWithText(), other.isWithText()) && 
				 nullSafeEquals(isWithAudio(), other.isWithAudio()); 

	}
}
