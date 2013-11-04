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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.GameBean;

public class GeneralItem extends GameBean implements Comparable<GeneralItem>{
	
	private Long id;

	private Integer sortKey;

	private String scope;
	
	private String name;

	private String description;

	private Dependency dependsOn;

	private Dependency disappearOn;
	
	public Boolean autoLaunch;

	private Integer radius;
	
//	private Long showAtTimeStamp;

	private Double lng;

	private Double lat;
	
	private String iconUrl;

    private String iconUrlMd5Hash;

    private String section;

    private String tags;

	private List<String> roles;
	
	private Long visibleAt;
	
	private Long disappearAt;
	
	private Boolean showCountDown;

    private Boolean showOnMap;

    private Boolean showInList;

    private List<FileReference> fileReferences = new Vector();

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		GeneralItem other = (GeneralItem ) obj;
		return 
			nullSafeEquals(getId(), other.getId()) &&
			nullSafeEquals(getSortKey(), other.getSortKey()) &&
			nullSafeEquals(getScope(), other.getScope()) &&
			nullSafeEquals(getName(), other.getName()) &&
			nullSafeEquals(getDescription(), other.getDescription()) &&
			nullSafeEquals(getDependsOn(), other.getDependsOn()) &&
			nullSafeEquals(getDisappearOn(), other.getDisappearOn()) &&
			nullSafeEquals(getRadius(), other.getRadius()) &&
            nullSafeEquals(getSection(), other.getSection()) &
//			nullSafeEquals(getShowAtTimeStamp(), other.getShowAtTimeStamp()) &&
			nullSafeEquals(getLng(), other.getLng()) &&
			nullSafeEquals(getLat(), other.getLat()) &&
			nullSafeEquals(getIconUrl(), other.getIconUrl()) &&
            nullSafeEquals(getIconUrlMd5Hash(), other.getIconUrlMd5Hash()) &&
            nullSafeEquals(getSection(), other.getSection()) &&
            nullSafeEquals(getTags(), other.getTags()) &&
            nullSafeEquals(getShowCountDown(), other.getShowCountDown()) &&
            nullSafeEquals(getShowInList(), other.getShowInList()) &&
            nullSafeEquals(getShowOnMap(), other.getShowOnMap()) &&
			nullSafeEquals(getRoles(), other.getRoles()) ; 
	}

	
	
	public GeneralItem() {
		setType(getClass().getName());
	}

    public List<FileReference> getFileReferences(){
        return fileReferences;
    }

    public void setFileReferences(List<FileReference> fileReferences){
        this.fileReferences = fileReferences;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
		
	public Integer getSortKey() {
		return sortKey;
	}

	public void setSortKey(int sortKey) {
		this.sortKey = sortKey;
	}

	public String getScope() {
		if (scope == null) return "user";
		return scope;
	}

	public void setScope(String scope) {
		if (scope == null) this.scope = "user";
		this.scope = scope;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Dependency getDependsOn() {
		return dependsOn;
	}

	public void setDependsOn(Dependency dependsOn) {
		this.dependsOn = dependsOn;
	}
	
	public Dependency getDisappearOn() {
		return disappearOn;
	}

	public void setDisappearOn(Dependency disappearOn) {
		this.disappearOn = disappearOn;
	}

	public Boolean getAutoLaunch() {
		return autoLaunch;
	}

	public void setAutoLaunch(Boolean autoLaunch) {
		this.autoLaunch = autoLaunch;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
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
	
	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

    public String getIconUrlMd5Hash() {
        return iconUrlMd5Hash;
    }

    public void setIconUrlMd5Hash(String iconUrlMd5Hash) {
        this.iconUrlMd5Hash = iconUrlMd5Hash;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public boolean isMessage() {
		return (getLat() == null && getLng() == null);
	}


	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	@SuppressWarnings("ConstantConditions")
    @Override
	public int compareTo(GeneralItem other) {
        if (getSection()== null) setSection("");
        if (other.getSection()== null) other.setSection("");
		if (getSortKey() == null) setSortKey(0);
		if (other.getSortKey() == null) other.setSortKey(0);
        if (!getSection().equals(other.getSection())) {
            return getSection().compareTo(other.getSection());
        }
        if (getSortKey() == null) setSortKey(0);
        if (other.getSortKey() == null) other.setSortKey(0);
		int returnValue = getSortKey() - other.getSortKey();
		if (returnValue != 0) return returnValue;
		returnValue = getName().compareTo(other.getName());
		if (returnValue != 0) return returnValue;
		return getId().compareTo(other.getId());
	}

	public Long getVisibleAt() {
		return visibleAt;
	}

	public void setVisibleAt(Long visibleAt) {
		this.visibleAt = visibleAt;
	}

	public Long getDisappearAt() {
		return disappearAt;
	}

	public void setDisappearAt(Long disappearAt) {
		this.disappearAt = disappearAt;
	}

	public Boolean getShowCountDown() {
		return showCountDown;
	}

	public void setShowCountDown(Boolean showCountDown) {
		this.showCountDown = showCountDown;
	}


    public Boolean getShowOnMap() {
        return showOnMap;
    }

    public void setShowOnMap(Boolean showOnMap) {
        this.showOnMap = showOnMap;
    }

    public Boolean getShowInList() {
        return showInList;
    }

    public void setShowInList(Boolean showInList) {
        this.showInList = showInList;
    }

//    public static void main(String[] args) throws Exception {
//        GeneralItem gi = new GeneralItem();
//        FileReference fr = new FileReference();
//        fr.setKey("filip");
//        List<FileReference> arrayList = new Vector<FileReference>();
//        arrayList.add(fr);
//        gi.setFileReferences(arrayList);
//
//        System.out.println(gi);
//        Object o = JsonBeanDeserializer.deserialize(gi.toString());
//        System.out.println(o);
//    }
}
