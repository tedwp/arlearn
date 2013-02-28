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
package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.Bean;

public class LocationUpdate extends Bean {
	
	public static final int PLAYER = 1;
	public static final int MODERATOR = 2;
	
	private Double lat;
	private Double lng;
	private String account;
	private Integer recepientType;	
	
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Integer getRecepientType() {
		return recepientType;
	}
	public void setRecepientType(Integer recepientType) {
		this.recepientType = recepientType;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		LocationUpdate other = (LocationUpdate) obj;
		return super.equals(obj) && 
			nullSafeEquals(getAccount(), other.getAccount()) &&
			nullSafeEquals(getRecepientType(), other.getRecepientType()) &&
			nullSafeEquals(getLat(), other.getLat()) &&
			nullSafeEquals(getLng(), other.getLng()); 
	}
}
