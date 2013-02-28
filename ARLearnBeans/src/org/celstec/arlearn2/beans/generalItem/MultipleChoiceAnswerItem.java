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

public class MultipleChoiceAnswerItem extends Bean {

	private Boolean isCorrect;
	private String answer;
	private String nfcTag;
	private String id;

	public MultipleChoiceAnswerItem() {
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getIsCorrect() {
		return isCorrect;
	}

	public void setIsCorrect(Boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public String getNfcTag() {
		return nfcTag;
	}

	public void setNfcTag(String nfcTag) {
		this.nfcTag = nfcTag;
	}

	public boolean equals(Object obj) {
		MultipleChoiceAnswerItem other = (MultipleChoiceAnswerItem ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getId(), other.getId()) && 
			nullSafeEquals(getIsCorrect(), other.getIsCorrect()) && 
			nullSafeEquals(getNfcTag(), other.getNfcTag()) && 
			nullSafeEquals(getAnswer(), other.getAnswer()) ; 
	}

}
