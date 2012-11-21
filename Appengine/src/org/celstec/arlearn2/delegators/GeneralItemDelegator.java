package org.celstec.arlearn2.delegators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.dependencies.ActionDependency;
import org.celstec.arlearn2.beans.dependencies.AndDependency;
import org.celstec.arlearn2.beans.dependencies.BooleanDependency;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.dependencies.OrDependency;
import org.celstec.arlearn2.beans.dependencies.TimeDependency;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
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
import org.celstec.arlearn2.cache.VisibleGeneralItemsCache;
import org.celstec.arlearn2.delegators.generalitems.VisibleItemsList;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;
import org.celstec.arlearn2.jdo.manager.GeneralItemManager;
import org.celstec.arlearn2.jdo.manager.GeneralItemVisibilityManager;
import org.celstec.arlearn2.tasks.beans.NotifyRunsFromGame;
import org.htmlparser.util.Translate;

import com.google.gdata.util.AuthenticationException;

public class GeneralItemDelegator extends GoogleDelegator {

	public GeneralItemDelegator(String authToken) throws AuthenticationException {
		super(authToken);
	}

	public GeneralItemDelegator(GoogleDelegator gd) {
		super(gd);
	}

	public GeneralItem createGeneralItem(GeneralItem gi) {
		if (gi.getDescription() != null) gi.setDescription(Translate.decode(gi.getDescription()).replaceAll("\\<.*?>",""));
		GeneralitemsCache.getInstance().removeGeneralItemList(gi.getGameId());
		
		gi.setDeleted(false);
		GeneralItemManager.addGeneralItem(gi);
		(new NotifyRunsFromGame(authToken, gi.getGameId(), gi, GeneralItemModification.CREATED)).scheduleTask();
		if (gi.getDependsOn() != null) {
			GeneralItemVisibilityManager.delete(null, gi.getId(), null, null);	
		} 		
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

	public GeneralItemList getAllGeneralItems(Long runIdentifier) {
		RunDelegator qr = new RunDelegator(this);
		Run run = qr.getRun(runIdentifier);
		return getGeneralItems(run.getGameId());
	}

	public GeneralItemList getGeneralItemsRun(Long runIdentifier) {
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
		// TODO run does not exist: return a proper exception
		return getGeneralItems(run.getGameId());
	}

	@Deprecated
	public GeneralItemList getNonPickableItems(Long runIdentifier, String userIdentifier) {
		GeneralItemList returnItemList = getGeneralItemsRun(runIdentifier);
		List<GeneralItem> gl = returnItemList.getGeneralItems();
		for (int i = gl.size() - 1; i >= 0; i--) {
			if (gl.get(i) instanceof PickupItem) {
				gl.remove(i);
				continue;
			}
		}
		return returnItemList;
	}
	
	public GeneralItemList getVisibleItems(Long runIdentifier, String userIdentifier) {
		GeneralItemList gil = VisibleGeneralItemsCache.getInstance().getVisibleGeneralitems(runIdentifier, userIdentifier);
		if (gil == null) {
			gil = new GeneralItemList();
			RunDelegator qr = new RunDelegator(this);
			Run run = qr.getRun(runIdentifier);
			if (run == null) {
				GeneralItemList il = new GeneralItemList();
				il.setError("run not found");
				il.setErrorCode(Bean.RUNNOTFOUND);
				return il;
			}
			
			List<Long> visibleItemIds = GeneralItemVisibilityManager.getVisibleItems(runIdentifier, userIdentifier);
			GeneralItemList generalItemList = getGeneralItems(run.getGameId());

			for (Iterator<GeneralItem> iterator = generalItemList.getGeneralItems().iterator(); iterator.hasNext();) {
				GeneralItem item = iterator.next();
				if (item.getDeleted() == null || !item.getDeleted())
				if (visibleItemIds.contains(item.getId())) {
					gil.addGeneralItem(item); 
				}
				
			}
			VisibleGeneralItemsCache.getInstance().putVisibleGeneralItemList(gil, runIdentifier, userIdentifier);
		}
		return gil;
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
			if (influencedBy(generalItem, action) && isVisible(generalItem, al, u)) {
				GeneralItemModification gim = new GeneralItemModification();
				gim.setModificationType(GeneralItemModification.VISIBLE);
				gim.setRunId(runId);
				gim.setGeneralItem(generalItem);
				GeneralItemVisibilityManager.setItemVisible(gim.getGeneralItem().getId(), runId, u.getEmail(), GeneralItemVisibilityManager.VISIBLE_STATUS);

				ChannelNotificator.getInstance().notify(u.getEmail(), gim);
//				if ("answer_300_mct11.2_correct".equals(action.getAction())){
//					ChannelNotificator.getInstance().notify(u.getEmail().replace("mobilea", "mobileb"), gim);
//					ChannelNotificator.getInstance().notify(u.getEmail().replace("mobilea", "mobilec"), gim);
//				}
				
			}
		}

	}
	
	private boolean influencedBy(GeneralItem gi, Action action) {
		boolean result = false;
		if (gi.getDependsOn() != null) result = result || influencedBy (gi.getDependsOn(), action);
		return result;
	}

	private boolean influencedBy(Dependency dependsOn, Action action) {
		if (dependsOn == null) return false;
		if (dependsOn instanceof ActionDependency) {
			if (action.getAction() == null) return false;
			return ((ActionDependency)dependsOn).getAction().equals(action.getAction());
		}
		if (dependsOn instanceof TimeDependency) return influencedBy(((TimeDependency)dependsOn).getOffset(), action);
		if (dependsOn instanceof BooleanDependency) {
			BooleanDependency bd = (BooleanDependency) dependsOn;
			boolean result = false;
			for (Dependency d: bd.getDependencies()){
				result = result || influencedBy(d, action);
			}
			return result;
		}
		return false;
	}

	public boolean isVisible(GeneralItem gi, ActionList al, User u) {
		if (gi.getDependsOn() == null )
			return gi.timeStampCheck();
		UsersDelegator ud = new UsersDelegator(this);
		HashMap<String, User> uMap = ud.getUserMap(u.getRunId());
		Dependency dep = gi.getDependsOn();
//		String role = dep.getRole();
//		if (role != null && !hasRole(u, role)) return false;
//		Integer scope = dep.getScope();
//		if (scope == null) scope = Dependency.USER_SCOPE;
//		if (dep instanceof ActionDependency) {
//			if (checkActions(((ActionDependency) dep), al, u, uMap))
//				return gi.timeStampCheck();
//		}
		if (checkActions(dep, al, u, uMap))
			return gi.timeStampCheck();
		return false;
	}

	private boolean hasRole(User u, String role) {
		if (u.getRoles() == null) return false;
		for (String r: u.getRoles()) {
			if (role.equals(r)) return true;
		}
		return false;
	}

	public boolean checkActions(Dependency dep, ActionList al, User u, HashMap<String, User> uMap) {
		if (dep instanceof ActionDependency) {
			return checkActions(((ActionDependency) dep), al, u, uMap);
		}
		if (dep instanceof AndDependency) {
			return checkActions(((AndDependency) dep), al, u, uMap);
		}
		if (dep instanceof OrDependency) {
			return checkActions(((OrDependency) dep), al, u, uMap);
		}
		return false;
	}
	public boolean checkActions(ActionDependency dOn, ActionList al, User u, HashMap<String, User> uMap) {
		Iterator<Action> it = al.getActions().iterator();
		while (it.hasNext()) {
			if (checkAction(dOn, (Action) it.next(),u, uMap))
				return true;
		}
		return false;
	}
	
	public boolean checkActions(AndDependency andDep, ActionList al, User u, HashMap<String, User> uMap) {
		boolean result = true;
		for (Dependency dOn : andDep.getDependencies()) {
				result = result && checkActions(dOn, al, u, uMap);
			if (!result) return false;
		}
		return result;
	}
	
	public boolean checkActions(OrDependency orDep, ActionList al, User u, HashMap<String, User> uMap) {
		boolean result = false;
		for (Dependency dOn : orDep.getDependencies()) {
				result = result || checkActions(dOn, al, u, uMap);
			if (result) return true;
		}
		return result;
	}

	public boolean checkAction(ActionDependency dOn, Action a, User u, HashMap<String, User> uMap) {
		if (a == null) return false;
//		UsersDelegator ud = new UsersDelegator(this);
//		User actionUser = ud.getUserByEmail(u.getRunId(), a.getUserEmail());
		Integer scope = dOn.getScope();
		if (scope == null) scope = Dependency.USER_SCOPE;
		switch (scope) {
		case Dependency.USER_SCOPE:
			if (!a.getUserEmail().equals(u.getEmail())) return false;
			break;
		case Dependency.TEAM_SCOPE:
			if (!uMap.get(a.getUserEmail()).getTeamId().equals(u.getTeamId())) return false;
			break;

		default:
			break;
		}
		String role = dOn.getRole();
		if (role != null && !hasRole(uMap.get(a.getUserEmail()), role)) return false;
		
		if (dOn.getAction() != null && !dOn.getAction().equals(a.getAction()))
			return false;
		if (dOn.getGeneralItemId() != null && !dOn.getGeneralItemId().equals(a.getGeneralItemId()))
			return false;
		if (dOn.getGeneralItemType() != null && !dOn.getGeneralItemType().equals(a.getGeneralItemType()))
			return false;
		return true;
	}

	public List<GeneralItem> getNonVisibleItems(GeneralItemList giList, VisibleItemsList vil) {
		List<GeneralItem> returnItems = new ArrayList();
		for (GeneralItem item : giList.getGeneralItems()) {
			if (vil.getVisibleItem(item.getId()) == null)
				returnItems.add(item);
		}
		return returnItems;

	}

}
