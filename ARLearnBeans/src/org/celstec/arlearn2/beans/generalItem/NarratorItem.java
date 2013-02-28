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

public class NarratorItem extends GeneralItem {
	
	private String videoUrl;
	private String audioUrl;
	private String imageUrl;
	
	private String text;
	private String richText;
	private OpenQuestion openQuestion;
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		NarratorItem other = (NarratorItem ) obj;
		return 
			nullSafeEquals(getVideoUrl(), other.getVideoUrl()) &&
			nullSafeEquals(getAudioUrl(), other.getAudioUrl()) &&
			nullSafeEquals(getImageUrl(), other.getImageUrl()) &&
			nullSafeEquals(getText(), other.getText()) &&
			nullSafeEquals(getRichText(), other.getRichText()) &&
			nullSafeEquals(getOpenQuestion(), other.getOpenQuestion()) ; 

	}
	
	public NarratorItem(){
	 super();
	}
	
	public String getRichText() {
		return richText;
	}

	public void setRichText(String richText) {
		this.richText = richText;
	}

	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getAudioUrl() {
		return audioUrl;
	}
	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public OpenQuestion getOpenQuestion() {
		return openQuestion;
	}

	public void setOpenQuestion(OpenQuestion openQuestion) {
		this.openQuestion = openQuestion;
	}

}

