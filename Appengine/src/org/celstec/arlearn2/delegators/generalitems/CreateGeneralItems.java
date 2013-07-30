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
package org.celstec.arlearn2.delegators.generalitems;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.cache.GeneralitemsCache;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.jdo.manager.GeneralItemManager;
import org.celstec.arlearn2.tasks.beans.NotifyRunsFromGame;
import org.celstec.arlearn2.tasks.beans.UpdateGeneralItems;

import com.google.gdata.util.AuthenticationException;

public class CreateGeneralItems extends GoogleDelegator {

	public CreateGeneralItems(String authToken) throws AuthenticationException {
		super(authToken);
	}

	public CreateGeneralItems(GoogleDelegator gd) {
		super(gd);
	}

//	public GeneralItem createGeneralItem(GeneralItem gi) {
//		GeneralitemsCache.getInstance().removeGameVariablesCollector(gi.getGameId());
//		GeneralItemManager.addGeneralItem(gi);
//		(new NotifyRunsFromGame(authToken, gi.getGameId(), gi, GeneralItemModification.CREATED)).scheduleTask();
//
//		return gi;
//	}

//	public void deleteGeneralItems(long gameId) {
//		GeneralItemManager.deleteGeneralItem(gameId);
//		GeneralitemsCache.getInstance().removeGameVariablesCollector(gameId);
//	}
//	
//	public GeneralItem deleteGeneralItem(long gameId, String itemId) {
//		UsersDelegator qu = new UsersDelegator(this);
//		String myAccount = qu.getCurrentUserAccount();
//		QueryGeneralItems cgi = new QueryGeneralItems(this);
//		GeneralItem gi = cgi.getGeneralItemForGame(gameId, Long.parseLong(itemId));
//		GameDelegator gd = new GameDelegator(this);
//		Game g = gd.getGame(gi.getGameId());
//		if (!g.getOwner().equals(myAccount)) {
//			gi = new GeneralItem();
//			gi.setError("You are not the owner of this game");
//			return gi;
//		}
//		GeneralItemManager.deleteGeneralItem(gameId, itemId);
//		GeneralitemsCache.getInstance().removeGameVariablesCollector(gameId);
//		(new NotifyRunsFromGame(authToken, gi.getGameId(), gi, GeneralItemModification.DELETED)).scheduleTask();
//
//		return gi;
//	}
	
}
