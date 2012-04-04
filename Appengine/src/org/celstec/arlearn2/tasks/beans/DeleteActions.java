package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.delegators.ActionDelegator;

import com.google.gdata.util.AuthenticationException;

public class DeleteActions extends GenericBean {
	
	private Long runId;

	public DeleteActions() {
		super();
	}
	
	public DeleteActions(String token, Long runId) {
		super(token);
		this.runId = runId;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	@Override
	public void run() {
		try {
			ActionDelegator ad = new ActionDelegator("auth"+getToken());
			ad.deleteActions(getRunId());
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
	}
}
