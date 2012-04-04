package org.celstec.arlearn2.delegators.progressRecord;


import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.game.ProgressDefinition;
import org.celstec.arlearn2.beans.run.ProgressRecord;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.delegators.ProgressDefinitionDelegator;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.jdo.manager.ProgressRecordManager;
import org.celstec.arlearn2.cache.ProgressRecordCache;

import com.google.gdata.util.AuthenticationException;

public class CreateProgressRecord extends GoogleDelegator {

	public CreateProgressRecord(GoogleDelegator gd) {
		super(gd);
	}

	public CreateProgressRecord(String authToken)
			throws AuthenticationException {
		super(authToken);
	}

	public ProgressRecord createProgressRecord(ProgressRecord pr) {
		ProgressRecordCache.getInstance().removeProgress(pr.getRunId(), pr.getScope());
		if (pr.getRunId() == null) {
			pr.setError("No run identifier specified");
			return pr;
		}
		ProgressRecordManager.addProgressRecord(pr.getAction(), pr.getGeneralItemId(), pr.getGeneralItemType(), pr.getRunId(), pr.getScope(), pr.getTeamId(), pr.getEmail(),pr.getTimestamp());
//		ProgressRecordCache.getInstance().putProgressRecord(pr, pr.getRunId(), pr.getScope(), pr.getAction(),pr.getGeneralItemId(), pr.getGeneralItemType(), pr.getEmail(), pr.getTeamId());
		return pr;
	}
	
	public ProgressRecord updateProgress(Action action, String teamId) {
		ProgressDefinitionDelegator pdd = new ProgressDefinitionDelegator(this);
		RunDelegator rd = new RunDelegator(this);
		Run run = rd.getRun(action.getRunId());
		ProgressDefinition pd = pdd.getProgressDefinitionsForAction(run.getGameId(), action);
		QueryProgressRecord qpr = new QueryProgressRecord(this);
		ProgressRecord pr = null;
		if (pd != null && pd.getScope() != null && pd.getScope().length() > 0) {
			pr = qpr.getProgressRecord(action.getRunId(), action.getAction(), action.getGeneralItemId(), action.getGeneralItemType(), pd.getScope(), action.getUserEmail(), teamId);
			if (pr == null) {
				pr = new ProgressRecord();
				pr.setRunId(action.getRunId());
//				pr.setActionId(actionId);
				pr.setAction(action.getAction());
				pr.setGeneralItemId(action.getGeneralItemId());
				pr.setGeneralItemType(action.getGeneralItemType());
				pr.setScope(pd.getScope());
				pr.setEmail(action.getUserEmail());
				pr.setTeamId(teamId);
				pr = createProgressRecord(pr);
				
				//if scope == all => remove all progress records in run?
				//if scope == team => remove all records for team
				//if scope == user => remove all records for user
//				ProgressRecordCache.getInstance().removeProgress(runId, pd.getScope(), email, teamId);
//				ProgressRecordCache.getInstance().removeProgressRecord(runId, actionId, pd.getScope(), email, teamId);
//				ProgressRecordCache.getInstance().putProgressRecord(runId, pr);
				
				//TODO update caches
			}
			return pr;
		}
		return null;
	}

	public void deleteProgressRecord(Long runId) {
		// TODO empty cache	
		ProgressRecordManager.deleteProgressRecord(runId);
		
	}

}
