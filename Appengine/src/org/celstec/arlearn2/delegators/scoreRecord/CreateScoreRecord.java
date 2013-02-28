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
package org.celstec.arlearn2.delegators.scoreRecord;

import java.util.List;

import org.celstec.arlearn2.beans.run.Action;

import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.game.ScoreDefinition;
import org.celstec.arlearn2.beans.run.ScoreRecord;
import org.celstec.arlearn2.cache.ScoreRecordCache;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.ScoreDefinitionDelegator;
import org.celstec.arlearn2.delegators.notification.ScoreNotification;
import org.celstec.arlearn2.jdo.manager.ScoreRecordManager;
import org.celstec.arlearn2.tasks.beans.UpdateScore;

import com.google.gdata.util.AuthenticationException;

public class CreateScoreRecord extends GoogleDelegator {

	public CreateScoreRecord(String authToken) throws AuthenticationException {
		super(authToken);
	}

	public CreateScoreRecord(GoogleDelegator gd) {
		super(gd);
	}

	
	public ScoreRecord createScoreRecord(ScoreRecord sr) {
		return ScoreRecordManager.addScoreRecord(sr.getAction(), sr.getGeneralItemId(), sr.getGeneralItemType(), sr.getRunId(), sr.getScope(), sr.getTeamId(), sr.getEmail(), sr.getValue());
	}
	
	public ScoreRecord updateScore(Action action, String teamId) {
		ScoreDefinitionDelegator qd = new ScoreDefinitionDelegator(this);
		RunDelegator rd = new RunDelegator(this);
		Run run = rd.getRun(action.getRunId());
//		ScoreDefinition pd = qd.getScoreDefinitionForAction(run.getGameId(), action);
		List<ScoreDefinition> sds = qd.getScoreDefinitionsList(run.getGameId(), null, null, null);
		for (ScoreDefinition sd : sds) {
			boolean definitionIsApplicable = true;
			if (sd.getAction() != null && !sd.getAction().equals(action.getAction())) definitionIsApplicable = false ;
			if (sd.getGeneralItemId() != null && !sd.getGeneralItemId().equals(action.getGeneralItemId())) definitionIsApplicable = false ;
			if (sd.getGeneralItemType() != null && !sd.getGeneralItemType().equals(action.getGeneralItemType())) definitionIsApplicable = false ;
			if (sd.getScope() == null || sd.getScope().length() == 0) definitionIsApplicable = false ;
			if (definitionIsApplicable) {
				QueryScoreRecord qsr = new QueryScoreRecord(this);
				ScoreRecord pr = null;
					pr = qsr.getScoreRecord(action, sd.getScope(), teamId);
					// if a score record exists, there should be no update of the existing record
					if (pr == null) {
						pr = new ScoreRecord();
						pr.setRunId(action.getRunId());
						pr.setAction(action.getAction());
						pr.setGeneralItemId(action.getGeneralItemId());
						pr.setGeneralItemType(action.getGeneralItemType());
						pr.setScope(sd.getScope());
						pr.setEmail(action.getUserEmail());
						pr.setTeamId(teamId);
						pr.setValue(sd.getValue());
						pr = createScoreRecord(pr);
//						ScoreRecordCache.getInstance().removeScore(runId, pd.getScope(), email, teamId);
//						ScoreRecordCache.getInstance().removeScoreRecord(runId, actionId, pd.getScope(), email, teamId);
//						ScoreRecordCache.getInstance().putScoreRecord(runId, pr);
						// TODO update score cache
						
						//TODO async tell score
						(new UpdateScore(authToken, action.getRunId(), sd.getScope(), action.getUserEmail())).scheduleTask();
					}
					return pr;
				
			}
			
		}
		
		return null;
	}

	public void deleteScoreRecords(Long runId) {
		ScoreRecordManager.deleteScoreRecords(runId, null);
		ScoreRecordCache.getInstance().removeScore(runId, "user");
		ScoreRecordCache.getInstance().removeScore(runId, "team");
		ScoreRecordCache.getInstance().removeScore(runId, "all");		
	}
	
	public void deleteScoreRecords(Long runId, String account) {
		ScoreRecordManager.deleteScoreRecords(runId, account);
		ScoreRecordCache.getInstance().removeScore(runId, "user");
		ScoreRecordCache.getInstance().removeScore(runId, "team");
		ScoreRecordCache.getInstance().removeScore(runId, "all");		
	}

}
