package org.celstec.arlearn2.jdo.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.GameJDO;
import org.celstec.arlearn2.jdo.classes.GeneralItemJDO;
import org.celstec.arlearn2.jdo.classes.RunJDO;
import org.codehaus.jettison.json.JSONException;

import com.google.appengine.api.datastore.Text;

public class RunManager {

	private static final String params[] = new String[] { "id", "gameId", "owner", "title", "tagId" };
	private static final String paramsNames[] = new String[] { "runParam", "gameParam", "ownerEmailParam", "titleParam", "tagIdParam" };
	private static final String types[] = new String[] { "Long", "Long", "String", "String", "String" };

	public static Long addRun(String title, String owner, Long gameId, Long runId, Long startTime, Long serverCreationTime, Run run) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		RunJDO runJdo = new RunJDO();
		runJdo.setGameId(gameId);
		runJdo.setRunId(runId);
		runJdo.setOwner(owner);
		runJdo.setTitle(title);
		runJdo.setStartTime(startTime);
		runJdo.setServerCreationTime(serverCreationTime);
		runJdo.setLastModificationDate(serverCreationTime);
		runJdo.setPayload(new Text(run.toString()));
		if (run.getRunConfig() != null) {
			runJdo.setTagId(run.getRunConfig().getNfcTag());
		}
		try {
			return pm.makePersistent(runJdo).getRunId();
		} finally {
			pm.close();
		}
	}

	private static List<RunJDO> getRuns(PersistenceManager pm, Long runId, Long gameId, String owner, String title, String tagId) {
		Query query = pm.newQuery(RunJDO.class);
		Object args[] = { runId, gameId, owner, title, tagId };
		query.setFilter(ManagerUtil.generateFilter(args, params, paramsNames));
		query.declareParameters(ManagerUtil.generateDeclareParameters(args, types, params, paramsNames));
		return (List<RunJDO>) query.executeWithArray(ManagerUtil.filterOutNulls(args));
	}
	
	public static List<Run> getRuns(Long runId, Long gameId, String owner, String title, String tagId) {
		ArrayList<Run> returnRuns = new ArrayList<Run>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Iterator<RunJDO> it = getRuns(pm, runId, gameId, owner, title, tagId).iterator();
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
			List<RunJDO> runsToDelete = getRuns(pm, runId, null, null, null, null);
			pm.deletePersistentAll(runsToDelete);
		} finally {
			pm.close();
		}

	}
	
	public static void setStatusDeleted(long runId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<RunJDO> deleteList = getRuns(pm, runId, null, null, null, null);
			for (RunJDO jdo: deleteList) {
				jdo.setDeleted(true);
				jdo.setLastModificationDate(System.currentTimeMillis());
			}
		} finally {
			pm.close();
		}
	}
	
	public static void setLastModificationDate(long runId, long timestamp) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<RunJDO> deleteList = getRuns(pm, runId, null, null, null, null);
			for (RunJDO jdo: deleteList) {
				jdo.setLastModificationDate(timestamp);
			}
		} finally {
			pm.close();
		}
	}
	
	public static void updateRun(long runId, Run run) {
		run.setGame(null);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<RunJDO> updateList = getRuns(pm, runId, null, null, null, null);
			for (RunJDO jdo: updateList) {
				jdo.setPayload(new Text(run.toString()));
				jdo.setLastModificationDate(System.currentTimeMillis());
				jdo.setTitle(run.getTitle());
				jdo.setGameId(run.getGameId());
				jdo.setOwner(run.getOwner());
				jdo.setStartTime(run.getStartTime());
				jdo.setServerCreationTime(run.getServerCreationTime());
				jdo.setDeleted(false);
				if (run.getRunConfig() != null) {
					jdo.setTagId(run.getRunConfig().getNfcTag());
				}
			}
		} finally {
			pm.close();
		}
	}
	
	
	private static Run toBean(RunJDO jdo) {
		if (jdo == null)
			return null;
		Run run;
		if (jdo.getPayload() != null) {
			try {
				run = (Run) JsonBeanDeserializer.deserialize(jdo.getPayload().getValue());
			} catch (JSONException e) {
				run = new Run();
			}
		} else {
			run = new Run();
		}
		run.setRunId(jdo.getRunId());
		run.setTitle(jdo.getTitle());
		run.setGameId(jdo.getGameId());
		run.setOwner(jdo.getOwner());
		run.setStartTime(jdo.getStartTime());
		run.setDeleted(jdo.getDeleted());
		run.setServerCreationTime(jdo.getServerCreationTime());
		run.setLastModificationDate(jdo.getLastModificationDate());
		return run;
	}

}
