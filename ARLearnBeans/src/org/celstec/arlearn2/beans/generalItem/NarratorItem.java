package org.celstec.arlearn2.beans.generalItem;

public class NarratorItem extends GeneralItem {
	
	private String videoUrl;
	private String audioUrl;
	private String imageUrl;
	private String text;
	private String richText;
	private OpenQuestion openQuestion;
	
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

