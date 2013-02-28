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

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class OpenBadgeAssertion extends Bean {

	private String recipient;
	private String evidence;
	private String expires;
	private String issued_on;
	private String badge_name;
	private String badge_image;
	private String badge_description;
	private String badge_criteria;
	private String badge_issuer_origin;
	private String badge_issuer_org;
	private String badge_issuer_contact;
	private String badge_issuer_name;

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	
	public String getEvidence() {
		return evidence;
	}

	public void setEvidence(String evidence) {
		this.evidence = evidence;
	}

	public String getExpires() {
		return expires;
	}

	public void setExpires(String expires) {
		this.expires = expires;
	}

	public String getIssued_on() {
		return issued_on;
	}

	public void setIssued_on(String issued_on) {
		this.issued_on = issued_on;
	}

	public String getBadge_name() {
		return badge_name;
	}

	public void setBadge_name(String badge_name) {
		this.badge_name = badge_name;
	}

	public String getBadge_image() {
		return badge_image;
	}

	public void setBadge_image(String badge_image) {
		this.badge_image = badge_image;
	}

	public String getBadge_description() {
		return badge_description;
	}

	public void setBadge_description(String badge_description) {
		this.badge_description = badge_description;
	}

	public String getBadge_criteria() {
		return badge_criteria;
	}

	public void setBadge_criteria(String badge_criteria) {
		this.badge_criteria = badge_criteria;
	}

	public String getBadge_issuer_origin() {
		return badge_issuer_origin;
	}

	public void setBadge_issuer_origin(String badge_issuer_origin) {
		this.badge_issuer_origin = badge_issuer_origin;
	}

	public String getBadge_issuer_org() {
		return badge_issuer_org;
	}

	public void setBadge_issuer_org(String badge_issuer_org) {
		this.badge_issuer_org = badge_issuer_org;
	}

	public String getBadge_issuer_contact() {
		return badge_issuer_contact;
	}

	public void setBadge_issuer_contact(String badge_issuer_contact) {
		this.badge_issuer_contact = badge_issuer_contact;
	}


	public String getBadge_issuer_name() {
		return badge_issuer_name;
	}

	public void setBadge_issuer_name(String badge_issuer_name) {
		this.badge_issuer_name = badge_issuer_name;
	}


	public static BeanSerializer serializer = new BeanSerializer(){

		@Override
		public JSONObject toJSON(Object bean) {
			OpenBadgeAssertion ou = (OpenBadgeAssertion) bean;
			JSONObject returnObject =  new JSONObject();
			try {
				if (ou.getRecipient() != null) returnObject.put("recipient", ou.getRecipient());
				if (ou.getEvidence() != null) returnObject.put("evidence", ou.getEvidence());
				if (ou.getExpires() != null) returnObject.put("expires", ou.getExpires());
				if (ou.getIssued_on() != null) returnObject.put("issued_on", ou.getIssued_on());

				JSONObject jsonBadge = new JSONObject();
				jsonBadge.put("version", "1.0");
				if (ou.getBadge_name() != null) jsonBadge.put("name", ou.getBadge_name());
				if (ou.getBadge_image() != null) jsonBadge.put("image", ou.getBadge_image());
				if (ou.getBadge_description() != null) jsonBadge.put("description", ou.getBadge_description());
				if (ou.getBadge_criteria() != null) jsonBadge.put("criteria", ou.getBadge_criteria());
				returnObject.put("badge", jsonBadge);
				
				JSONObject jsonIssuer = new JSONObject();
				if (ou.getBadge_issuer_origin() != null) jsonIssuer.put("origin", ou.getBadge_issuer_origin());
				if (ou.getBadge_issuer_org() != null) jsonIssuer.put("org", ou.getBadge_issuer_org());
				if (ou.getBadge_issuer_contact() != null) jsonIssuer.put("contact", ou.getBadge_issuer_contact());
				if (ou.getBadge_issuer_name() != null) jsonIssuer.put("name", ou.getBadge_issuer_name());
				jsonBadge.put("issuer", jsonIssuer);


			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};

	
}

