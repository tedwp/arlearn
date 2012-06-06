package org.celstec.arlearn2.api;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ActionList;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.DependsOn;
import org.celstec.arlearn2.beans.run.GameStateRecord;
import org.celstec.arlearn2.beans.game.ProgressDefinition;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.TeamList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.UserList;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;

import org.celstec.arlearn2.delegators.ActionDelegator;
import org.celstec.arlearn2.delegators.ProgressDefinitionDelegator;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.TeamsDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.delegators.progressRecord.QueryProgressRecord;
import org.celstec.arlearn2.delegators.scoreRecord.QueryScoreRecord;
import org.codehaus.jettison.json.JSONObject;

import com.google.gdata.util.AuthenticationException;

@Path("/progress")
public class Progress extends Service {
	private static final Logger logger = Logger.getLogger(Progress.class.getName());

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String pdString, @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		Object inProgressDefinition = deserialise(pdString, ProgressDefinition.class, contentType);
		if (inProgressDefinition instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inProgressDefinition), accept);
		ProgressDefinition pd = (ProgressDefinition) inProgressDefinition;

		ProgressDefinitionDelegator pdd = new ProgressDefinitionDelegator(token);
		ProgressDefinition proDef = pdd.createProgressDefinition(pd);
		return serialise(proDef, accept);

	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/definition/game/{gameId}/action/{action}/generalItemId/{generalItemId}/generalItemType/{generalItemType}")
	public String getDefinitions(@HeaderParam("Authorization") String token, @PathParam("gameId") Long gameId, @PathParam("action") String actionString,
			@PathParam("generalItemId") Long generalItemId, @PathParam("generalItemType") String generalItemType, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		ProgressDefinitionDelegator pdd = new ProgressDefinitionDelegator(token);

		if (actionString.equals("null"))
			actionString = null;
		if (generalItemId.equals("null"))
			generalItemId = null;
		if (generalItemType.equals("null"))
			generalItemType = null;

		Action action = new Action();
		action.setAction(actionString);
		action.setGeneralItemId(generalItemId);
		action.setGeneralItemType(generalItemType);

		return serialise(pdd.getProgressDefinitionsForAction(gameId, action), accept);

	}

//	@GET
//	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	@Path("/gameState/{runIdentifier}")
//	public String getGameState(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
//			throws AuthenticationException {
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
//		GameStateRecord gsr = new GameStateRecord();
//		Long runId = new Long(runIdentifier);
//		RunDelegator rd = new RunDelegator(token);
//		Run run = rd.getRun(runIdentifier);
//		TeamsDelegator td = new TeamsDelegator(token);
//		TeamList tl = td.getTeams(runIdentifier);
//
//		UsersDelegator qu = new UsersDelegator(token);
//		UserList ul = qu.getUsers(runId);
//
//		User loggedUser = qu.getUserByEmail(runId, qu.getCurrentUserAccount());
//		List<User> loggedTeam = qu.getUserList(runId, null, null, loggedUser.getTeamId());
//
//		QueryProgressRecord qpr = new QueryProgressRecord(token);
//		QueryScoreRecord qsr = new QueryScoreRecord(token);
//
//		Iterator it = tl.getTeams().iterator();
//		while (it.hasNext()) {
//			Team t = (Team) it.next();
//			Long teamProgress = qpr.getTeamProgress(runId, t.getTeamId());
//			Long allProgress = qpr.getAllProgressForTeam(runId, t.getTeamId());
//			Long teamScore = qsr.getTeamScore(runId, t.getTeamId());
//			Long allScore = qsr.getAllScoreForTeam(runId, t.getTeamId());
//
//			if (teamProgress != null)
//				gsr.getTeamProgress().put(t.getTeamId(), teamProgress);
//			if (allProgress != null)
//				gsr.getAllProgress().put(t.getTeamId(), allProgress);
//			if (teamScore != null)
//				gsr.getTeamScores().put(t.getTeamId(), teamScore);
//			if (allScore != null)
//				gsr.getAllScores().put(t.getTeamId(), allScore);
//		}
//
//		it = ul.getUsers().iterator();
//		while (it.hasNext()) {
//			User u = (User) it.next();
//			Long userProgress = qpr.getUserProgress(runId, u.getEmail());
//			Long userScore = qsr.getUserScore(runId, u.getEmail());
//
//			if (userProgress != null)
//				gsr.getUserProgress().put(u.getEmail(), userProgress);
//			if (userScore != null)
//				gsr.getUserScores().put(u.getEmail(), userScore);
//		}
//
//		ActionDelegator qa = new ActionDelegator(token);
//		ActionList al = qa.getActionList(runId);
//
//		gsr.setRunState(getRunState(run, al, loggedUser, loggedTeam));
//
//		return serialise(gsr, accept);
//	}

//	public boolean isGameOver(Run run, ActionList al, User user, List<User> team) {
//		if (run.getGameOverDependsOn() == null || run.getGameOverDependsOn().equals(""))
//			return false;
//		// DependsOn dOn = new DependsOn(run.getGameOverDependsOn());
//		DependsOn dOn = run.getGameOverDependsOn();
//		if (dOn.getScope() == -1)
//			return false;
//		if (al != null && al.getActions() != null) {
//			ActionList alFiltered = new ActionList();
//			if (dOn.getScope() == dOn.USER_SCOPE) {
//				Iterator it = al.getActions().iterator();
//				while (it.hasNext()) {
//					Action a = (Action) it.next();
//					if (a != null && a.getUserEmail() != null && a.getUserEmail().equals(user.getEmail())) {
//						alFiltered.addAction(a);
//					}
//				}
//			} else if (dOn.getScope() == dOn.TEAM_SCOPE) {
//				Iterator it = al.getActions().iterator();
//				while (it.hasNext()) {
//					Action a = (Action) it.next();
//					Iterator uit = team.iterator();
//					while (uit.hasNext()) {
//						User teamMember = (User) uit.next();
//						if (a != null && a.getUserEmail() != null && a.getUserEmail().equals(teamMember.getEmail())) {
//							alFiltered.addAction(a);
//						}
//					}
//				}
//			} else {
//				alFiltered = al;
//			}
//			if (checkActions(dOn, alFiltered))
//				return true;
//		}
//		return false;
//	}

//	public String getRunState(Run run, ActionList al, User user, List<User> team) {
//		// TODO: create a dynamic method to calculate the runState depending on
//		// action
//		if (isGameOver(run, al, user, team)) {
//			return "gameOver";
//		}
//		return "active";
//	}

	// TODO make this code more generic see progress
	public boolean checkActions(DependsOn dOn, ActionList al) {
		Iterator<Action> it = al.getActions().iterator();
		while (it.hasNext()) {
			if (checkAction(dOn, (Action) it.next()))
				return true;
		}
		return false;
	}

	public boolean checkAction(DependsOn dOn, Action a) {
		if (dOn.getAction() != null && !dOn.getAction().equals(a.getAction()))
			return false;
		if (dOn.getGeneralItemId() != null && !dOn.getGeneralItemId().equals(a.getGeneralItemId()))
			return false;
		if (dOn.getGeneralItemType() != null && !dOn.getGeneralItemType().equals(a.getGeneralItemType()))
			return false;
		return true;
	}
}
