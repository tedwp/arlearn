package org.celstec.arlearn2.delegators;

import java.util.Iterator;
import java.util.StringTokenizer;

import org.celstec.arlearn2.beans.run.RunAccess;
import org.celstec.arlearn2.beans.run.RunAccessList;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.jdo.classes.RunAccessJDO;
import org.celstec.arlearn2.jdo.manager.RunAccessManager;

import com.google.gdata.util.AuthenticationException;

public class RunAccessDelegator extends GoogleDelegator {

	public RunAccessDelegator(String authtoken) throws AuthenticationException {
		super(authtoken);
	}

	public RunAccessDelegator(GoogleDelegator gd) {
		super(gd);
	}
	
	public void provideAccessWithCheck(Long runIdentifier, String account, Integer accessRight) {
		provideAccess(runIdentifier, account, accessRight);
	}
	
	public void provideAccess(Long runIdentifier, String account, int accessRights) {
		StringTokenizer st = new StringTokenizer(account, ":");
		int accountType = 0;
		String localID = null;
		if (st.hasMoreTokens()) {
			accountType = Integer.parseInt(st.nextToken());
		}
		if (st.hasMoreTokens()) {
			localID = st.nextToken();
		}
		RunAccessManager.addRunAccess(localID, accountType, runIdentifier, accessRights);
	}
	
	public RunAccessList getRunsAccess(Long from, Long until) {
		RunAccessList gl = new RunAccessList();
		String myAccount = UserLoggedInManager.getUser(authToken);
		if (myAccount == null) {
			gl.setError("login to retrieve your list of runs");
			return gl;
		}
		return getRunsAccess(myAccount, from, until);
	}
	
	public RunAccessList getRunsAccess(String account, Long from, Long until) {
		StringTokenizer st = new StringTokenizer(account, ":");
		int accountType = 0;
		String localID = null;
		if (st.hasMoreTokens()) {
			accountType = Integer.parseInt(st.nextToken());
		}
		if (st.hasMoreTokens()) {
			localID = st.nextToken();
		}
		Iterator<RunAccess> it = RunAccessManager.getRunList(accountType, localID, from, until).iterator();
		RunAccessList rl = new RunAccessList();
		while (it.hasNext()) {
			RunAccess ga = (RunAccess) it.next();
			rl.addRunAccess(ga);
		}
		rl.setServerTime(System.currentTimeMillis());
		return rl;
	}
	
	public boolean isOwner(String myAccount, Long runId) {
		try {
			return RunAccessManager.getAccessById(myAccount + ":" + runId).getAccessRights() == RunAccessJDO.OWNER;
		} catch (Exception e) {
			return false;
		}

	}
}
