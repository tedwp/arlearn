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
