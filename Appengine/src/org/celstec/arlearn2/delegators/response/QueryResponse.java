package org.celstec.arlearn2.delegators.response;

import java.io.IOException;
import java.util.logging.Logger;

import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.fusion.CSV;
import org.celstec.arlearn2.util.ResponseCache;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class QueryResponse extends GoogleDelegator {

	public QueryResponse(GoogleDelegator gd) {
		super(gd);
	}

	public QueryResponse(String authtoken) throws AuthenticationException {
		super(authtoken);
	}

//	public long getResponseTableIdForRun(Long runId) {
//		long tableid = ResponseCache.getInstance().getResponseTableIdForRun(runId);
//		if (tableid == -1) {
//			tableid = getTableId(runId, Response.TABLE_NAME + "_" + runId);
//			if (tableid != -1)
//				ResponseCache.getInstance().putResponseTableIdForRun(runId, tableid);
//		}
//		return tableid;
//	}

	
}
