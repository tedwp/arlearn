package org.celstec.arlearn2.delegators.response;

import java.io.IOException;
import java.util.logging.Level;

import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.fusion.CSV;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.jdo.manager.ResponseManager;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class CreateResponse extends GoogleDelegator {
	
	public CreateResponse(String authtoken) throws AuthenticationException {
		super(authtoken);
	}
	
//	public Response createResponse(Long runIdentifier, Response r){
//		RunDelegator rd = new RunDelegator(this);
//		Run run = rd.getRun(runIdentifier);
//		if (run == null) {
//			r.setError("invalid run identifier");
//			return r;
//		}
//		ResponseManager.addResponse(r.getGeneralItemId(), r.getResponseValue(), run.getRunId(), r.getUserEmail(), r.getTimestamp());
//		return r;
//	}

}
