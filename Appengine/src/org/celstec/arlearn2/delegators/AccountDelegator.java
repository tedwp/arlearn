package org.celstec.arlearn2.delegators;

import java.util.UUID;

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.jdo.classes.AccountJDO;
import org.celstec.arlearn2.jdo.manager.AccountManager;

import com.google.gdata.util.AuthenticationException;

public class AccountDelegator extends GoogleDelegator {

	public AccountDelegator(String authtoken) throws AuthenticationException {
		super(authtoken);
	}

	public AccountDelegator(GoogleDelegator gd) {
		super(gd);
	}

	public Account getAccountInfo(Account myAccount) {
		return AccountManager.getAccount(myAccount);
		
	}

	public Account getContactDetails(String accountId) {
		return AccountManager.getAccount(accountId);
	}

	public Account createAnonymousContact(Account inContact) {
		String localID = UUID.randomUUID().toString();
		return AccountManager.toBean(AccountManager.addAccount(localID, 0, inContact.getEmail(), inContact.getGivenName(), inContact.getFamilyName(), inContact.getName(), inContact.getPicture()));
	}

}
