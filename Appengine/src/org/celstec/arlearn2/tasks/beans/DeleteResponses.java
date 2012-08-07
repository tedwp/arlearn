package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.delegators.ResponseDelegator;
import org.celstec.arlearn2.delegators.TeamsDelegator;
import org.celstec.arlearn2.jdo.manager.ResponseManager;

import com.google.gdata.util.AuthenticationException;

public class DeleteResponses extends GenericBean {

	private Long runId;
	private String account;

	public DeleteResponses() {
		super();
	}

	public DeleteResponses(String token, Long runId) {
		super(token);
		this.runId = runId;
	}

	public DeleteResponses(String token, Long runId, String account) {
		super(token);
		this.runId = runId;
		this.account = account;
	}
	
	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	@Override
	public void run() {
		try {
			ResponseDelegator rd = new ResponseDelegator(getToken());
			if (account == null) {
				rd.deleteResponses(runId);
			} else{
				rd.deleteResponses(runId, getAccount());

			}
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
	}
}
