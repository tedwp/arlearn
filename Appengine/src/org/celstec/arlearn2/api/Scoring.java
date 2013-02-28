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

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.game.ScoreDefinition;

import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.ScoreDefinitionDelegator;
import org.celstec.arlearn2.delegators.scoreRecord.QueryScoreRecord;

import com.google.gdata.util.AuthenticationException;

@Path("/scoring")
public class Scoring extends Service {
	private static final Logger logger = Logger.getLogger(Scoring.class.getName());

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String pdString, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inScoreDefinition = deserialise(pdString, ScoreDefinition.class, contentType);
		if (inScoreDefinition instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inScoreDefinition), accept);
		
		RunDelegator rd = new RunDelegator(token);
		ScoreDefinitionDelegator sdd = new ScoreDefinitionDelegator(token);
		ScoreDefinition proDef = sdd.createScoreDefinition((ScoreDefinition) inScoreDefinition);
		return serialise(proDef, accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runId}")
	public String getScore(@HeaderParam("Authorization") String token, @PathParam("runId") Long runId, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		QueryScoreRecord qsr = new QueryScoreRecord(token);
		return serialise(qsr.getUserScore(runId), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/definition/game/{gameId}/action/{action}/generalItemId/{generalItemId}/generalItemType/{generalItemType}")
	public String getDefinitions(@HeaderParam("Authorization") String token, @PathParam("gameId") Long gameId, @PathParam("action") String actionString,
			@PathParam("generalItemId") Long generalItemId, @PathParam("generalItemType") String generalItemType, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		ScoreDefinitionDelegator sdd = new ScoreDefinitionDelegator(token);

		if (actionString.equals("null"))
			actionString = null;
		if (generalItemId.equals("null"))
			generalItemId = null;
		if (generalItemType.equals("null"))
			generalItemType = null;

		Action action = new Action();
		action.setAction(actionString);
		action.setGeneralItemId(generalItemId);
		action.setGeneralItemType(generalItemType);

		return serialise(sdd.getScoreDefinitionForAction(gameId, action), accept);

	}

	// TODO createScoreDefinitionForAction

	// TODO queryScoreDefinitionForAction -> List of ScoreDefinitions for Action

	// TODO calculate score for action and create ScoreRecord

	// TODO calculate score for user

	// TODO calculate score for team

	// TODO calculate score overview (all teams, all users)

}
