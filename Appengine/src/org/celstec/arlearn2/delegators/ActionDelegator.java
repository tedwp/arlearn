package org.celstec.arlearn2.delegators;

import java.util.logging.Logger;

import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ActionList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.notification.Notification;
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
        log.severe("action "+action.getAction());
        log.severe("user "+action.getUserEmail()+" run "+action.getRunId());
        

		if (action.getRunId() == null) {
			action.setError("No run identifier specified");
			return action;
		}
		UsersDelegator qu = new UsersDelegator(this);
		action.setUserEmail(qu.getCurrentUserAccount());
		
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
		
		ActionManager.addAction(action.getRunId(), action.getAction(), action.getUserEmail(), action.getGeneralItemId(), action.getGeneralItemType(), action.getTime());
		(new UpdateGeneralItems(authToken, action.getRunId(), action.getAction(), action.getUserEmail())).scheduleTask();
//		Queue queue = QueueFactory.getDefaultQueue();
//		queue.add(TaskOptions.Builder.withUrl("/asyncTask")
//				.param(AsyncTasksServlet.TASK, "" + AsyncTasksServlet.UPDATE_GENERAL_ITEMS)
//				.param(AsyncTasksServlet.AUTH, authToken)
//				.param(AsyncTasksServlet.RUNID, "" + action.getRunId())
//				.param(AsyncTasksServlet.ACTION, action.getAction())
//				.param(AsyncTasksServlet.EMAIL, action.getUserEmail()));
//		queue.add(TaskOptions.Builder.withUrl("/asyncTask")
//				.param(AsyncTasksServlet.TASK, "" + AsyncTasksServlet.UPDATE_SCORE)
//				.param(AsyncTasksServlet.AUTH, authToken)
//				.param(AsyncTasksServlet.RUNID, "" + action.getRunId())
//				.param(AsyncTasksServlet.ACTION, action.getAction())
//				.param(AsyncTasksServlet.EMAIL, action.getUserEmail()));
		ActionCache.getInstance().removeRunAction(action.getRunId());
		return action;
	}

	public void deleteActions(Long runId) {
		ActionManager.deleteActions(runId);
		ActionCache.getInstance().removeRunAction(runId);
	}
	
	
}
