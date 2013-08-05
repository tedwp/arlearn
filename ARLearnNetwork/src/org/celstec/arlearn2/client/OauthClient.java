package org.celstec.arlearn2.client;

import org.celstec.arlearn2.beans.game.GamesList;
import org.celstec.arlearn2.beans.oauth.OauthInfoList;

public class OauthClient extends GenericClient{

	private static OauthClient instance;
	private static final int ERROR_DESERIALIZING = 1;

	private OauthClient() {
		super("/oauth");
	}
	
	public static OauthClient getOauthClient() {
		if (instance == null) {
			instance = new OauthClient();
		}
		return instance;
	}
	
	public OauthInfoList getOauthInfo() {
		return (OauthInfoList)  executeGet(getUrlPrefix()+"/getOauthInfo", null, OauthInfoList.class);
	}
}
