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
