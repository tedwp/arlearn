package org.celstec.arlearn2.delegators.inventory;

import java.io.IOException;
import java.util.logging.Level;

import org.celstec.arlearn2.beans.run.InventoryRecord;
import org.celstec.arlearn2.cache.InventoryRecordCache;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.fusion.CSV;
import org.celstec.arlearn2.jdo.manager.InventoryRecordManager;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class CreateInventoryRecord extends GoogleDelegator {

	public CreateInventoryRecord(String authToken) throws AuthenticationException {
		super(authToken);
	}

	public CreateInventoryRecord(GoogleDelegator gd) {
		super(gd);
	}

	public InventoryRecord createInventoryRecord(InventoryRecord ir) {
		if (ir.getRunId() == null) {
			ir.setError("No run identifier specified");
			return ir;
		}
//		InventoryRecordCache.getInstance().removeInventoryRecords(ir.getRunId(), ir.getScope());
		ir = InventoryRecordManager.addInventoryRecord(ir.getRunId(), ir.getGeneralItemId(), ir.getScope(), ir.getEmail(), ir.getTeamId(), ir.getLat(), ir.getLng(), ir.getStatus(), ir.getTimestamp());
		InventoryRecordCache.getInstance().putInventoryRecordList(ir, ir.getRunId(), ir.getScope(), ir.getGeneralItemId(), ir.getEmail(), ir.getTeamId());
		return ir;
	}
	
	public void deleteInventryRecords(long runId) {
		InventoryRecordManager.deleteInventoryRecords(runId);
		//TODO empty cache, after migrate of cachemanager
	}


}
