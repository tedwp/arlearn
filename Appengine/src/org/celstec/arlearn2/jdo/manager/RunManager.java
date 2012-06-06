package org.celstec.arlearn2.jdo.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.DependsOn;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.GameJDO;
import org.celstec.arlearn2.jdo.classes.RunJDO;
import org.codehaus.jettison.json.JSONException;

public class RunManager {

	private static final String params[] = new String[] { "id", "gameId", "owner", "title" };
	private static final String paramsNames[] = new String[] { "runParam", "gameParam", "ownerEmailParam", "titleParam" };
	private static final String types[] = new String[] { "Long", "Long", "String", "String" };

	public static Long addRun(String title, String owner, Long gameId, Long runId, Long startTime, Long serverCreationTime, String gameOverDependsOn) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		RunJDO runJdo = new RunJDO();
		runJdo.setGameId(gameId);
		runJdo.setRunId(runId);
		runJdo.setOwner(owner);
		runJdo.setTitle(title);
		runJdo.setStartTime(startTime);
		runJdo.setServerCreationTime(serverCreationTime);
		runJdo.setGameOverDependsOn(gameOverDependsOn);
		try {
			return pm.makePersistent(runJdo).getRunId();
		} finally {
			pm.close();
		}
	}

	public static List<Run> getRuns(Long runId, Long gameId, String owner, String title) {
		ArrayList<Run> returnRuns = new ArrayList<Run>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Iterator<RunJDO> it = getRuns(pm, runId, gameId, owner, title).iterator();
			while (it.hasNext()) {
				returnRuns.add(toBean((RunJDO) it.next()));
			}
			return returnRuns;
		} finally {
			pm.close();
		}

	}


	public static void deleteRun(Long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<RunJDO> runsToDelete = getRuns(pm, runId, null, null, null);
			pm.deletePersistentAll(runsToDelete);
		} finally {
			pm.close();
		}

	}

	private static List<RunJDO> getRuns(PersistenceManager pm, Long runId, Long gameId, String owner, String title) {
		Query query = pm.newQuery(RunJDO.class);
		Object args[] = { runId, gameId, owner, title };
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return (List<RunJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));
	}
	
	private static Run toBean(RunJDO jdo) {
		if (jdo == null)
			return null;
		Run run = new Run();
		run.setRunId(jdo.getRunId());
		run.setTitle(jdo.getTitle());
		run.setGameId(jdo.getGameId());
		run.setOwner(jdo.getOwner());
		run.setStartTime(jdo.getStartTime());
		run.setServerCreationTime(jdo.getServerCreationTime());
		JsonBeanDeserializer jbd;
//		try {
//			jbd = new JsonBeanDeserializer(jdo.getGameOverDependsOn());
//			run.setGameOverDependsOn((DependsOn) jbd.deserialize(DependsOn.class));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return run;
	}

}
