package org.celstec.arlearn2.beans.game;

import java.io.Serializable;


public class Game extends GameBean implements Serializable{
	
	private String title;
	private String creator;
	private String owner;
	private String feedUrl;
	private Config config;
	
	public Game() {
		
	}
		
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getFeedUrl() {
		return feedUrl;
	}

	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}


	public Config getConfig() {
		return config;
	}


	public void setConfig(Config config) {
		this.config = config;
	}
	
	@Override
	public boolean equals(Object obj) {
		Game other = (Game ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getTitle(), other.getTitle()) && 
			nullSafeEquals(getCreator(), other.getCreator()) && 
			nullSafeEquals(getOwner(), other.getOwner()) && 
			nullSafeEquals(getFeedUrl(), other.getFeedUrl()) && 
			nullSafeEquals(getConfig(), other.getConfig()); 

	}
	
	
}

