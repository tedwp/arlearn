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
package org.celstec.arlearn2.delegators.notification;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.UserScore;
import org.celstec.arlearn2.jdo.UserLoggedInManager;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.google.gdata.util.AuthenticationException;

public class ScoreNotification extends Notification{

	public ScoreNotification(int scope, long runId, String authToken) {
		super(scope, runId, authToken);
	}

	
	public void notifyScoreUpdate(UserScore us, String email) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("userScore",  ""+ us.getUserScore());
		hm.put("teamScore",  ""+ us.getTeamScore());
		hm.put("allScore",  ""+ us.getAllScore());
		hm.put("totalScore",  ""+ us.getTotalScore());

		notify(email, "UpdateScore", hm);

	}
	public void notifyPlayers(int score) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("score",  ""+ score);
		notify("UpdateScore", hm);
	}
	public void notifyPlayers() {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("score",  ""+ getRunId());
		notify("UpdateScore", hm);
		
//		try {
//			QueryUser qu = new QueryUser(getAuthToken());
//			User me = qu.getUser(getRunId(), user);
//			if (me == null) {
//				log.log(Level.SEVERE, "could not retrieve corresponding user");
//				return;
//			}
//			if (getScope() == USER) {
//				notify(me);
//			} else {
//				Iterator<User> it = qu.getUsers(getRunId()).getUsers().iterator();
//				while (it.hasNext()) {
//					notifyUser((User) it.next(), me.getTeamId());
//				}
//			}
//		} catch (AuthenticationException e) {
//			e.printStackTrace();
//		}

	}
    private static final Logger log = Logger.getLogger(ScoreNotification.class.getName());


//	private void notifyUser(User u, String team) {
//		switch (getScope()) {
//		case TEAM:
//			if (u.getTeamId().equals(team))
//				break;
//		case ALL:
//			notify(u);
//			break;
//
//		}
//
//	}
//
//	private void notify(User u) {
//		HashMap<String, String> hm = new HashMap<String, String>();
//		hm.put("score", "10");
//		notify(u, "UpdateScore", hm);
//	}
}
