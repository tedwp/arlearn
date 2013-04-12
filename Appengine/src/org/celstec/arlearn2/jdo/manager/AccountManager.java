package org.celstec.arlearn2.jdo.manager;

import javax.jdo.PersistenceManager;

import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.AccountJDO;

public class AccountManager {

	public static AccountJDO addAccount(String localID, int accountType, String email, String given_name, String family_name, String name, String picture) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		AccountJDO account = new AccountJDO();
		account.setLocalId(localID);
		account.setAccountType(accountType);
		account.setUniqueId();
		account.setEmail(email);
		account.setGiven_name(given_name);
		account.setFamily_name(family_name);
		account.setName(name);
		account.setPicture(picture);
		
		try {
			pm.makePersistent(account);
			return account;
		} finally {
			pm.close();
		}
	}
}
