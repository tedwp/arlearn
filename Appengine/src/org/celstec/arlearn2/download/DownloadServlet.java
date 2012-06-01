package org.celstec.arlearn2.download;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.celstec.arlearn2.beans.GamePackage;
import org.celstec.arlearn2.beans.RunPackage;
import org.celstec.arlearn2.beans.game.ScoreDefinitionList;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.TeamList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.UserList;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.ScoreDefinitionDelegator;
import org.celstec.arlearn2.delegators.TeamsDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.delegators.generalitems.QueryGeneralItems;

import com.google.gdata.util.AuthenticationException;

import java.io.*;

public class DownloadServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, FileNotFoundException {
		String fileName = "test";
		if (request.getParameter("runId") != null) fileName = "run."+request.getParameter("runId");
		if (request.getParameter("gameId") != null) fileName = "game."+request.getParameter("gameId");
		response.setContentType("application/octet-stream");
		response.setHeader( "Content-Disposition", "attachment; filename=\""+fileName+".json\"" );
		ServletContext ctx = getServletContext();

		String type = request.getParameter("type");
		String returnString = "";
		if ("run".equals(type)) returnString = returnRun(request);
		if ("game".equals(type)) returnString = returnGame(request);
		PrintWriter pw = response.getWriter();
		pw.write(returnString);
		pw.close();
	}

	
	private String returnGame(HttpServletRequest request) {
		long gameId = Long.parseLong(request.getParameter("gameId"));
		String auth = "auth="+request.getParameter("auth");
		GamePackage gp = new GamePackage();
		try {
			GameDelegator gd = new GameDelegator(auth);
			gp.setGame(gd.getGame(gameId));
			QueryGeneralItems qgi = new QueryGeneralItems(gd);
			gp.setGeneralItems(qgi.getGeneralItems(gameId).getGeneralItems());
			ScoreDefinitionDelegator sd = new ScoreDefinitionDelegator(gd);
			ScoreDefinitionList list = new ScoreDefinitionList();
			list.setScoreDefinitions(sd.getScoreDefinitionsList(gameId, null, null, null));
			gp.setScoreDefinitions(list);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		return gp.toString();
	}
	private String returnRun(HttpServletRequest request) {
		long runId = Long.parseLong(request.getParameter("runId"));
		String auth = "auth="+request.getParameter("auth");
		
		RunPackage rp = new RunPackage();
		try {
			RunDelegator rd = new RunDelegator(auth);
			rp.setRun(rd.getRun(runId));
			
			TeamsDelegator td = new TeamsDelegator(rd);
			TeamList tl = td.getTeams(runId);
			if (tl != null) rp.setTeams(tl.getTeams());
			
			UsersDelegator ud = new UsersDelegator(rd);
			UserList ul = ud.getUsers(runId);
			if (ul != null){
				for (User u: ul.getUsers()) {
					for (Team t : rp.getTeams()) {
						if (t.getTeamId().equals(u.getTeamId())){
							t.addUser(u);
						}
					}
				}
			}
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		rp.getRun().setGame(null);
		return rp.toString();
	}
	
	
}
