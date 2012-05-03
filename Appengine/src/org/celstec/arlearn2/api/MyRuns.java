package org.celstec.arlearn2.api;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.RunDelegator;

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
	public String getParticipateRuns(@HeaderParam("Authorization") String token, @DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		RunDelegator rd = new RunDelegator(token);
		return serialise(rd.getParticipateRuns(), accept);
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
		logger.log(Level.SEVERE, "in get config" );

		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		logger.log(Level.SEVERE, "in get config2" );
		RunDelegator rd = new RunDelegator(token);
		Config c = rd.getConfig(runIdentifier);
		logger.log(Level.SEVERE, "config "+c);

		return serialise(c, accept);
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
		
		RunDelegator rd = new RunDelegator(token);
		return serialise(rd.createRun(run), accept);
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
}
