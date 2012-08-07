package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;

public class OpenQuestion extends Bean {

	private boolean withAudio;
	private boolean withPicture;
	private boolean withVideo;
	
	public boolean isWithAudio() {
		return withAudio;
	}
	public void setWithAudio(boolean withAudio) {
		this.withAudio = withAudio;
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
		OpenQuestion other = (OpenQuestion ) obj;
		return super.equals(obj) && 
				 nullSafeEquals(isWithVideo(), other.isWithVideo()) && 
				 nullSafeEquals(isWithPicture(), other.isWithPicture()) && 
				 nullSafeEquals(isWithAudio(), other.isWithAudio()); 

	}
}
