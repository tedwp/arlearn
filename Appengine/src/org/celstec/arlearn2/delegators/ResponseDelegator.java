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

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.beans.run.ResponseList;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunAccess;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;
import org.celstec.arlearn2.jdo.manager.ResponseManager;

import com.google.gdata.util.AuthenticationException;

public class ResponseDelegator extends GoogleDelegator {

	public ResponseDelegator(Service service) {
		super(service);
	}
	
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

		ResponseManager.addResponse(r.getGeneralItemId(), r.getResponseValue(), run.getRunId(), r.getUserEmail(), r.getTimestamp());

        RunAccessDelegator rad = new RunAccessDelegator(this);
        NotificationDelegator nd = new NotificationDelegator();
        for (RunAccess ra :rad.getRunAccess(r.getRunId()).getRunAccess()){
            nd.broadcast(r, ra.getAccount());
        }
		return r;
	}

	public ResponseList getResponses(Long runId, Long itemId, String account) {
		ResponseList rl = new ResponseList();
		rl.setResponses(ResponseManager.getResponse(runId, itemId, account, null, false));
		return rl;
	}

    public ResponseList getResponsesFromUntil(Long runId, Long itemId, String fullId, Long from, Long until, String cursor) {
        return ResponseManager.getResponse(runId, itemId, fullId, from, until, cursor);
    }

    public ResponseList getResponsesFromUntil(Long runId, Long itemId, Long from, Long until, String cursor) {
        return ResponseManager.getResponse(runId, itemId, from, until, cursor);
    }

	public ResponseList getResponsesFromUntil(Long runId, Long from, Long until, String cursor) {
		return ResponseManager.getResponse(runId, from, until, cursor);
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
