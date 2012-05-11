package org.celstec.arlearn2.delegators;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.jdo.manager.RunManager;
import org.celstec.arlearn2.jdo.manager.UserManager;
import org.celstec.arlearn2.tasks.beans.DeleteActions;
//import org.celstec.arlearn2.tasks.beans.DeleteInventoryRecords;
//import org.celstec.arlearn2.tasks.beans.DeleteProgressRecord;
import org.celstec.arlearn2.tasks.beans.DeleteBlobs;
import org.celstec.arlearn2.tasks.beans.DeleteResponses;
import org.celstec.arlearn2.tasks.beans.DeleteScoreRecords;
import org.celstec.arlearn2.tasks.beans.DeleteTeams;
import org.celstec.arlearn2.tasks.beans.DeleteUsers;
import org.celstec.arlearn2.tasks.beans.DeleteVisibleItems;
import org.celstec.arlearn2.tasks.beans.NotifyUpdateRun;
import org.celstec.arlearn2.util.RunsCache;

import com.google.gdata.util.AuthenticationException;

//TODO migrate and adapt cache
public class RunDelegator extends GoogleDelegator {
	private static final Logger logger = Logger.getLogger(RunDelegator.class.getName());

	public RunDelegator(String authtoken) throws AuthenticationException {
		super(authtoken);
	}

	public RunDelegator(GoogleDelegator gd) {
		super(gd);
	}

	public Run getRun(Long runId) {
		Run r = RunsCache.getInstance().getRun(runId);
		if (r == null) {
			List<Run> runList = RunManager.getRuns(runId, null, null, null);
			if (runList.isEmpty())
				return null;
			r = runList.get(0);
			RunsCache.getInstance().putRun(runId, r);
		}
		GameDelegator gd = new GameDelegator(this);
		r.setGame(gd.getUnOwnedGame(r.getRunId()));
		return r;
	}

	public RunList getRuns() {
		RunList rl = new RunList();
		UsersDelegator qu = new UsersDelegator(this);
		String myAccount = qu.getCurrentUserAccount();

		if (myAccount == null) {
			 rl.setError("login to retrieve your list of games");
		} else {
			rl.setRuns(RunManager.getRuns(null, null, myAccount, null));
		}
		return rl;
	}

	public long getRunDuration(Long runId) {
		Run r = getRun(runId);
		if (r == null) {
			System.out.println("runid is null");
			return 0;
		}
		Long runStartTime = r.getStartTime();
		if (runStartTime == null)
			return 0;
		return System.currentTimeMillis() - r.getStartTime();
	}

	public RunList getParticipateRuns() {
		UsersDelegator qu = new UsersDelegator(this);
		String myAccount = qu.getCurrentUserAccount();
		// TODO migrate this method to UserQuery
		// TODO add this to cache
		// TODO migrate RunsCache
		Iterator<User> it = UserManager.getUserList(null, myAccount, null, null).iterator();
		RunList rl = new RunList();
		while (it.hasNext()) {
			User user = (User) it.next();
			rl.addRun(getRun(user.getRunId()));
		}
		return rl;
	}

	public Run createRun(Run run) {
		if (run.getStartTime() == null) {
			run.setStartTime(System.currentTimeMillis());
			run.setServerCreationTime(run.getStartTime());
		}
		UsersDelegator qu = new UsersDelegator(this);
		String myAccount = qu.getCurrentUserAccount();

		if (myAccount == null) {
			run.setError("login to create a game");
			return run;
		}
		GameDelegator cg = new GameDelegator(this);
		Game game = cg.getGame(run.getGameId());
		if (game == null) {
			run.setError("Game with id '" + run.getGameId() + "' does not exist");
			return run;
		}
		JsonBeanSerialiser jbs = new JsonBeanSerialiser(run.getGameOverDependsOn());
		run.setRunId(RunManager.addRun(run.getTitle(), myAccount, game.getGameId(), run.getRunId(), run.getStartTime(), run.getServerCreationTime(), jbs.serialiseToJson().toString()));
		return run;
	}

	public Run deleteRun(Long runId) {
		UsersDelegator qu = new UsersDelegator(this);
		String myAccount = qu.getCurrentUserAccount();
		return deleteRun(getRun(runId), myAccount);
	}

	private Run deleteRun(Run r, String myAccount) {
		if (!r.getOwner().equals(myAccount)) {
			Run run = new Run();
			run.setError("You are not the owner of this run");
			return run;
		}
		RunManager.deleteRun(r.getRunId());
		RunsCache.getInstance().removeRun(r.getRunId());
		(new DeleteVisibleItems(authToken, r.getRunId())).scheduleTask();
		(new DeleteActions(authToken, r.getRunId())).scheduleTask();
		(new DeleteTeams(authToken, r.getRunId(), null)).scheduleTask();
		(new DeleteBlobs(authToken, r.getRunId())).scheduleTask();
		(new DeleteResponses(authToken, r.getRunId())).scheduleTask();
		(new DeleteScoreRecords(authToken, r.getRunId())).scheduleTask();
		//TODO  switch on DeleteInventoryRecords
//		(new DeleteInventoryRecords(authToken, r.getRunId())).scheduleTask();
//		(new DeleteProgressRecord(authToken, r.getRunId())).scheduleTask();
//		(new DeleteUsers(authToken, r.getRunId(), null,null)).scheduleTask(); // happens from within delete team
		return r;
	}

	public void deleteRuns(long gameId, String email) {
		List<Run> runList = RunManager.getRuns(null, gameId, email, null);
		for (Run r : runList) {
			deleteRun(r, email);
		}
	}

	public Config getConfig(Long runId) {
		Run r = getRun(runId);
		logger.log(Level.SEVERE, "gameId "+r.getGameId());
		GameDelegator gd = new GameDelegator(this);
		logger.log(Level.SEVERE, "game "+gd.getNotOwnedGame(r.getGameId()));
		return gd.getNotOwnedGame(r.getGameId()).getConfig();
	}
}