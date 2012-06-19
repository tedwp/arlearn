package org.celstec.arlearn2.tasks;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.delegators.generalitems.QueryGeneralItems;
import org.celstec.arlearn2.delegators.progressRecord.CreateProgressRecord;
import org.celstec.arlearn2.delegators.scoreRecord.CreateScoreRecord;
import org.celstec.arlearn2.tasks.beans.GenericBean;

import com.google.gdata.util.AuthenticationException;

public class AsyncTasksServlet extends HttpServlet {

	public static final String TASK = "task";
	public static final String SCOPE = "scope";
	public static final String AUTH = "auth";
	public static final String RUNID = "runId";
	public static final String GAMEID = "gameId";
	public static final String ACTION = "action";
	public static final String EMAIL = "email";
	public static final int UPDATE_PROGRESS = 1;
	public static final int UPDATE_SCORE = 2;
	public static final int UPDATE_GENERAL_ITEMS = 3;
    private static final Logger log = Logger.getLogger(AsyncTasksServlet.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		BeanDeserialiser bd = new BeanDeserialiser(request);
		GenericBean gb = bd.deserialize();
		gb.run();
	}

	protected void doGet_old(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int task = Integer.parseInt(req.getParameter(TASK));
		String authToken = req.getParameter(AUTH);
		long runId = 0;
		if (req.getParameter(RUNID)!=null) Long.parseLong(req.getParameter(RUNID));
		UsersDelegator qu = null;
		User u = null;
		try {
			qu = new UsersDelegator("auth=" + authToken);
			u = qu.getUserByEmail(runId, req.getParameter(EMAIL));
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		Action action = new Action();
		action.setRunId(runId);
		action.setAction(req.getParameter(ACTION));
		
		if (u != null)
			action.setUserEmail(u.getEmail());
			switch (task) {
			case UPDATE_PROGRESS:
				CreateProgressRecord cpr = new CreateProgressRecord(qu);
				
				cpr.updateProgress(action, u.getTeamId());
				break;
			case UPDATE_SCORE:
				CreateScoreRecord csr = new CreateScoreRecord(qu);
				csr.updateScore(action, u.getTeamId());
				
				break;
			case UPDATE_GENERAL_ITEMS:
				GeneralItemDelegator gid = new GeneralItemDelegator(qu);
				QueryGeneralItems qgi = new QueryGeneralItems(qu);
				gid.checkActionEffect(action, runId, u);
				break;
			}
			
	
	
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
