package org.celstec.arlearn2.jdo.manager;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.TeamJDO;

import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.TeamList;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class TeamManager {

	public static void addTeam(Long runId, String teamId, String name) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		TeamJDO team = new TeamJDO();
		team.setTeamId(teamId);
		team.setName(name);
		team.setRunId(runId);
		try {
			pm.makePersistent(team);
		} finally {
			pm.close();
		}
	}

	public static TeamList getTeams(Long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(TeamJDO.class);
			query.setFilter("runId == runIdParam");
			query.declareParameters("Long runIdParam");
			Iterator<TeamJDO> it = ((List<TeamJDO>) query.execute(runId)).iterator();
			TeamList returnList = new TeamList();
			returnList.setRunId(runId);
			while (it.hasNext()) {
				returnList.addTeam(toBean((TeamJDO) it.next()));
			}
			return returnList;
		} finally {
			pm.close();
		}
	}

	public static Team getTeam(String teamId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			return toBean(getTeamJDO(pm, teamId));
		} finally {
			pm.close();
		}
	}

	private static TeamJDO getTeamJDO(PersistenceManager pm, String teamId) {
		try {
			Key k = KeyFactory.createKey(TeamJDO.class.getSimpleName(), teamId);
			TeamJDO teamJDO = pm.getObjectById(TeamJDO.class, k);
			return teamJDO;
		} catch (Exception e) {
			return null;
		}
	}

	private static Team toBean(TeamJDO jdo) {
		if (jdo == null) return null;
		Team teamBean = new Team();
		teamBean.setName(jdo.getName());
		teamBean.setRunId(jdo.getRunId());
		teamBean.setTeamId(jdo.getTeamIdString());
		return teamBean;
	}

	public static void deleteTeam(String teamId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			TeamJDO teamJDO = getTeamJDO(pm, teamId);
			if (teamJDO != null) {
				pm.deletePersistent(teamJDO);
			}
		} finally {
			pm.close();
		}
	}
}
