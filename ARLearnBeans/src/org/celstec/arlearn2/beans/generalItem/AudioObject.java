package org.celstec.arlearn2.beans.generalItem;

public class AudioObject extends NarratorItem {
	
	private String audioFeed;

	public AudioObject() {
		super();
	}

	public String getAudioFeed() {
		return audioFeed;
	}

	public void setAudioFeed(String audioFeed) {
		this.audioFeed = audioFeed;
	}
	
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		AudioObject other = (AudioObject ) obj;
		return 
			nullSafeEquals(getAudioFeed(), other.getAudioFeed()) ; 

	}
}

