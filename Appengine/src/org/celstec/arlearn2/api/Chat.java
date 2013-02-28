/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.api;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.notification.GeneralitemNotification;
import org.celstec.arlearn2.delegators.notification.LocationNotification;
import org.celstec.arlearn2.delegators.notification.ScoreNotification;
import org.celstec.arlearn2.tasks.AsyncTasksServlet;
import org.celstec.arlearn2.tasks.XmppNotificationServlet;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
@Path("/chat")
public class Chat {

	//TODO this is a test class, not really used for chatting. This must be cleaned up thus.
	
	
    private static final Logger log = Logger.getLogger(AsyncTasksServlet.class.getName());

	
	@GET
	@Path("/message/{message}")
	public User flush(@PathParam("message") String message, @HeaderParam("Authorization") String token) {
		log.severe("test message");
		log.log(Level.SEVERE, "in chat class");
//		DeleteGame dg = new DeleteGame();
//		dg.setToken("token");
//		dg.setGameId(12345l);
//		dg.scheduleTask();
		Queue queue = QueueFactory.getDefaultQueue();
	    queue.add(TaskOptions.Builder.withUrl("/xmpp/notify")
	    		.param(XmppNotificationServlet.TASK, ""+XmppNotificationServlet.SCORE_NOTIFICATION)
	    		.param(XmppNotificationServlet.AUTH, token)
	    		.param(XmppNotificationServlet.RUNID, "1110705")
	    		.param(ScoreNotification.SCORE, message)
	    		.param(XmppNotificationServlet.SCOPE, ""+ScoreNotification.USER)
	    		);
	    queue.add(TaskOptions.Builder.withUrl("/xmpp/notify")
	    		.param(XmppNotificationServlet.TASK, ""+XmppNotificationServlet.GENERALITEM_NOTIFICATION)
	    		.param(XmppNotificationServlet.AUTH, token)
	    		.param(XmppNotificationServlet.RUNID, "1110705")
	    		.param(GeneralitemNotification.ITEMID, "item id")
	    		.param(XmppNotificationServlet.SCOPE, ""+ScoreNotification.USER)
	    		);
	    return new User();
	}
	
	@GET
	@Path("/location/{runId}/{user}/{lat}/{lng}/")
	public User location(
			@PathParam("runId") String runId, 
			@PathParam("user") String user, 
			@PathParam("lat") double lat, 
			@PathParam("lng") double lng, 
			@HeaderParam("Authorization") String token) {
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/xmpp/notify")
				.param(XmppNotificationServlet.TASK, "" + XmppNotificationServlet.LOCATION_NOTIFICATION)
				.param(XmppNotificationServlet.AUTH, token)
				.param(LocationNotification.LAT, "" + lat)
				.param(LocationNotification.LNG, "" + lng)
				.param(XmppNotificationServlet.RUNID, "" + runId)
				.param(XmppNotificationServlet.SCOPE, "" + LocationNotification.TEAM));
	    return new User();
	}
	
	@GET
	@Path("/updateRun/{runId}/email/{email}")
	public String createRun(@HeaderParam("Authorization") String token,
			@PathParam("runId") long runId, @PathParam("email") String email) {
		try{
		} catch (Exception e) {
			return e.getMessage();
		}
		return "ok";
	}
	
	@GET
	@Path("/test")
	public String test() {
		debug("stefaan.ternier@gmail.com");
		return "test ok";
	}
	
	public static void debug(String email) {
		 JID jid = new JID(email);
	        String msgBody = "Someone has sent you a gift on Example.com. To view: http://example.com/gifts/";
	        Message msg = new MessageBuilder()
	            .withRecipientJids(jid)
	            .withBody(msgBody)
	            .build();
	        
			log.severe("message body "+msg.toString());

	        boolean messageSent = false;
	        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
	        SendResponse status = xmpp.sendMessage(msg);
	        messageSent = (status.getStatusMap().get(jid) == SendResponse.Status.SUCCESS);
	        log.severe("message was sent successfully "+messageSent+" "+status.getStatusMap().toString());
	}
}
