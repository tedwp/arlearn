package org.celstec.arlearn2.beans;

public class AuthResponse extends Bean{
	
	private String auth;

	public AuthResponse() {
		
	}
	
	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}
	
	@Override
	public boolean equals(Object obj) {
		AuthResponse other = (AuthResponse ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getAuth(), other.getAuth()); 
	}
}
