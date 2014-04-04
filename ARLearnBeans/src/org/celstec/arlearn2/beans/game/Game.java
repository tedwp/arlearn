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
	
	public static final int PRIVATE = 1;
	public static final int WITH_LINK = 2;
	public static final int PUBLIC = 3;
	
	private String title;
	private String creator;
	private String description;
	private String owner;
	private String feedUrl;
	private Config config;
	private Integer sharing;
	private String licenseCode;
    private String language;
    private Double lng;
    private Double lat;
	
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

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
        if (config == null) return new Config();
		return config;
	}


	public void setConfig(Config config) {
		this.config = config;
	}
	
	
	public Integer getSharing() {
		return sharing;
	}

	public void setSharing(Integer sharing) {
		this.sharing = sharing;
	}

	public String getLicenseCode() {
		return licenseCode;
	}

	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

	@Override
	public boolean equals(Object obj) {
		Game other = (Game ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getTitle(), other.getTitle()) && 
			nullSafeEquals(getCreator(), other.getCreator()) && 
			nullSafeEquals(getOwner(), other.getOwner()) && 
			nullSafeEquals(getFeedUrl(), other.getFeedUrl()) && 
			nullSafeEquals(getConfig(), other.getConfig()) && 
			nullSafeEquals(getDescription(), other.getDescription()) && 
			nullSafeEquals(getLicenseCode(), other.getLicenseCode()) && 
			nullSafeEquals(getSharing(), other.getSharing()); 

	}

    public String getLanguage() {
        if (language == null) return "en";
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
	public int compareTo(Game o) {
		int result = getTitle().compareToIgnoreCase(o.getTitle());
		if (result != 0) return result;
		return getGameId().compareTo(o.getGameId());
	}
	
}

