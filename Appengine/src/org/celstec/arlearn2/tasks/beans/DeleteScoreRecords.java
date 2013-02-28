/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
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
