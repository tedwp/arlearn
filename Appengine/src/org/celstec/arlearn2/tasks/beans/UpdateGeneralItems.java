package org.celstec.arlearn2.tasks.beans;

import java.util.logging.Logger;

import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.ActionDelegator;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.delegators.generalitems.QueryGeneralItems;

import com.google.gdata.util.AuthenticationException;

public class UpdateGeneralItems extends GenericBean{

	private Long runId;
	private String action;
	private String userEmail;
	
	public UpdateGeneralItems() {
		
	}
	
	public UpdateGeneralItems(String token, Long runId, String action, String userEmail) {
		super(token);
		this.runId = runId;
		this.action = action;
		this.userEmail = userEmail;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	
	private static final Logger log = Logger.getLogger(UpdateGeneralItems.class.getName());

	
        
        
	@Override
	public void run() {
		log.severe("update general items "+runId+ " "+getAction());
		UsersDelegator qu = null;
		User u = null;
		try {
			qu = new UsersDelegator("auth=" + getToken());
			u = qu.getUserByEmail(runId, getUserEmail());
		} catch (AuthenticationException e) {
			e.printStackTrace();
			log.severe("exception "+e.getMessage());
		}
		
		Action a = new Action();
		a.setRunId(runId);
		a.setAction(getAction());
		GeneralItemDelegator gid = new GeneralItemDelegator(qu);

//		QueryGeneralItems qgi = new QueryGeneralItems(qu);
		log.severe("before check action effect"+runId+ " "+getAction());

		gid.checkActionEffect(a, runId, u);
		
	}

}
