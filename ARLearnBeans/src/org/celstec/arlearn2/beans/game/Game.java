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
package org.celstec.arlearn2.beans.game;

import java.io.Serializable;


public class Game extends GameBean implements Serializable, Comparable<Game>{
	
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
	
	@Override
	public int compareTo(Game o) {
		int result = getTitle().compareToIgnoreCase(o.getTitle());
		if (result != 0) return result;
		return getGameId().compareTo(o.getGameId());
	}
	
}

