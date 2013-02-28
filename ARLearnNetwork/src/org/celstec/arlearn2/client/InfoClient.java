package org.celstec.arlearn2.client;

import org.celstec.arlearn2.beans.Info;

public class InfoClient extends GenericClient{
	
	private static InfoClient instance;

	private InfoClient() {
		super("/info");
	}

	public static InfoClient getVersionClient() {
		if (instance == null) {
			instance = new InfoClient();
		}
		return instance;
	}

	public Info getInfo() {
		return (Info)  executeGet(getUrlPrefix(), null, Info.class);
	}
	
	
}
