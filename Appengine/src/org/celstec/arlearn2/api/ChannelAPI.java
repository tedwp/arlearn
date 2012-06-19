package org.celstec.arlearn2.api;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.run.LocationUpdate;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gdata.util.AuthenticationException;

@Path("/channelAPI")
public class ChannelAPI extends Service{

	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/token")
	public String getToken(@HeaderParam("Authorization") String token) throws AuthenticationException {
		try {
		if (!validCredentials(token))
			return null;
		UsersDelegator qu = new UsersDelegator(token);
		String myAccount = qu.getCurrentUserAccount();
		
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		return "{ \"token\": \""+channelService.createChannel(myAccount)+"\"}";

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/token2m")
	public String getShortToken(@HeaderParam("Authorization") String token) throws AuthenticationException {
		if (!validCredentials(token))
			return null;
		UsersDelegator qu = new UsersDelegator(token);
		String myAccount = qu.getCurrentUserAccount();
		
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		return "clientId: "+myAccount+" token2m " + channelService.createChannel(myAccount, 2);

	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/run/{account}")
	public String getRun(@PathParam("account") String account) throws AuthenticationException {		
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		Run r = new Run();
		r.setGameId(123l);
		r.setTitle("een game");
		channelService.sendMessage(new ChannelMessage(account, toJson(r)));
		return "sent";
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/location/{from}/{to}/{lat}/{lng}")
	public String loc(@PathParam("from") String from,@PathParam("to") String to,
			@PathParam("lat") Double lat,@PathParam("lng") Double lng) throws AuthenticationException {		
		LocationUpdate lu = new LocationUpdate();
		lu.setLat(lat);
		lu.setLng(lng);
		lu.setRecepientType(LocationUpdate.MODERATOR);
		lu.setAccount(from);
		ChannelNotificator.getInstance().notify(to, lu.toString());
		return "ok";
	}

}
