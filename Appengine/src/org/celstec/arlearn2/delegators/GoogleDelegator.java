package org.celstec.arlearn2.delegators;

import java.util.logging.Logger;
import com.google.gdata.util.AuthenticationException;

public class GoogleDelegator {
	protected static final Logger logger = Logger.getLogger(GoogleDelegator.class.getName());

	protected String authToken;

	public GoogleDelegator(String authToken) throws AuthenticationException {
		if (authToken == null) {
			this.authToken = null;
		} else {
			authToken = authToken.substring(authToken.indexOf("auth=") + 5);
			this.authToken = authToken;
		}
	}

	public GoogleDelegator(GoogleDelegator gd) {
		this.authToken = gd.authToken;
	}

	public GoogleDelegator() {
	}

	public String getAuthToken() {
		return authToken;
	}

}
