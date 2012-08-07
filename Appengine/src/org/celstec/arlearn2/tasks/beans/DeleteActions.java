package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.delegators.ActionDelegator;

import com.google.gdata.util.AuthenticationException;

public class DeleteActions extends GenericBean {
	
	private Long runId;
	private String account;

	public DeleteActions() {
		super();
	}
	
	public DeleteActions(String token, Long runId) {
		super(token);
		this.runId = runId;
	}
	
	public DeleteActions(String token, Long runId, String account) {
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
			ActionDelegator ad = new ActionDelegator("auth"+getToken());
			if (account == null) ad.deleteActions(getRunId());
			ad.deleteActions(getRunId(), getAccount());
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
	}
}
