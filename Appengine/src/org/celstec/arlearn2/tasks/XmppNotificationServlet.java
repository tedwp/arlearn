package org.celstec.arlearn2.tasks;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.celstec.arlearn2.delegators.notification.GeneralitemNotification;
import org.celstec.arlearn2.delegators.notification.LocationNotification;
import org.celstec.arlearn2.delegators.notification.Notification;
import org.celstec.arlearn2.delegators.notification.ScoreNotification;

public class XmppNotificationServlet extends HttpServlet{

	public static final String TASK = "task";
	public static final String USER = "user";
	public static final String SCOPE = "scope";
	public static final String AUTH = "authToken";
	public static final String RUNID = "RUNID";
	public static final int SCORE_NOTIFICATION = 1;
	public static final int GENERALITEM_NOTIFICATION = 2;
	public static final int LOCATION_NOTIFICATION = 3;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int task = Integer.parseInt(req.getParameter(TASK));
		String authToken = req.getParameter(AUTH);
		int runId = Integer.parseInt(req.getParameter(RUNID));
		int scope = Integer.parseInt(req.getParameter(SCOPE));
		switch (task) {
		case SCORE_NOTIFICATION:
			ScoreNotification sn = new ScoreNotification(scope, runId, authToken);
			if (req.getParameter(ScoreNotification.SCORE) != null) {
				sn.notifyPlayers(Integer.parseInt(req.getParameter(ScoreNotification.SCORE)));
			} else {
				sn.notifyPlayers();	
			}
			break;
		case GENERALITEM_NOTIFICATION:
			GeneralitemNotification gin = new GeneralitemNotification(scope, runId, authToken);
			gin.itemDeleted(req.getParameter(GeneralitemNotification.ITEMID));
			break;
		case LOCATION_NOTIFICATION:
			LocationNotification ln = new LocationNotification(scope, runId, authToken);
			double lat = Double.parseDouble(req.getParameter(LocationNotification.LAT));
			double lng = Double.parseDouble(req.getParameter(LocationNotification.LNG));
			ln.locationChanged(lat, lng);
			break;
		default:
			break;
		}
		
		
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	
	
}
