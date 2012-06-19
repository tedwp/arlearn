package org.celstec.arlearn2.delegators;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.LocationUpdateConfig;
import org.celstec.arlearn2.beans.run.LocationUpdate;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;
import org.celstec.arlearn2.jdo.UserLoggedInManager;

import com.google.gdata.util.AuthenticationException;

public class LocationDelegator extends GoogleDelegator{
	
	private static final Logger logger = Logger.getLogger(LocationDelegator.class.getName());

	public LocationDelegator(String authtoken) throws AuthenticationException {
		super(authtoken);
	}
	
	public LocationDelegator(GoogleDelegator gd) {
		super(gd);
	}
	
	public void processLocation(long runId, double lat, double lng) {
		UsersDelegator qu = new UsersDelegator(this);
		String myAccount = qu.getCurrentUserAccount();
		RunDelegator rd = new RunDelegator(this);
		Run r = rd.getRun(runId);
		GameDelegator gd = new GameDelegator(this);
		Game g = gd.getUnOwnedGame(r.getGameId());
		List<LocationUpdateConfig> list = getApplicableConfigItems(g.getConfig().getLocationUpdates());
		System.out.println("run" + r);
		System.out.println("game" + g);
		UsersDelegator ud = new UsersDelegator(this);
		System.out.println("users :"+ ud.getUsers(runId));
		LocationUpdate lu = new LocationUpdate();
		lu.setLat(lat);
		lu.setLng(lng);
		lu.setRecepientType(LocationUpdate.PLAYER);
		for (LocationUpdateConfig luc: list) {
			if ("team".equals(luc.getScope())) {
				
			}
			if ("all".equals(luc.getScope())) {
				for (User u: ud.getUsers(runId).getUsers()){
					lu.setAccount(u.getEmail());
					ChannelNotificator.getInstance().notify(u.getEmail(), lu.toString());
				}
			}
		}
		lu.setRecepientType(LocationUpdate.MODERATOR);
		lu.setAccount(r.getOwner());
		ChannelNotificator.getInstance().notify(r.getOwner(), lu.toString());
 
		//todo fetch team and users.
	}
	
	private List<LocationUpdateConfig> getApplicableConfigItems(List<LocationUpdateConfig> list) {
		ArrayList<LocationUpdateConfig> returnList = new ArrayList<LocationUpdateConfig>();
		for (LocationUpdateConfig luc: list) {
			returnList.add(luc);
		}
		return returnList;
	}

}
