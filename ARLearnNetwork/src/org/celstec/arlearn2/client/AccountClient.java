package org.celstec.arlearn2.client;

import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.beans.account.Account;

public class AccountClient extends GenericClient{

	public static AccountClient instance;

	private AccountClient() {
		super("/account");
	}

	public static AccountClient getAccountClient() {
		if (instance == null) {
			instance = new AccountClient();
		}
		return instance;
	}
	
	public AuthResponse anonymousLogin(String accountToken) {
		return (AuthResponse)  executeGet(getUrlPrefix()+"/anonymousLogin/"+accountToken, null, AuthResponse.class);
	}

	public Account accountDetails(String token) {
		return (Account)  executeGet(getUrlPrefix()+"/accountDetails", token, Account.class);
	}
}
