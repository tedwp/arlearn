package org.celstec.arlearn2.client;

import org.celstec.arlearn2.beans.Version;

public class VersionClient extends GenericClient{
	
	private static VersionClient instance;

	private VersionClient() {
		super("/version");
	}

	public static VersionClient getVersionClient() {
		if (instance == null) {
			instance = new VersionClient();
		}
		return instance;
	}

	public Version getVersionInfo(int versionCode) {
		return (Version)  executeGet(getUrlPrefix()+"/"+versionCode, null, Version.class);
	}
	
	public Version createVersion(Version version) {
		return (Version) executePost(getUrlPrefix(), null, version, Version.class);
	}
}
