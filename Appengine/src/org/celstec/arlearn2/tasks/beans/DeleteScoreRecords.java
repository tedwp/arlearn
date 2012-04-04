package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.delegators.ActionDelegator;
import org.celstec.arlearn2.delegators.scoreRecord.CreateScoreRecord;

import com.google.gdata.util.AuthenticationException;

public class DeleteScoreRecords extends GenericBean {

	private Long runId;
	public DeleteScoreRecords() {
		super();
	}
	
	public DeleteScoreRecords(String token, Long runId) {
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
			CreateScoreRecord csr = new CreateScoreRecord("auth"+getToken());
			csr.deleteScoreRecords(getRunId());
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}		
	}

}
