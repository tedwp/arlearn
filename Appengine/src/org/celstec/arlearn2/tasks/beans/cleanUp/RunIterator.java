package org.celstec.arlearn2.tasks.beans.cleanUp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import org.datanucleus.store.appengine.query.JDOCursorHelper;
import com.google.appengine.api.datastore.Cursor;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.RunJDO;
import org.celstec.arlearn2.jdo.manager.RunManager;
import org.celstec.arlearn2.tasks.beans.DeleteActions;
import org.celstec.arlearn2.tasks.beans.DeleteBlobs;
import org.celstec.arlearn2.tasks.beans.DeleteResponses;
import org.celstec.arlearn2.tasks.beans.DeleteScoreRecords;
import org.celstec.arlearn2.tasks.beans.DeleteTeams;
import org.celstec.arlearn2.tasks.beans.GenericBean;
import org.celstec.arlearn2.tasks.beans.UpdateGeneralItemsVisibility;
import org.celstec.arlearn2.util.RunsCache;

public class RunIterator extends GenericBean {

	String cursorString;

	public RunIterator() {
		super();
	}

	public RunIterator(String cursorString) {
		super();
		this.cursorString = cursorString;
	}

	public String getCursorString() {
		return cursorString;
	}

	public void setCursorString(String cursorString) {
		this.cursorString = cursorString;
	}

	@Override
	public void run() {
		if ("*".equals(cursorString)) {
			startIteration(null);
		} else {
			startIteration(cursorString);
		}
	}

//	private void startIteration() {
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			List<RunJDO> runs = RunManager.listAllRuns(pm, null);
//			processRuns(runs);
//			rescheduleIteration(runs);
//		} finally {
//			pm.close();
//		}
//	}

	private void rescheduleIteration(List<RunJDO> runs) {
		Cursor cCursor = JDOCursorHelper.getCursor(runs);
		String cursorString = cCursor.toWebSafeString();
		System.out.println("cursorout " + cursorString);
		(new RunIterator(cursorString)).scheduleTask();
	}

	private void startIteration(String cursorString) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<RunJDO> runs = RunManager.listAllRuns(pm, cursorString);
			processRuns(pm, runs);
			if (!runs.isEmpty()) rescheduleIteration(runs);
		} finally {
			pm.close();
		}

	}

	private void processRuns(PersistenceManager pm, List<RunJDO> runs) {
		for (RunJDO runJDO : runs) {
			processrun(pm, runJDO);
		}
	}
	
	private void processrun(PersistenceManager pm, RunJDO runJDO) {
		System.out.println("dealing with run " + runJDO.getTitle() + " " + runJDO.getDeleted());
		String authToken = null;
		GameDelegator gd = new GameDelegator();
		if (gd.getGameWithoutAccount(runJDO.getGameId()) == null) {
			RunManager.setStatusDeleted(runJDO.getRunId());
			RunsCache.getInstance().removeRun(runJDO.getRunId());
			(new UpdateGeneralItemsVisibility(authToken, runJDO.getRunId(), null, 2)).scheduleTask();

//			(new DeleteVisibleItems(authToken, r.getRunId())).scheduleTask();
			(new DeleteActions(authToken, runJDO.getRunId())).scheduleTask();
			(new DeleteTeams(authToken, runJDO.getRunId(), null)).scheduleTask();
			(new DeleteBlobs(authToken, runJDO.getRunId())).scheduleTask();
			(new DeleteResponses(authToken, runJDO.getRunId())).scheduleTask();
			(new DeleteScoreRecords(authToken, runJDO.getRunId())).scheduleTask();
		}
		if (runJDO.getDeleted() != null && runJDO.getDeleted() && (runJDO.getLastModificationDate()+ (2592000000l)) < System.currentTimeMillis()) {
			RunManager.deleteRun(pm, runJDO);
		}

	}
}
