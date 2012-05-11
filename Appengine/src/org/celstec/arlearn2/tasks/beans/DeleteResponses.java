package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.delegators.ResponseDelegator;
import org.celstec.arlearn2.delegators.TeamsDelegator;
import org.celstec.arlearn2.jdo.manager.ResponseManager;

import com.google.gdata.util.AuthenticationException;

public class DeleteResponses extends GenericBean {

	private Long runId;
	public DeleteResponses() {
		super();
	}

	public DeleteResponses(String token, Long runId) {
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
			ResponseDelegator rd = new ResponseDelegator(getToken());
			rd.deleteResponses(runId);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
	}
}