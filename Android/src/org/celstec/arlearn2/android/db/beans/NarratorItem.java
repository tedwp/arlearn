package org.celstec.arlearn2.android.db.beans;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class NarratorItem extends GeneralItem {
	
	private String audioUrl;
	private String videoUrl;
	private String imageUrl;
	private String text;
	private String html;
	private Boolean isOpenQuestion;
	
	public NarratorItem(GeneralItem gi) {
		super(gi);
		if (gi.getPayload() != null) {
			try {
				JSONObject object = new JSONObject(gi.getPayload());
				if (object != null) {
					if (object.has("html")) {
						setHtml(object.getString("html")); 
					}
					if (object.has("isOpenQuestion")) {
						setIsOpenQuestion(object.getBoolean("isOpenQuestion")); //TODO other fields
					}
				}
			} catch (JSONException e) {
				// in case of a JSON exception, the fields are not filled in
			}
		}
	}

	public NarratorItem() {
		super();
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
	
	

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
	

	public Boolean getIsOpenQuestion() {
		return isOpenQuestion;
	}

	public void setIsOpenQuestion(Boolean isOpenQuestion) {
		this.isOpenQuestion = isOpenQuestion;
	}

	public JSONObject getSpecificPartAsJson() {
		JSONObject json = new JSONObject();
		try {
			if (getText() != null)json.put("text", getText());
			if (getVideoUrl() != null)json.put("videoUrl", getVideoUrl());
			if (getAudioUrl() != null)json.put("audioUrl", getAudioUrl());
			if (getImageUrl() != null)json.put("imageUrl", getImageUrl());
			if (getHtml() != null) json.put("html", getHtml());
			if (getIsOpenQuestion() != null) json.put("isOpenQuestion", getIsOpenQuestion());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

}
