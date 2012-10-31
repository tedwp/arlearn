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
		if (!super.equals(obj)) return false;
		VideoObject other = (VideoObject ) obj;
		return 
			nullSafeEquals(getVideoFeed(), other.getVideoFeed()) ; 

	}
}
