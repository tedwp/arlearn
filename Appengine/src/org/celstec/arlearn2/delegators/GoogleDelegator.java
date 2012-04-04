package org.celstec.arlearn2.delegators;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.celstec.arlearn2.fusion.CSV;
import org.celstec.arlearn2.fusion.FusionClient;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class GoogleDelegator {
	protected static final Logger logger = Logger.getLogger(GoogleDelegator.class.getName());
	
	protected String authToken;
//	protected FusionClient fc;

	public GoogleDelegator(String authToken) throws AuthenticationException {
		authToken = authToken.substring(authToken.indexOf("auth=")+5);
		this.authToken = authToken;
//		fc = new FusionClient(authToken);
	}
	
	public GoogleDelegator(GoogleDelegator gd) {
		this.authToken = gd.authToken;
//		this.fc = gd.fc;
	}
	
//	protected long getTableId(Long runId, String tableName) {
//		CSV csv;
//		try {
//			csv = fc.showTables();
//			if (csv.containsValue(csv.getColumnIndex("name"), tableName)) {
//				long cachedId = Long.parseLong(csv.getValue(csv.rowIdForValue("name", tableName), "table id"));
//				return cachedId;
//			}
//		} catch (IOException e) {
//			logger.log(Level.SEVERE, e.getMessage(), e);
//		} catch (ServiceException e) {
//			logger.log(Level.SEVERE, e.getMessage(), e);
//		}
//		return -1;
//	}
	
	public String getAuthToken() {
		return authToken;
	}

}
