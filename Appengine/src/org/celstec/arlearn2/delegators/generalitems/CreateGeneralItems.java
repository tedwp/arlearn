package org.celstec.arlearn2.delegators.generalitems;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.cache.GeneralitemsCache;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
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
	
	public GeneralItem deleteGeneralItem(long gameId, String itemId) {
		UsersDelegator qu = new UsersDelegator(this);
		String myAccount = qu.getCurrentUserAccount();
		QueryGeneralItems cgi = new QueryGeneralItems(this);
		GeneralItem gi = cgi.getGeneralItemForGame(gameId, Long.parseLong(itemId));
		GameDelegator gd = new GameDelegator(this);
		Game g = gd.getGame(gi.getGameId());
		if (!g.getOwner().equals(myAccount)) {
			gi = new GeneralItem();
			gi.setError("You are not the owner of this game");
			return gi;
		}
		GeneralItemManager.deleteGeneralItem(gameId, itemId);
		GeneralitemsCache.getInstance().removeGeneralItemList(gameId);
		return gi;
	}
	
}
