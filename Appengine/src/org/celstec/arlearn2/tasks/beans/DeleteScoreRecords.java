package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.delegators.ActionDelegator;
import org.celstec.arlearn2.delegators.scoreRecord.CreateScoreRecord;

import com.google.gdata.util.AuthenticationException;

public class DeleteScoreRecords extends GenericBean {

	private Long runId;
	private String account;

	public DeleteScoreRecords() {
		super();
	}
	
	public DeleteScoreRecords(String token, Long runId) {
		super(token);
		this.runId = runId;
	}
	
	public DeleteScoreRecords(String token, Long runId, String account) {
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
			CreateScoreRecord csr = new CreateScoreRecord("auth"+getToken());
			if (getAccount() == null) {
				csr.deleteScoreRecords(getRunId());
			} else {
				csr.deleteScoreRecords(getRunId(), getAccount());
			}
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}		
	}

}
