package org.celstec.arlearn2.beans.generalItem;

public class VideoObject extends NarratorItem {

	private String videoFeed;
	
	public VideoObject () {
		
	}

	public String getVideoFeed() {
		return videoFeed;
	}

	public void setVideoFeed(String videoFeed) {
		this.videoFeed = videoFeed;
	}
	
	public boolean equals(Object obj) {
		VideoObject other = (VideoObject ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getVideoFeed(), other.getVideoFeed()) ; 

	}
}
