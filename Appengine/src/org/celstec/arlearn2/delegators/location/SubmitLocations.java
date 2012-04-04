package org.celstec.arlearn2.delegators.location;

import java.io.IOException;
import java.util.Iterator;

import org.celstec.arlearn2.beans.run.Location;
import org.celstec.arlearn2.beans.run.LocationList;
import org.celstec.arlearn2.delegators.GoogleDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.delegators.notification.LocationNotification;
import org.celstec.arlearn2.delegators.notification.ScoreNotification;
import org.celstec.arlearn2.fusion.CSV;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.jdo.manager.LocationManager;
import org.celstec.arlearn2.tasks.XmppNotificationServlet;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class SubmitLocations extends GoogleDelegator {

	public SubmitLocations(String authToken) throws AuthenticationException {
		super(authToken);
	}

	public void submitLocation(Long delta, Location location, long runId) {
		LocationList locList = new LocationList();
		locList.addLocation(location);
		submitLocations(delta, locList, runId);
		
	}
		public void submitLocations(Long delta, LocationList locList, long runId) {
			UsersDelegator qu = new UsersDelegator(this);
			String user = qu.getCurrentUserAccount();
//		String user = UserLoggedInManager.getUser(authToken);
		if (user != null && user.length() > 0) {
			try {
				Location lastLoc =LocationManager.addLocation(runId, user, locList);
				if (lastLoc != null) {
					Queue queue = QueueFactory.getDefaultQueue();
					queue.add(TaskOptions.Builder.withUrl("/xmpp/notify")
							.param(XmppNotificationServlet.TASK, "" + XmppNotificationServlet.LOCATION_NOTIFICATION)
							.param(XmppNotificationServlet.AUTH, authToken)
							.param(LocationNotification.LAT, "" + lastLoc.getLat())
							.param(LocationNotification.LNG, "" + lastLoc.getLng())
							.param(XmppNotificationServlet.RUNID, "" + runId)
							.param(XmppNotificationServlet.SCOPE, "" + LocationNotification.TEAM));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

//	private int createLocationTable(Long gameRunId) throws IOException, ServiceException {
//		CSV csv = fc.runCreate("CREATE TABLE " + Location.TABLE_NAME + "_" + gameRunId + " (userId: STRING, location: LOCATION, timestamp: NUMBER)");
//		return Integer.parseInt(csv.getValue(0, csv.getColumnIndex("tableid")));
//	}
//
//	private void addRowsToLocationTable(String statement) throws IOException, ServiceException {
//		fc.runCreate(statement);
//	}
//
//	private String insertString(Long tableId, String user, Double lat, Double lng, long timestamp) {
//		return "INSERT INTO " + tableId + " (userId, location, timestamp) VALUES ('" + user + "', '" + lat + " " + lng + "', " + timestamp + ");\n";
//	}
}
