package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.beans.run.ResponseList;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.jdo.manager.ResponseManager;

import com.google.gdata.util.AuthenticationException;

public class ResponseDelegator extends GoogleDelegator {

	public ResponseDelegator(String authtoken) throws AuthenticationException {
		super(authtoken);
	}
	
	public Response createResponse(Long runIdentifier, Response r){
		RunDelegator rd = new RunDelegator(this);
		Run run = rd.getRun(runIdentifier);
		if (run == null) {
			r.setError("invalid run identifier");
			return r;
		}
//		Long genItemId = r.getGeneralItemId();
//		String respValue = r.getResponseValue();
//		Long runId =run.getRunId();
//		String userEmail =r.getUserEmail();
//		Long timeStamp =r.getTimestamp();
//		System.out.println(ResponseManager.params[1]);
//		ResponseManager.addResponse(genItemId,respValue, runId, userEmail, timeStamp);
		ResponseManager.addResponse(r.getGeneralItemId(), r.getResponseValue(), run.getRunId(), r.getUserEmail(), r.getTimestamp());
		return r;
	}

	public ResponseList getResponses(Long runId, String account) {
		ResponseList rl = new ResponseList();
		rl.setResponses(ResponseManager.getResponse(runId, null, account, null, false));
//		Response r = new Response();
//		r.setUserEmail("arlearn1");
//		r.setResponseValue("this is the value");
//		r.setGeneralItemId(801l);
//		rl.addResponse(r);
		return rl;
	}
	
	public Response revokeResponse(Response r) {
		return ResponseManager.revokeResponse(r.getRunId(), r.getGeneralItemId(), r.getUserEmail(), r.getTimestamp());
	}

	public void deleteResponses(Long runId) {
		ResponseManager.deleteResponses(runId);
		
	}
}
