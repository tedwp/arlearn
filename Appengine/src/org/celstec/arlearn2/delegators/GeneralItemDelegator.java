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
package org.celstec.arlearn2.delegators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.dependencies.ActionDependency;
import org.celstec.arlearn2.beans.dependencies.AndDependency;
import org.celstec.arlearn2.beans.dependencies.BooleanDependency;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.dependencies.OrDependency;
import org.celstec.arlearn2.beans.dependencies.TimeDependency;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GamesList;
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
import org.celstec.arlearn2.tasks.beans.GeneralItemSearchIndex;
import org.celstec.arlearn2.tasks.beans.NotifyRunsFromGame;
import org.htmlparser.util.Translate;

import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;
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
		GeneralItemSearchIndex.scheduleGiTask(gi);
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
		gil.setServerTime(System.currentTimeMillis());
		return gil;
	}
	
	public GeneralItemList getGeneralItems(Long gameId, Long from, Long until) {
		GeneralItemList gil = new GeneralItemList();
		gil.setGeneralItems(GeneralItemManager.getGeneralitemsFromUntil(gameId, from, until));
		gil.setServerTime(System.currentTimeMillis());
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
//		for (int i = gl.size() - 1; i >= 0; i--) {
//			if (gl.get(i).getShowAtTimeStamp() != null && gl.get(i).getShowAtTimeStamp() > runDuration) {
//				gl.remove(i);
//				continue;
//			}
//		}
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
	
	public GeneralItemList getItems(Long runIdentifier, String userIdentifier, Integer status) {
		GeneralItemList gil = VisibleGeneralItemsCache.getInstance().getVisibleGeneralitems(runIdentifier, userIdentifier, status);
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
			
			HashMap<Long, Long> visibleItemIds = GeneralItemVisibilityManager.getItems(runIdentifier, userIdentifier, status);
			GeneralItemList generalItemList = getGeneralItems(run.getGameId());

			for (Iterator<GeneralItem> iterator = generalItemList.getGeneralItems().iterator(); iterator.hasNext();) {
				GeneralItem item = iterator.next();
				if (item.getDeleted() == null || !item.getDeleted())
				if (visibleItemIds.containsKey(item.getId())) {
					if (status == GeneralItemVisibilityManager.VISIBLE_STATUS) {
						item.setVisibleAt(visibleItemIds.get(item.getId()));
					}
					if (status == GeneralItemVisibilityManager.DISAPPEARED_STATUS) {
						item.setDisappearAt(visibleItemIds.get(item.getId()));
					}
					gil.addGeneralItem(item); 
				}
				
			}
			VisibleGeneralItemsCache.getInstance().putVisibleGeneralItemList(gil, runIdentifier, userIdentifier, status);
		}
		return gil;
	}

	
	

	public void checkActionEffect(Action action, long runId, User u) {
		if (u == null) {
			return;
		}
		ActionDelegator qa = new ActionDelegator(this);
		ActionList al = qa.getActionList(runId);
		GeneralItemDelegator gid = new GeneralItemDelegator(this);
		GeneralItemList visableGIs =  gid.getItems(runId, u.getEmail(), GeneralItemVisibilityManager.VISIBLE_STATUS);
		GeneralItemList disappearedGIs =  gid.getItems(runId, u.getEmail(), GeneralItemVisibilityManager.DISAPPEARED_STATUS);
		
//		VisibleItemDelegator vid = new VisibleItemDelegator(this);
		
//		VisibleItemsList vil = vid.getVisibleItems(runId, null, u.getEmail(), u.getTeamId());
//		vil.merge(vid.getVisibleItems(runId, null, null, u.getTeamId()));
//		vil.merge(vid.getVisibleItems(runId, null, u.getEmail(), null));

		List<GeneralItem> nonVisibleItems = getNonVisibleItems(getAllGeneralItems(runId), visableGIs);
		Iterator<GeneralItem> it = nonVisibleItems.iterator();
		while (it.hasNext()) {
			GeneralItem generalItem = (GeneralItem) it.next();
			long visAt;
			if (influencedByAppear(generalItem, action) && (visAt = isVisible(generalItem, al, u))!=-1 && itemMatchesUserRoles(generalItem, u.getRoles())) {
				GeneralItemModification gim = new GeneralItemModification();
				gim.setModificationType(GeneralItemModification.VISIBLE);
				gim.setRunId(runId);
				gim.setGameId(generalItem.getGameId());
				gim.setGeneralItem(generalItem);
				gim.setItemId(generalItem.getId());
				generalItem.setVisibleAt(visAt);
				GeneralItemVisibilityManager.setItemVisible(gim.getGeneralItem().getId(), runId, u.getEmail(), GeneralItemVisibilityManager.VISIBLE_STATUS, visAt);

				ChannelNotificator.getInstance().notify(u.getEmail(), gim);
			}
			
		}
		List<GeneralItem> notDisappearedItems =  getNotDisappearedItems(getAllGeneralItems(runId), disappearedGIs);
		it = notDisappearedItems.iterator();
		while (it.hasNext()) {
			GeneralItem generalItem = (GeneralItem) it.next();
			long disAt;
			if (influencedByDisappear(generalItem, action) && ( disAt =hasDisappeared(generalItem, al, u)) != -1) {
				GeneralItemModification gim = new GeneralItemModification();
				gim.setModificationType(GeneralItemModification.DISAPPEARED);
				gim.setRunId(runId);
				gim.setGameId(generalItem.getGameId());
				gim.setItemId(generalItem.getId());
				gim.setGeneralItem(generalItem);
				generalItem.setDisappearAt(disAt);
				
				GeneralItemVisibilityManager.setItemVisible(gim.getGeneralItem().getId(), runId, u.getEmail(), GeneralItemVisibilityManager.DISAPPEARED_STATUS, disAt);
				ChannelNotificator.getInstance().notify(u.getEmail(), gim);	
			}	
		}
	}
	
	public static boolean itemMatchesUserRoles(GeneralItem generalItem, List<String> list) {
		if (generalItem.getRoles() == null) return true;
		if (generalItem.getRoles().isEmpty()) return true;
		for (String itemRole: generalItem.getRoles()) {
			if (!userRoleListContainsRole(list, itemRole)) return false;
		}
		return true;
	}
	
	private static  boolean userRoleListContainsRole(List<String> list, String itemRole) {
		for (String userRole: list) {
			if (itemRole.equalsIgnoreCase(userRole)) return true;
		}
		return false;
	}
	
	private boolean influencedByAppear(GeneralItem gi, Action action) {
		boolean result = false;
		if (gi.getDependsOn() != null) result = result || influencedBy (gi.getDependsOn(), action);
		return result;
	}
	
	private boolean influencedByDisappear(GeneralItem gi, Action action) {
		boolean result = false;
		if (gi.getDisappearOn() != null) result = result || influencedBy (gi.getDisappearOn(), action);
		return result;
	}

	private boolean influencedBy(Dependency dependsOn, Action action) {
		if (dependsOn == null) return false;
		if (dependsOn instanceof ActionDependency) {
			if (((ActionDependency)dependsOn).getAction() == null && action.getAction() != null) return false;
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

	public long isVisible(GeneralItem gi, ActionList al, User u) {
		if (gi.getDependsOn() == null )
			return 0l;
		UsersDelegator ud = new UsersDelegator(this);
		HashMap<String, User> uMap = ud.getUserMap(u.getRunId());
		Dependency dep = gi.getDependsOn();
		return checkActions(dep, al, u, uMap);
			
	}
	
	public long hasDisappeared(GeneralItem gi, ActionList al, User u) {
		Dependency dep = gi.getDisappearOn();
		if (dep == null )
			return 0l;
		UsersDelegator ud = new UsersDelegator(this);
		HashMap<String, User> uMap = ud.getUserMap(u.getRunId());
		return checkActions(dep, al, u, uMap);

	}


	private boolean hasRole(User u, String role) {
		if (u.getRoles() == null) return false;
		for (String r: u.getRoles()) {
			if (role.equals(r)) return true;
		}
		return false;
	}

	public long checkActions(Dependency dep, ActionList al, User u, HashMap<String, User> uMap) {
		if (dep instanceof ActionDependency) {
			return checkActions(((ActionDependency) dep), al, u, uMap);
		}
		if (dep instanceof AndDependency) {
			return checkActions(((AndDependency) dep), al, u, uMap);
		}
		if (dep instanceof OrDependency) {
			return checkActions(((OrDependency) dep), al, u, uMap);
		}
		if (dep instanceof TimeDependency) {
			return checkActions(((TimeDependency) dep), al, u, uMap);
		}
		return -1;
	}
	public long checkActions(ActionDependency dOn, ActionList al, User u, HashMap<String, User> uMap) {
		Iterator<Action> it = al.getActions().iterator();
		long minTime = -1;
		while (it.hasNext()) {
			long actionCheck = checkAction(dOn, (Action) it.next(),u, uMap); 
			if (actionCheck != -1) {
				if (minTime == -1) {
					minTime = actionCheck;
				} else {
					minTime = Math.min(minTime, actionCheck);
				}
			}
		}
		return minTime;
	}
	
	public long checkActions(TimeDependency dOn, ActionList al, User u, HashMap<String, User> uMap) {
		if (dOn.getOffset() == null|| dOn.getTimeDelta() == null) return -1;
		long time = checkActions(((ActionDependency) dOn.getOffset()), al, u, uMap);
		if (time == -1) return -1;
		return checkActions(((ActionDependency) dOn.getOffset()), al, u, uMap) + dOn.getTimeDelta();
	}
	
	public long checkActions(AndDependency andDep, ActionList al, User u, HashMap<String, User> uMap) {
		boolean result = true;
		long maxTime = -1;
		for (Dependency dOn : andDep.getDependencies()) {
			long time = checkActions(dOn, al, u, uMap);
			result = result && (time != -1); 
			if (!result) return -1;
			if (maxTime == -1) {
				maxTime = time;
			} else {
				maxTime = Math.max(maxTime, time);
			}
		}
		return maxTime;
	}
	
	public long checkActions(OrDependency orDep, ActionList al, User u, HashMap<String, User> uMap) {
		boolean result = false;
		long minTime = -1;
		for (Dependency dOn : orDep.getDependencies()) {
			long time = checkActions(dOn, al, u, uMap);
			if (time != -1) {
				result = true;
				if (minTime == -1) {
					minTime = time;
				} else {
					minTime = Math.min(minTime, time);
				}
			}
		}
		return minTime;
	}

	public long checkAction(ActionDependency dOn, Action a, User u, HashMap<String, User> uMap) {
		if (a == null) return -1;
//		UsersDelegator ud = new UsersDelegator(this);
//		User actionUser = ud.getUserByEmail(u.getRunId(), a.getUserEmail());
		Integer scope = dOn.getScope();
		if (scope == null) scope = Dependency.USER_SCOPE;
		switch (scope) {
		case Dependency.USER_SCOPE:
			if (!a.getUserEmail().equals(u.getEmail())) return -1;
			break;
		case Dependency.TEAM_SCOPE:
			if (!uMap.get(a.getUserEmail()).getTeamId().equals(u.getTeamId())) return -1;
			break;

		default:
			break;
		}
		String role = dOn.getRole();
		if (role != null && !hasRole(uMap.get(a.getUserEmail()), role)) return -1;
		
		if (dOn.getAction() != null && !dOn.getAction().equals(a.getAction()))
			return -1;
		if (dOn.getGeneralItemId() != null && !dOn.getGeneralItemId().equals(a.getGeneralItemId()))
			return -1;
		if (dOn.getGeneralItemType() != null && !dOn.getGeneralItemType().equals(a.getGeneralItemType()))
			return -1;
		if (a.getTime() == null) return 0;
		return a.getTime();
	}

	
	
	public List<GeneralItem> getNonVisibleItems(GeneralItemList allItems, GeneralItemList filterAway) {
		List<GeneralItem> returnItems = new ArrayList();
		long currentTime = System.currentTimeMillis();
		HashSet<Long> idsToRemove = new HashSet<Long>();
		for (Iterator iterator = filterAway.getGeneralItems().iterator(); iterator.hasNext();) {
			GeneralItem generalItem = (GeneralItem) iterator.next();
			if (generalItem.getVisibleAt() == null) {
				idsToRemove.add(generalItem.getId());
			} else if (generalItem.getVisibleAt() < currentTime) {
				idsToRemove.add(generalItem.getId());
			}
		}
		
		for (GeneralItem item : allItems.getGeneralItems()) {
			if (!idsToRemove.contains(item.getId())) {
				returnItems.add(item);
			}
		}
		return returnItems;

	}
	
	public List<GeneralItem> getNotDisappearedItems(GeneralItemList allItems, GeneralItemList filterAway) {
		List<GeneralItem> returnItems = new ArrayList();
		long currentTime = System.currentTimeMillis();
		HashSet<Long> idsToRemove = new HashSet<Long>();
		for (Iterator iterator = filterAway.getGeneralItems().iterator(); iterator.hasNext();) {
			GeneralItem generalItem = (GeneralItem) iterator.next();
			if (generalItem.getDisappearAt() == null) {
				idsToRemove.add(generalItem.getId());
			} else if (generalItem.getDisappearAt() < currentTime) {
				idsToRemove.add(generalItem.getId());
			}
		}
		
		for (GeneralItem item : allItems.getGeneralItems()) {
			if (!idsToRemove.contains(item.getId())) {
				returnItems.add(item);
			}
		}
		return returnItems;

	}

	public GeneralItemList search(String searchQuery) {
		try {
		    Results<ScoredDocument> results = getIndex().search(searchQuery);
		    GeneralItemList resultsList = new GeneralItemList();
		    for (ScoredDocument document : results) {
		    	GeneralItem gi =new GeneralItem();
		    	gi.setName(document.getFields("title").iterator().next().getText());
		    	gi.setGameId(document.getFields("gameId").iterator().next().getNumber().longValue());
		    	gi.setId(document.getFields("generalItemId").iterator().next().getNumber().longValue());

		    	resultsList.addGeneralItem(gi);
		    }
		    return resultsList;
		} catch (SearchException e) {
		    if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
		        // retry
		    }
		}
		return null;
	}

	public Index getIndex() {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("generalItem_index").build();
		return SearchServiceFactory.getSearchService().getIndex(indexSpec);
	}


}
