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
package org.celstec.arlearn2.beans.generalItem;

public class AudioObject extends NarratorItem {
	
	private String audioFeed;
    private Boolean autoPlay;
    private String md5Hash;

	public AudioObject() {
		super();
	}

	public String getAudioFeed() {
		return audioFeed;
	}

	public void setAudioFeed(String audioFeed) {
		this.audioFeed = audioFeed;
	}

    public Boolean getAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(Boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		AudioObject other = (AudioObject ) obj;
		return
            nullSafeEquals(getMd5Hash(), other.getMd5Hash())&&
			nullSafeEquals(getAudioFeed(), other.getAudioFeed())&&
            nullSafeEquals(getAutoPlay(), other.getAutoPlay()) ;

	}

    public String getMd5Hash() {
        return md5Hash;
    }

    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }
}

