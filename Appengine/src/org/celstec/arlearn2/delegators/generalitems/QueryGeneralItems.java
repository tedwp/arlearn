package org.celstec.arlearn2.delegators.generalitems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.juli.logging.Log;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ActionList;
import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.game.DependsOn;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.run.InventoryRecord;
import org.celstec.arlearn2.beans.generalItem.PickupItem;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.cache.GeneralitemsCache;
import org.celstec.arlearn2.delegators.ActionDelegator;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.delegators.VisibleItemDelegator;
import org.celstec.arlearn2.delegators.inventory.CreateInventoryRecord;
import org.celstec.arlearn2.delegators.inventory.QueryInventoryRecord;
import org.celstec.arlearn2.delegators.notification.Notification;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.jdo.manager.GeneralItemManager;
import org.celstec.arlearn2.tasks.beans.NotifyItemVisible;
import org.celstec.arlearn2.tasks.beans.UpdateGeneralItems;

import com.google.gdata.util.AuthenticationException;

public class QueryGeneralItems extends GoogleDelegator {
	private static final Logger log = Logger.getLogger(QueryGeneralItems.class.getName());

	public QueryGeneralItems(String authtoken) throws AuthenticationException {
		super(authtoken);
	}
	

	public QueryGeneralItems(GoogleDelegator gd) {
		super(gd);
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

	// public GeneralItemList getGeneralItems_old(Long tableId) {
	// GeneralItemList gil = new GeneralItemList();
	// try {
	// CSV csv = fc.showTables();
	// if (csv.containsValue("name", "GeneralItem_" + tableId)) {
	// int giTableId = Integer.parseInt(csv.getValue(csv.rowIdForValue("name",
	// "GeneralItem_" + tableId), "table id"));
	// csv = fc.runSelect("select * from " + giTableId);
	// Iterator<HashMap<String, String>> rowsIt = csv.iterator();
	// while (rowsIt.hasNext()) {
	// HashMap<java.lang.String, java.lang.String> hm =
	// (HashMap<java.lang.String, java.lang.String>) rowsIt.next();
	// GeneralItem generalItem;
	// try {
	// Class artifactClass = Class.forName("org.celstec.arlearn2.beans." +
	// hm.get("type"));
	// generalItem = (GeneralItem)
	// artifactClass.getConstructor(String.class).newInstance(hm.get("payload"));
	// generalItem.setRadius(20);
	// generalItem.setId(hm.get("id"));
	// generalItem.setName(hm.get("name"));
	// generalItem.setDescription(hm.get("description"));
	// String location = hm.get("location");
	// if (location != null && location.length() > 0) {
	// String lat = location.substring(0, location.indexOf(' '));
	// String lng = location.substring(location.indexOf(' ') + 1);
	// generalItem.setLat(Double.valueOf(lat));
	// generalItem.setLng(Double.valueOf(lng));
	// }
	// String showAtTime = hm.get("timeDelta");
	// if (showAtTime != null && !showAtTime.equals("")) {
	// generalItem.setShowAtTimeStamp(Long.parseLong(showAtTime));
	// }
	// String dependsOnString = hm.get("dependsOn");
	// if (dependsOnString != null && !dependsOnString.equals("")) {
	// generalItem.setDependsOn(dependsOnString);
	// }
	// gil.addGeneralItem(generalItem);
	// } catch (ClassNotFoundException cnfe) {
	// generalItem = new GeneralItem();
	// }
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return gil;
	// }

	private GeneralItemList getAllGeneralItems(Long runIdentifier) {
		RunDelegator qr = new RunDelegator(this);
		Run run = qr.getRun(runIdentifier);
		return getGeneralItems(run.getGameId());
	}

	 public List<Action> getMatchingActions(ActionList al, DependsOn dOn){
         ArrayList<Action> returnList = new ArrayList<Action>();
         for (Action a: al.getActions()) {
                 if (dependsOnConditionMatches(a, dOn)) returnList.add(a);
         }
         return returnList;
	 }
	 
	 private boolean dependsOnConditionMatches(Action action, DependsOn dOn) {
         if (dOn.getAction() != null && !dOn.getAction().equals(action.getAction())) return false; 
         if (dOn.getGeneralItemId() != null && !dOn.getGeneralItemId().equals(action.getGeneralItemId())) return false; 
         if (dOn.getGeneralItemType() != null && !dOn.getGeneralItemType().equals(action.getGeneralItemType())) return false; 
         return true;
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
//			if (gl.get(i).getDependsOn() != null) {
////				DependsOn dOn = new DependsOn(gl.get(i).getDependsOn());
//				DependsOn dOn = gl.get(i).getDependsOn();
//				if (dOn.getAction() != null || dOn.getGeneralItemId() != null || dOn.getGeneralItemType() != null) {
//					ActionDelegator ad = new ActionDelegator(this);
//					ActionList al = ad.getActionList(runIdentifier);
//					// HashMap<String, List<Action>> hm =
//					// qa.getActions(runIdentifier);
//					// List<Action> la = hm.get(dOn.getEvent());
//					List<Action> la = getMatchingActions(al, dOn);
//					if (la == null || la.size() == 0) {
//						// System.out.println("action not yet executed");
//						gl.remove(i);
//						continue;
//					} else {
//						if (dOn.getScope() == DependsOn.ALL_SCOPE) {
//							// System.out.println("one player has issued this action");
//							// so don't remove
//						} else if (dOn.getScope() == DependsOn.TEAM_SCOPE) {
//							boolean teamMemberExecutedAction = false;
//							Iterator<Action> it = la.iterator();
//							UsersDelegator qu = new UsersDelegator(this);
//							String teamIdentifier = qu.getUserByEmail(runIdentifier, userIdentifier).getTeamId();
//							while (!teamMemberExecutedAction && it.hasNext()) {
//								Action action = (Action) it.next();
//
//								if (action != null && action.getUserEmail() != null && !action.getUserEmail().equals("")) {
//									// UserLoggedInManager.normalizeEmail(action.getUserEmail()).equalsIgnoreCase(userIdentifier))
//									// {
//									User actionUser = qu.getUserByEmail(runIdentifier, action.getUserEmail());
//									if (actionUser != null && teamIdentifier.equals(actionUser.getTeamId())) {
//										teamMemberExecutedAction = true;
//									}
//								}
//							}
//							if (!teamMemberExecutedAction) {
//								gl.remove(i);
//								// System.out.println("no team member has issued this action");
//								continue;
//							} else {
//								// System.out.println("a team member has issued this action");
//							}
//							// System.out.println("one team member  has issued this action");
//						} else if (dOn.getScope() == DependsOn.USER_SCOPE) {
//							boolean userExecutedAction = false;
//							Iterator<Action> it = la.iterator();
//							while (!userExecutedAction && it.hasNext()) {
//								Action action = (Action) it.next();
//								if (action != null && UserLoggedInManager.normalizeEmail(action.getUserEmail()).equalsIgnoreCase(userIdentifier)) {
//									userExecutedAction = true;
//								}
//							}
//							if (!userExecutedAction) {
//								gl.remove(i);
//								// System.out.println("user has not issued this action");
//								continue;
//							} else {
//								// System.out.println("user has issued this action");
//							}
//						}
//					}
//				}
//			}
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
	
	public GeneralItemList getNonPickableItemsAll(Long runIdentifier) {
		RunDelegator qr = new RunDelegator(this);
		Run run = qr.getRun(runIdentifier);
		//TODO run does not exist: return a proper exception
		return getGeneralItems(run.getGameId());
	}

	public GeneralItemList getPickupItems(Long runIdentifier, String userIdentifier) {
		GeneralItemList returnItemList = getGeneralItems(runIdentifier, userIdentifier);
		List<GeneralItem> gl = returnItemList.getGeneralItems();
		for (int i = gl.size() - 1; i >= 0; i--) {
			if (!(gl.get(i) instanceof PickupItem)) {
				gl.remove(i);
				continue;
			}
		}
		return returnItemList;
	}

	public GeneralItemList getMapInventoryItems(Long runId, String email, String teamId) {
		QueryInventoryRecord qir = new QueryInventoryRecord(this);
		CreateInventoryRecord cir = new CreateInventoryRecord(this);
		GeneralItemList result = this.getPickupItems(runId, email);

		List<GeneralItem> gl = result.getGeneralItems();
		for (int i = gl.size() - 1; i >= 0; i--) {
			GeneralItem gi = gl.get(i);
			InventoryRecord ir = qir.getInventoryRecord(runId, gi.getId(), gi.getScope(), email, teamId);
			if (ir == null) {
				ir = new InventoryRecord();
				ir.setRunId(runId);
				if (gi.getScope().equals("user"))
					ir.setEmail(email);
				// TODO if scope is user...do we need to set the team? For now I
				// do not set this (check with roland)
				if (gi.getScope().equals("team"))
					ir.setTeamId(teamId);
				ir.setGeneralItemId(gi.getId());
				ir.setScope(gi.getScope());
				ir.setStatus("map");
				cir.createInventoryRecord(ir);
			} else if (!("map".equals(ir.getStatus()) || "dropped".equals(ir.getStatus()))) {
				gl.remove(i);
			} else if ("dropped".equals(ir.getStatus())) {
				gl.get(i).setLat(ir.getLat());
				gl.get(i).setLng(ir.getLng());
			}
		}

		return result;
	}

	public GeneralItemList getUserInventoryItems(Long runId, String email, String teamId) {
		QueryInventoryRecord qir = new QueryInventoryRecord(this);
		CreateInventoryRecord cir = new CreateInventoryRecord(this);
		GeneralItemList result = this.getPickupItems(runId, email);

		List<GeneralItem> gl = result.getGeneralItems();
		for (int i = gl.size() - 1; i >= 0; i--) {
			GeneralItem gi = gl.get(i);
			InventoryRecord ir = qir.getInventoryRecord(runId, gi.getId(), gi.getScope(), email, teamId);
			if (ir == null) { // not yet initialised, is on map
				gl.remove(i);
			} else if (!"picked".equals(ir.getStatus())) { // not picked, either
															// on map or
															// consumed
				gl.remove(i);
			} else if (!"user".equals(ir.getScope())) { // would be in team
														// inventory
				gl.remove(i);
			} else if (email == null || !email.equals(ir.getEmail())) { // something
																		// is
																		// wrong,
																		// as we
																		// are
																		// in
																		// user
																		// scope!
				gl.remove(i);
			}
		}

		return result;
	}

	public GeneralItemList getTeamInventoryItems(Long runId, String email, String teamId) {
		QueryInventoryRecord qir = new QueryInventoryRecord(this);
		CreateInventoryRecord cir = new CreateInventoryRecord(this);
		GeneralItemList result = this.getPickupItems(runId, email);

		List<GeneralItem> gl = result.getGeneralItems();
		for (int i = gl.size() - 1; i >= 0; i--) {
			GeneralItem gi = gl.get(i);
			InventoryRecord ir = qir.getInventoryRecord(runId, gi.getId(), gi.getScope(), email, teamId);
			if (ir == null) { // not yet initialised, is on map
				gl.remove(i);
			} else if (!"picked".equals(ir.getStatus())) { // not picked, either
															// on map or
															// consumed
				gl.remove(i);
			} else if (!("team".equals(ir.getScope()) || "all".equals(ir.getScope()))) { // would
																							// be
																							// in
																							// user
																							// inventory
				gl.remove(i);
			} else if (teamId == null || !teamId.equals(ir.getTeamId())) { // maybe
																			// picked
																			// by
																			// other
																			// team?
				gl.remove(i);
			}
		}

		return result;
	}
	
	
	
	

	
    
	
		
	public void checkActionEffect(Action action, long runId, User u) {
		if (u == null) {
			log.log(Level.SEVERE, "user is null in checkActionEffect (runId: "+runId+")");
			return;
		}
		ActionDelegator qa = new ActionDelegator(this);
		ActionList al = qa.getActionList(runId);
		VisibleItemDelegator vid = new VisibleItemDelegator(this);
		VisibleItemsList vil = vid.getVisibleItems(runId, null, u.getEmail(), u.getTeamId());
		vil.merge(vid.getVisibleItems(runId, null, null, u.getTeamId()));
		vil.merge(vid.getVisibleItems(runId, null, u.getEmail(), null));
		
		log.severe("vil is "+vil);
		
		vil.log(log);

		Iterator<GeneralItem> it = getNonVisibleItems(getAllGeneralItems(runId), vil).iterator();
		while (it.hasNext()) {
			GeneralItem generalItem = (GeneralItem) it.next();
			System.out.println("dealing with "+generalItem.getId()+" "+generalItem.getName());
			log.severe("dealing with "+generalItem.getId()+" "+generalItem.getName());
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
	
	 public  List<GeneralItem> getNonVisibleItems(GeneralItemList giList, VisibleItemsList vil) {
         List<GeneralItem> returnItems = new ArrayList();
         for (GeneralItem item : giList.getGeneralItems()) {
                 if (vil.getVisibleItem(item.getId())==null) returnItems.add(item);
         }
         return returnItems;
         
 }
	 
//	 public boolean isVisible(GeneralItem gi, ActionList al) {
//         if (gi.getDependsOn()==null || gi.getDependsOn().equals("")) return gi.timeStampCheck();
////         DependsOn dOn = new DependsOn(gi.getDependsOn());
//         DependsOn dOn = gi.getDependsOn();
//         if (checkActions(dOn,al)) return gi.timeStampCheck();
//         return false;
//	 }
	 
	 
	 //TODO make this code more generic see progress
	 public boolean checkActions(DependsOn dOn, ActionList al) {
         Iterator<Action>it = al.getActions().iterator();
         while (it.hasNext()) {
                 if (checkAction(dOn, (Action) it.next())) return true;
         }
         return false;
 }

	 public boolean checkAction(DependsOn dOn, Action a) {
         if (dOn.getAction() != null && !dOn.getAction().equals(a.getAction())) return false;
         if (dOn.getGeneralItemId() != null && !dOn.getGeneralItemId().equals(a.getGeneralItemId())) return false;
         if (dOn.getGeneralItemType() != null && !dOn.getGeneralItemType().equals(a.getGeneralItemType())) return false;
         return true;
 }
	 

}
