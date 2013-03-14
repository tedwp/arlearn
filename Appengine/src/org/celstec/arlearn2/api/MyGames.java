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

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.MapRegion;
import org.celstec.arlearn2.beans.notification.GameModification;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import com.google.gdata.util.AuthenticationException;

@Path("/myGames")
public class MyGames extends Service {

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	public String getGames(@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@QueryParam("from") Long from,
			@QueryParam("until") Long until) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		if (from == null) {
			from = 0l;
		}
		GameDelegator qg = new GameDelegator(token);
		return serialise(qg.getGames(from, until), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/participate")
	public String getGamesParticipate(
			@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@QueryParam("from") Long from,
			@QueryParam("until") Long until
			) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GameDelegator qg = new GameDelegator(token);
		if (from == null && until == null) {
			return serialise(qg.getParticipateGames(), accept);	
		}
		return serialise(qg.getParticipateGames( from, until), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/gameId/{gameIdentifier}")
	public String getGame(@HeaderParam("Authorization") String token, @PathParam("gameIdentifier") Long gameIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GameDelegator qg = new GameDelegator(token);
		return serialise(qg.getGame(gameIdentifier), accept);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String gameString, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		Object inGame = deserialise(gameString, Game.class, contentType);
		if (inGame instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inGame), accept);
		
		GameDelegator cg = new GameDelegator(token);
		return serialise(cg.createGame((Game) inGame, GameModification.CREATED), accept);
	}

	@DELETE
	@Path("/gameId/{gameIdentifier}")
	public String deleteGame(@HeaderParam("Authorization") String token, @PathParam("gameIdentifier") Long gameIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GameDelegator cg = new GameDelegator(token);
		return serialise(cg.deleteGame(gameIdentifier), accept);
	}
	
	//ROLES
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/config/gameId/{gameIdentifier}")
	public String getRoles(@HeaderParam("Authorization") String token, @PathParam("gameIdentifier") Long gameIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GameDelegator qg = new GameDelegator(token);
		Game g = qg.getGame(gameIdentifier);
		if (g == null) return "{}";
		if (g.getError() != null) serialise(qg.getGame(gameIdentifier), accept);
		if (g.getConfig() != null && g.getConfig() != null) {
			return serialise(g.getConfig(), accept);
		} else {
			return "{}";
		}
		
	}
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/config/manualtrigger/gameId/{gameIdentifier}")
	public String installManualTrigger(@HeaderParam("Authorization") String token, 
			String generalItem, 
			@PathParam("gameIdentifier") Long gameIdentifier,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GameDelegator qg = new GameDelegator(token);
		return serialise(qg.addManualTrigger(gameIdentifier, generalItem), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("config/removeManualTrigger/gameId/{gameIdentifier}/itemId/{itemIdentifier}")
	public String removeManualTrigger(@HeaderParam("Authorization") String token, 
			@PathParam("gameIdentifier") Long gameIdentifier, 
			@PathParam("itemIdentifier") Long itemIdentifier, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GameDelegator qg = new GameDelegator(token);
		return serialise(qg.removeTrigger(gameIdentifier, itemIdentifier), accept);

	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/config/gameId/{gameIdentifier}/role")
	public String createRole(@HeaderParam("Authorization") String token, String roleString, 
			@PathParam("gameIdentifier") Long gameIdentifier,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GameDelegator qg = new GameDelegator(token);
		return serialise(qg.createRole(gameIdentifier, roleString), accept);
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/config/gameId/{gameIdentifier}/mapType")
	public String setMapType(@HeaderParam("Authorization") String token, String mapType, 
			@PathParam("gameIdentifier") Long gameIdentifier,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GameDelegator qg = new GameDelegator(token);
		return serialise(qg.setMapType(gameIdentifier, Integer.parseInt(mapType)), accept);
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/config/gameId/{gameIdentifier}/withMap")
	public String setWithMap(@HeaderParam("Authorization") String token, String booleanString, 
			@PathParam("gameIdentifier") Long gameIdentifier,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GameDelegator qg = new GameDelegator(token);
		return serialise(qg.setWithMap(gameIdentifier, Boolean.parseBoolean(booleanString)), accept);
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/config/gameId/{gameIdentifier}/mapRegion")
	public String addMapRegion(@HeaderParam("Authorization") String token, String regions, 
			@PathParam("gameIdentifier") Long gameIdentifier,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GameDelegator qg = new GameDelegator(token);
		try {
			List<MapRegion> regionsBean = ListDeserializer.toBean(new JSONArray(regions), MapRegion.class);
			return serialise(qg.setRegions(gameIdentifier, regionsBean), accept);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
