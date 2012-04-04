package org.celstec.arlearn2.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AuthResponse")
public class AuthResponse extends Bean {
	
	private String auth;

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

}
