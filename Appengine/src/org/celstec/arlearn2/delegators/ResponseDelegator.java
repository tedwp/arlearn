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
package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.beans.run.ResponseList;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;
import org.celstec.arlearn2.jdo.manager.ResponseManager;

import com.google.gdata.util.AuthenticationException;

public class ResponseDelegator extends GoogleDelegator {

	public ResponseDelegator(String authtoken) throws AuthenticationException {
		super(authtoken);
	}
	
	public ResponseDelegator(GoogleDelegator gd) {
		super(gd);
	}
	
	public Response createResponse(Long runIdentifier, Response r){
		RunDelegator rd = new RunDelegator(this);
		Run run = rd.getRun(runIdentifier);
		if (run == null) {
			r.setError("invalid run identifier");
			return r;
		}
		UsersDelegator qu = new UsersDelegator(this);
		r.setUserEmail(qu.getCurrentUserAccount());
//		Long genItemId = r.getGeneralItemId();
//		String respValue = r.getResponseValue();
//		Long runId =run.getRunId();
//		String userEmail =r.getUserEmail();
//		Long timeStamp =r.getTimestamp();
//		System.out.println(ResponseManager.params[1]);
//		ResponseManager.addResponse(genItemId,respValue, runId, userEmail, timeStamp);
		ResponseManager.addResponse(r.getGeneralItemId(), r.getResponseValue(), run.getRunId(), r.getUserEmail(), r.getTimestamp());
		ChannelNotificator.getInstance().notify(run.getOwner(), r);

		return r;
	}

	public ResponseList getResponses(Long runId, Long itemId, String account) {
		ResponseList rl = new ResponseList();
		rl.setResponses(ResponseManager.getResponse(runId, itemId, account, null, false));
		return rl;
	}
	
	
	public Response revokeResponse(Response r) {
		return ResponseManager.revokeResponse(r.getRunId(), r.getGeneralItemId(), r.getUserEmail(), r.getTimestamp());
	}

	public void deleteResponses(Long runId) {
		ResponseManager.deleteResponses(runId, null);	
	}
	
	public void deleteResponses(Long runId, String email) {
		ResponseManager.deleteResponses(runId, email);
		
	}
}
