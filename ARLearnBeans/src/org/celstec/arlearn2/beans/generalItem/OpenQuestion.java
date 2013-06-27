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

import org.celstec.arlearn2.beans.Bean;

public class OpenQuestion extends Bean {

	private boolean withAudio;
	private boolean withText;
	private boolean withValue;
	private boolean withPicture;
	private boolean withVideo;
	
	private String textDescription;
	private String valueDescription;
	
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
	
	
	public boolean isWithValue() {
		return withValue;
	}
	public void setWithValue(boolean withValue) {
		this.withValue = withValue;
	}
	public String getTextDescription() {
		return textDescription;
	}
	public void setTextDescription(String textDescription) {
		this.textDescription = textDescription;
	}
	public String getValueDescription() {
		return valueDescription;
	}
	public void setValueDescription(String valueDescription) {
		this.valueDescription = valueDescription;
	}
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		OpenQuestion other = (OpenQuestion ) obj;
		return 
				 nullSafeEquals(isWithVideo(), other.isWithVideo()) && 
				 nullSafeEquals(isWithPicture(), other.isWithPicture()) && 
				 nullSafeEquals(isWithText(), other.isWithText()) && 
				 nullSafeEquals(isWithValue(), other.isWithValue()) && 
				 nullSafeEquals(getTextDescription(), other.getTextDescription()) && 
				 nullSafeEquals(getValueDescription(), other.getValueDescription()) && 
				 nullSafeEquals(isWithAudio(), other.isWithAudio()); 

	}
}
