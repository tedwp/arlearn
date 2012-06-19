package org.celstec.arlearn2.delegators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.generalItem.PickupItem;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ActionList;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.cache.GeneralitemsCache;
import org.celstec.arlearn2.delegators.generalitems.VisibleItemsList;
import org.celstec.arlearn2.jdo.manager.GeneralItemManager;
import org.celstec.arlearn2.tasks.beans.NotifyRunsFromGame;

import com.google.gdata.util.AuthenticationException;

public class GeneralItemDelegator extends GoogleDelegator {

	public GeneralItemDelegator(String authToken) throws AuthenticationException {
		super(authToken);
	}

	public GeneralItemDelegator(GoogleDelegator gd) {
		super(gd);
	}

	public GeneralItem createGeneralItem(GeneralItem gi) {
		GeneralitemsCache.getInstance().removeGeneralItemList(gi.getGameId());
		gi.setDeleted(false);
		GeneralItemManager.addGeneralItem(gi);
		(new NotifyRunsFromGame(authToken, gi.getGameId(), gi, GeneralItemModification.CREATED)).scheduleTask();

		return gi;
	}
	
	public void deleteGeneralItems(long gameId) {
		GeneralItemManager.deleteGeneralItem(gameId);
		GeneralitemsCache.getInstance().removeGeneralItemList(gameId);
	}
	
	public GeneralItem deleteGeneralItem(long gameId, String itemId) {
		UsersDelegator qu = new UsersDelegator(this);
		String myAccount = qu.getCurrentUserAccount();
		GeneralItem gi = getGeneralItemForGame(gameId, Long.parseLong(itemId));
		GameDelegator gd = new GameDelegator(this);
		Game g = gd.getGame(gi.getGameId());
		if (!g.getOwner().equals(myAccount)) {
			gi = new GeneralItem();
			gi.setError("You are not the owner of this game");
			return gi;
		}
		GeneralItemManager.setStatusDeleted(gameId, itemId);
		GeneralitemsCache.getInstance().removeGeneralItemList(gameId);
		(new NotifyRunsFromGame(authToken, gi.getGameId(), gi, GeneralItemModification.DELETED)).scheduleTask();

		return gi;
	}
	
	public GeneralItemList getGeneralItems(Long gameId) {
		GeneralItemList gil = GeneralitemsCache.getInstance().getGeneralitems(gameId, null, null);
		if (gil == null) {
			gil = new GeneralItemList();
			gil.setGeneralItems(GeneralItemManager.getGeneralitems(gameId, null, null));
			GeneralitemsCache.getInstance().putGeneralItemList(gil, gameId, null, null);
		}
		return gil;
	}
	
	private GeneralItemList getAllGeneralItems(Long runIdentifier) {
		RunDelegator qr = new RunDelegator(this);
		Run run = qr.getRun(runIdentifier);
		return getGeneralItems(run.getGameId());
	}
	
	public GeneralItemList getGeneralItems(Long runIdentifier, String userIdentifier) {
		RunDelegator qr = new RunDelegator(this);
		Run run = qr.getRun(runIdentifier);
		if (run == null) {
			GeneralItemList il = new GeneralItemList();
			il.setError("run not found");
			il.setErrorCode(Bean.RUNNOTFOUND);
			return il;
		}
		GeneralItemList returnItemList = getGeneralItems(run.getGameId());
		
		List<GeneralItem> gl = returnItemList.getGeneralItems();
		long runDuration = (qr).getRunDuration(runIdentifier);
		for (int i = gl.size() - 1; i >= 0; i--) {
			if (gl.get(i).getShowAtTimeStamp() != null && gl.get(i).getShowAtTimeStamp() > runDuration) {
				gl.remove(i);
				continue;
			}
		}
		return returnItemList;
	}
	
	public GeneralItem getGeneralItem(Long runIdentifier, Long generalItemId) { 
		// TODO:better do a DB query by id																			 
		RunDelegator rd = new RunDelegator(this);
		Run run = rd.getRun(runIdentifier);
		return getGeneralItemForGame(run.getGameId(), generalItemId);
	}
	
	public GeneralItem getGeneralItemForGame(Long gameId, Long generalItemId) { 
		// TODO:better do a DB query by id																			 
		GeneralItemList returnItemList = getGeneralItems(gameId);
		List<GeneralItem> gl = returnItemList.getGeneralItems();
		for (int i = gl.size() - 1; i >= 0; i--) {
			if (gl.get(i).getId().equals(generalItemId)) {
				return gl.get(i);
			}
		}
		return null;
	}
	
	public GeneralItemList getNonPickableItemsAll(Long runIdentifier) {
		RunDelegator qr = new RunDelegator(this);
		Run run = qr.getRun(runIdentifier);
		//TODO run does not exist: return a proper exception
		return getGeneralItems(run.getGameId());
	}
	
	public GeneralItemList getNonPickableItems(Long runIdentifier, String userIdentifier) {
		GeneralItemList returnItemList = getGeneralItems(runIdentifier, userIdentifier);
		List<GeneralItem> gl = returnItemList.getGeneralItems();
		for (int i = gl.size() - 1; i >= 0; i--) {
			if (gl.get(i) instanceof PickupItem) {
				gl.remove(i);
				continue;
			}
		}
		return returnItemList;
	}
	
	public void checkActionEffect(Action action, long runId, User u) {
		if (u == null) {
			return;
		}
		ActionDelegator qa = new ActionDelegator(this);
		ActionList al = qa.getActionList(runId);
		VisibleItemDelegator vid = new VisibleItemDelegator(this);
		VisibleItemsList vil = vid.getVisibleItems(runId, null, u.getEmail(), u.getTeamId());
		vil.merge(vid.getVisibleItems(runId, null, null, u.getTeamId()));
		vil.merge(vid.getVisibleItems(runId, null, u.getEmail(), null));
		
		

		Iterator<GeneralItem> it = getNonVisibleItems(getAllGeneralItems(runId), vil).iterator();
		while (it.hasNext()) {
			GeneralItem generalItem = (GeneralItem) it.next();
			//TODO
//			if (isVisible(generalItem, al)) {
//				System.out.println("is from now on visible");
//				log.severe(generalItem.getId()+" is visible");
//				String email = null;
//				String teamId = null;
//				if ("user".equals(generalItem.getScope())) email = u.getEmail();
//				if ("team".equals(generalItem.getScope())) teamId = u.getTeamId();
//				vid.addVisibleItem(runId, generalItem.getId(), email, teamId);
//				(new NotifyItemVisible(authToken, runId, generalItem)).scheduleTask();
//			}
		}

	}

		 public List<GeneralItem> getNonVisibleItems(GeneralItemList giList, VisibleItemsList vil) {
	         List<GeneralItem> returnItems = new ArrayList();
	         for (GeneralItem item : giList.getGeneralItems()) {
	                 if (vil.getVisibleItem(item.getId())==null) returnItems.add(item);
	         }
	         return returnItems;
	         
		 }
	
}