package org.celstec.arlearn2.api;

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

import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.TeamList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.TeamsDelegator;

import com.google.gdata.util.AuthenticationException;

@Path("/team")
public class Teams extends Service {

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}")
	public String getTeams(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		TeamsDelegator td = new TeamsDelegator(verifyCredentials(token));
		return serialise(td.getTeams(runIdentifier), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/teamId/{teamId}")
	public String getTeam(@HeaderParam("Authorization") String token, @PathParam("teamId") String teamId, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		TeamsDelegator td = new TeamsDelegator(verifyCredentials(token));
		return serialise(td.getTeam(teamId), accept);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String teamString, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		Object inTeam = deserialise(teamString, Team.class, contentType);
		if (inTeam instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inTeam), accept);
		Team team = (Team) inTeam;
		
		TeamsDelegator td = new TeamsDelegator(verifyCredentials(token));
		return serialise(td.createTeam(team), accept);

	}

	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/teamId/{teamId}")
	public String deleteTeam(@HeaderParam("Authorization") String token, @PathParam("teamId") String teamId, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		TeamsDelegator td = new TeamsDelegator(verifyCredentials(token));
		td.deleteTeam(teamId);
		return null;

	}

}
