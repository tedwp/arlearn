package org.celstec.arlearn2.genericBeans;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class NarratorItem extends GeneralItem {

	private String audioUrl;
	private String videoUrl;
	private String imageUrl;
	private String text;

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

	public String getSpecificPartAsBean() {
		JSONObject json = new JSONObject();
		try {
			if (getText() != null)json.put("text", getText());
			if (getVideoUrl() != null)json.put("videoUrl", getVideoUrl());
			if (getAudioUrl() != null)json.put("audioUrl", getAudioUrl());
			if (getImageUrl() != null)json.put("imageUrl", getImageUrl());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

}
