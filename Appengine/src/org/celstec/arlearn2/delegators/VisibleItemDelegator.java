package org.celstec.arlearn2.delegators;

import java.util.List;

import org.celstec.arlearn2.beans.run.VisibleItem;
import org.celstec.arlearn2.cache.VisibleItemsCache;
import org.celstec.arlearn2.delegators.generalitems.VisibleItemsList;

import org.celstec.arlearn2.jdo.manager.VisibleItemsManager;
import org.celstec.arlearn2.util.FusionCache;

import com.google.gdata.util.AuthenticationException;

public class VisibleItemDelegator extends GoogleDelegator {
	
	public VisibleItemDelegator(String authtoken) throws AuthenticationException {
		super(authtoken);
	}

	public VisibleItemDelegator(GoogleDelegator gd) {
		super(gd);
	}
	
	public VisibleItemsList getVisibleItems(Long runId, String itemId, String userEmail, String teamId) {
		List<VisibleItem> visibleItems = null;
		if (itemId == null) visibleItems = VisibleItemsCache.getInstance().getVisibleItems(runId, userEmail, teamId);
		if (visibleItems == null) {
			visibleItems = VisibleItemsManager.getVisibleItems(runId, itemId, userEmail, teamId);
			VisibleItemsCache.getInstance().putVisibleItems(visibleItems, runId, userEmail, teamId);
		}
		VisibleItemsList vil = new VisibleItemsList();
		vil.setVisibleItems(visibleItems);
		return vil;
	}
	
	public void addVisibleItem(Long runId, Long generalItemId, String email, String teamId) {
		VisibleItemsManager.addVisibleItem(runId, generalItemId, email, teamId);
		List<VisibleItem> visibleItems = VisibleItemsCache.getInstance().getVisibleItems(runId, email, teamId);
		if (visibleItems != null) {
			VisibleItemsCache.getInstance().removeVisibleItems(runId, email, teamId);
		}
	}
	
	public void deleteVisibleItems(Long runId) {
		VisibleItemsManager.deleteVisibleItems(runId);
		//TODO clean cache
		FusionCache.getInstance().clearAll();
	}
}
