package org.celstec.arlearn2.delegators.generalitems;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.cache.GeneralitemsCache;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.jdo.manager.GeneralItemManager;

import com.google.gdata.util.AuthenticationException;

public class CreateGeneralItems extends GoogleDelegator {

	public CreateGeneralItems(String authToken) throws AuthenticationException {
		super(authToken);
	}

	public CreateGeneralItems(GoogleDelegator gd) {
		super(gd);
	}

	public GeneralItem createGeneralItem(GeneralItem gi) {
		GeneralitemsCache.getInstance().removeGeneralItemList(gi.getGameId());
		GeneralItemManager.addGeneralItem(gi);
		return gi;
	}

	public void deleteGeneralItems(long gameId) {
		GeneralItemManager.deleteGeneralItem(gameId);
		GeneralitemsCache.getInstance().removeGeneralItemList(gameId);
	}
	
	//TODO add method to delete an individual item.

}
