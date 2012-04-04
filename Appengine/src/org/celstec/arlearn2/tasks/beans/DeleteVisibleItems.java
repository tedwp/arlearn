package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.delegators.VisibleItemDelegator;

import com.google.gdata.util.AuthenticationException;

public class DeleteVisibleItems extends GenericBean {
	private Long runId;

	public DeleteVisibleItems() {
		super();
	}
	
	public DeleteVisibleItems(String token, Long runId) {
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
		VisibleItemDelegator vid;
		try {
			vid = new VisibleItemDelegator("auth="+getToken());
			vid.deleteVisibleItems(getRunId());
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		
	}
	
}
