package org.celstec.arlearn2.jdo.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.account.AccountList;
import org.celstec.arlearn2.beans.run.ResponseList;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.AccountJDO;
import org.celstec.arlearn2.jdo.classes.ResponseJDO;
import org.datanucleus.store.appengine.query.JDOCursorHelper;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.KeyFactory;

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
		account.setLastModificationDate(System.currentTimeMillis());
		
		try {
			pm.makePersistent(account);
			return account;
		} finally {
			pm.close();
		}
	}
	
	public static Account getAccount(Account myAccount) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			return toBean(pm.getObjectById(AccountJDO.class, KeyFactory.createKey(AccountJDO.class.getSimpleName(), myAccount.getAccountType()+":"+myAccount.getLocalId())));
		} finally {
			pm.close();
		}
	}
	
	static Account getAccount(PersistenceManager pm , int type, String localId) {
			return toBean(pm.getObjectById(AccountJDO.class, KeyFactory.createKey(
					AccountJDO.class.getSimpleName(), type+":"+localId)));
		
	}
	
	public static Account toBean(AccountJDO jdo) {
		if (jdo == null)
			return null;
		Account account = new Account();
		account.setLocalId(jdo.getLocalId());
		account.setAccountType(jdo.getAccountType());
		account.setEmail(jdo.getEmail());
		account.setName(jdo.getName());
		account.setFamilyName(jdo.getFamily_name());
		account.setGivenName(jdo.getGiven_name());
		account.setPicture(jdo.getPicture());
		return account;
	}

	public static Object getAccount(String accountId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			return toBean(pm.getObjectById(AccountJDO.class, KeyFactory.createKey(AccountJDO.class.getSimpleName(), accountId)));
		} finally {
			pm.close();
		}
	}

	
}
