package org.celstec.arlearn2.client;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.beans.game.ScoreDefinition;
import org.celstec.arlearn2.network.ConnectionFactory;

public class LoginClient extends GenericClient{

	public static LoginClient instance;

	private LoginClient() {
		super("/login");
	}

	public static LoginClient getLoginClient() {
		if (instance == null) {
			instance = new LoginClient();
		}
		return instance;
	}

	public AuthResponse authenticate(String login, String password) {
		HttpResponse response = ConnectionFactory.getConnection().executePOST(getUrlPrefix(), null, "application/json", login + "\n" + password, "text/plain");
		try {
			String entry = EntityUtils.toString(response.getEntity());
			return (AuthResponse) jsonDeserialise(entry, AuthResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			AuthResponse rl = new AuthResponse();
			rl.setError("exception " + e.getMessage());
			return rl;
		}

	}
	
	public void instructions(String token, String account) {
		executePost(getUrlPrefix()+"/instructions/"+account, token, null, null);
	}

}
