package org.celstec.arlearn2.beans;

public class Version extends Bean {

	private Integer versionCode;
	private String serviceUrl;

	public Integer getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	@Override
	public boolean equals(Object obj) {
		Version other = (Version) obj;
		return super.equals(obj) && nullSafeEquals(getVersionCode(), other.getVersionCode()) && 
				nullSafeEquals(getServiceUrl(), other.getServiceUrl());
	}
}
