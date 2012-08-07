package org.celstec.arlearn2.delegators;

import java.util.List;
import java.util.logging.Logger;

import org.celstec.arlearn2.beans.dependencies.ActionDependency;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ActionList;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;
import org.celstec.arlearn2.delegators.progressRecord.CreateProgressRecord;
import org.celstec.arlearn2.delegators.scoreRecord.CreateScoreRecord;
import org.celstec.arlearn2.jdo.manager.ActionManager;
import org.celstec.arlearn2.tasks.beans.UpdateGeneralItems;
import org.celstec.arlearn2.util.ActionCache;

import com.google.gdata.util.AuthenticationException;

public class ActionDelegator extends GoogleDelegator{
	
	private static final Logger logger = Logger.getLogger(ActionDelegator.class.getName());

	public ActionDelegator(String authtoken) throws AuthenticationException {
		super(authtoken);
	}
	
	public ActionDelegator(GoogleDelegator gd) {
		super(gd);
	}
	
	
	public ActionList getActionList(Long runId) {
		ActionList al = ActionCache.getInstance().getRunActions(runId);
		if (al == null) {
			al = ActionManager.runActions(runId);
			ActionCache.getInstance().putRunActions(runId, al);
		}
		return al;
	}
	private static final Logger log = Logger.getLogger(ActionDelegator.class.getName());

	public Action createAction(Action action) {

		if (action.getRunId() == null) {
			action.setError("No run identifier specified");
			return action;
		}
		UsersDelegator qu = new UsersDelegator(this);
		action.setUserEmail(qu.getCurrentUserAccount());
		
		RunDelegator rd = new RunDelegator(this);
		Run r = rd.getRun(action.getRunId());
		
		User u = qu.getUserByEmail(action.getRunId(), action.getUserEmail());
		if (u == null) {
			action.setError("User not found");
			log.severe("user not found");
			return action;
		}
		// check if this action needs to be recorded as progress
		CreateProgressRecord cpr = new CreateProgressRecord(this);
//		cpr.updateProgress(action.getRunId(), action.getAction(), action.getUserEmail(), u.getTeamId());
		cpr.updateProgress(action, u.getTeamId());
		
		// check if this action needs to be recorded as score
		CreateScoreRecord csr = new CreateScoreRecord(this);
//		csr.updateScore(action.getRunId(), action.getAction(), action.getUserEmail(), u.getTeamId());
		csr.updateScore(action, u.getTeamId());
		RunDelegator qr = new RunDelegator(this);
		Run run = qr.getRun(action.getRunId());
		ActionRelevancyPredictor arp = ActionRelevancyPredictor.getActionRelevancyPredicator(run.getGameId(), this);
		System.out.println(arp.toString());
		//TODO migrate these to list of relevant dependecies (getActionDependencies[])
		boolean relevancy = arp.isRelevant(action);
		if (relevancy || "read".equals(action.getAction())) {
			ActionManager.addAction(action.getRunId(), action.getAction(), action.getUserEmail(), action.getGeneralItemId(), action.getGeneralItemType(), action.getTimestamp());
			ActionCache.getInstance().removeRunAction(action.getRunId());
			ChannelNotificator.getInstance().notify(r.getOwner(), action);
			//TODO update score
		} 
		if (relevancy) {
			(new UpdateGeneralItems(authToken, action.getRunId(), action.getAction(), action.getUserEmail(), action.getGeneralItemId(), action.getGeneralItemType())).scheduleTask();
		}
		return action;
	}
	
	private boolean applyRelevancyFilter(Action action, List<ActionDependency> dependencies) {
		for (ActionDependency dep: dependencies) {
			boolean soFar = true;
			if (dep.getAction() != null && !dep.getAction().equals(action.getAction())) soFar = false; 
			if (dep.getGeneralItemId() != null && !dep.getGeneralItemId().equals(action.getGeneralItemId()))soFar = false; 
			if (dep.getGeneralItemType() != null && !dep.getGeneralItemType().equals(action.getGeneralItemType())) soFar = false; 
			if (soFar) return true;
		}
		return false;
	}
	
//	private List<ActionDependency> getActionDependencies(Long runId) {
//		RunDelegator qr = new RunDelegator(this);
//		Run run = qr.getRun(runId);
//		List<ActionDependency> gil = GeneralitemsCache.getInstance().getGameActions(run.getGameId(), null, null);
//		if (gil == null) {
//			System.out.println("not from cache");
//			GeneralItemDelegator gd = new GeneralItemDelegator(this);
//			gil = getActionDependencies(gd.getGeneralItems(run.getGameId()).getGeneralItems());
//			GeneralitemsCache.getInstance().putGameActionsList(gil, run.getGameId(), null, null);
//		}
//		return gil;
//	}
	
	

	public void deleteActions(Long runId) {
		ActionManager.deleteActions(runId);
		ActionCache.getInstance().removeRunAction(runId);
	}
	
	public void deleteActions(Long runId, String email) {
		ActionManager.deleteActions(runId, email);
		ActionCache.getInstance().removeRunAction(runId);
	}
	
	
}
