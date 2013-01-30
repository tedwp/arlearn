package org.celstec.arlearn2.api;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.generalItem.OpenBadge;
import org.celstec.arlearn2.beans.generalItem.OpenBadgeAssertion;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.cache.GeneralitemsCache;
import org.celstec.arlearn2.cache.MyGamesCache;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.jdo.manager.GameManager;
import org.celstec.arlearn2.jdo.manager.GeneralItemManager;
import org.celstec.arlearn2.jdo.manager.RunManager;
import org.celstec.arlearn2.jdo.manager.UserManager;

import com.google.gdata.util.AuthenticationException;

@Path("/myRuns")
public class MyRuns extends Service {
	private static final Logger logger = Logger.getLogger(MyRuns.class.getName());

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getRuns(@HeaderParam("Authorization") String token, @DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(token);
		return serialise(rd.getRuns(), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/participate")
	public String getParticipateRuns(
			@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@QueryParam("from") Long from,
			@QueryParam("until") Long until
			) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(token);
		if (from == null && until == null) {
			return serialise(rd.getParticipateRuns(), accept);	
		}
		return serialise(rd.getParticipateRuns(from, until), accept);
	}

	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}")
	public String getRun(@HeaderParam("Authorization") String token, 
			@PathParam("runIdentifier") Long runIdentifier,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(token);
		return serialise(rd.getRun(runIdentifier), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/config/runId/{runIdentifier}")
	public String getConfig(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {

		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(token);
		Config c = rd.getConfig(runIdentifier);
		return serialise(c, accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/selfRegister/tagId/{tagId}")
	public String selfRegister(@HeaderParam("Authorization") String token, 
			@PathParam("tagId") String tagId, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {

		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(token);
		return serialise(rd.selfRegister(tagId), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/selfRegister/runId/{runId}")
	public String selfRegisterWithRunId(@HeaderParam("Authorization") String token, 
			@PathParam("runId") Long runId, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {

		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(token);
		return serialise(rd.selfRegister(runId), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/gameId/{gameIdentifier}")
	public RunList getRuns(@HeaderParam("Authorization") String token, @PathParam("gameIdentifier") String gameIdentifier, @HeaderParam("Accept") String accept) throws AuthenticationException {
		// TODO
		return null;
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String runString, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@PathParam("gameIdentifier") String gameIdentifier, @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inRun = deserialise(runString, Run.class, contentType);
		if (inRun instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inRun), accept);
		Run run = (Run) inRun;
		run.setDeleted(false);
		RunDelegator rd = new RunDelegator(token);
		return serialise(rd.createRun(run), accept);
	}
	
	@PUT
	@Path("/runId/{runIdentifier}")
	public String updateRun(@HeaderParam("Authorization") String token, String runString,
			@PathParam("runIdentifier") Long runIdentifier, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inRun = deserialise(runString, Run.class, contentType);
		if (inRun instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inRun), accept);
		Run run = (Run) inRun;
		run.setDeleted(false);
		RunDelegator rd = new RunDelegator(token);
		return serialise(rd.updateRun(run, runIdentifier), accept);
	}

	@DELETE
	@Path("/runId/{runIdentifier}")
	public String deleteRun(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("gameIdentifier") String gameIdentifier,
			@HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(token);
		return serialise(rd.deleteRun(runIdentifier), accept);
	}
	
	@GET
	@Path("/badge/{runIdentifier}/{itemId}/{userId}")
	 @Produces("application/json")
	public String getBadge(@PathParam("runIdentifier") Long runId, @PathParam("itemId") String itemId, @PathParam("userId") String email) {
		List<Run> runList = RunManager.getRuns(runId, null, null, null, null);
		if (runList.isEmpty())
			return null;
		Run r = runList.get(0);
		List<User> users = UserManager.getUserList(null, email, null, runId);
		if (users.isEmpty())
			return null;
		GeneralItemList gil = new GeneralItemList();
			gil.setGeneralItems(GeneralItemManager.getGeneralitems(r.getGameId(), itemId, null));
			GeneralitemsCache.getInstance().putGeneralItemList(gil, r.getGameId(), itemId, null);
		if (gil == null || gil.getGeneralItems() == null || gil.getGeneralItems().isEmpty()) {
			return null;
		}
		GeneralItem gi = gil.getGeneralItems().get(0);
		OpenBadge ob = (OpenBadge) gi;
		OpenBadgeAssertion ou = new OpenBadgeAssertion();
		ou.setRecipient(users.get(0).getFullEmail());
		ou.setEvidence(ob.getEvidence());
//		ou.setExpires("2013-06-01");
//		ou.setIssued_on("2012-10-11");
		ou.setBadge_name(ob.getName());
		ou.setBadge_image(ob.getImage());
		ou.setBadge_description(ob.getDescription());
		ou.setBadge_criteria("/badges/html5-basic");
		ou.setBadge_issuer_origin(ob.getBadgeUrl());
		ou.setBadge_issuer_name("celstec");
		return serialise(ou, "application/json").replace("\\/", "/");
	}
}
