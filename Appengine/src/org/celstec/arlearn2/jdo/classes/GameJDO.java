package org.celstec.arlearn2.jdo.classes;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class GameJDO extends GameClass {
	
	@Persistent
    private String title;

	@Persistent
    private String owner;
	
	@Persistent
    private String creatorEmail;
	
	@Persistent
    private String feedUrl;
	
	@Persistent
	private Text config;

	public Long getGameId() {
		return id.getId();
	}
	
	public void setGameId(Long gameId) {
		if (gameId != null) 
			setGameId(KeyFactory.createKey(GameJDO.class.getSimpleName(), gameId));
	}
	
	public void setGameId(Key gameId) {
		this.id = gameId;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
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

	public String getConfig() {
		if (config == null) return null;
		return config.getValue();
	}

	public void setConfig(String config) {
		this.config = new Text(config);
	}
	
}
