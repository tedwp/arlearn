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
import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.MapRegion;
import org.celstec.arlearn2.beans.notification.GameModification;
import org.celstec.arlearn2.delegators.GameAccessDelegator;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.tasks.beans.GameSearchIndex;
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
	@Path("/gameAccess")
	public String getGameAccess(@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@QueryParam("from") Long from,
			@QueryParam("until") Long until) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		if (from == null) {
			from = 0l;
		}
		GameAccessDelegator qg = new GameAccessDelegator(token);
		return serialise(qg.getGamesAccess(from, until), accept);
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
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/access/gameId/{gameIdentifier}/account/{account}/accessRight/{accessRight}")
	public String access(@HeaderParam("Authorization") String token, 
			@PathParam("gameIdentifier") Long gameIdentifier, 
			@PathParam("account") String account, 
			@PathParam("accessRight") Integer accessRight, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GameAccessDelegator gad = new GameAccessDelegator(token);
		gad.provideAccessWithCheck(gameIdentifier, account, accessRight);
		return "{}";
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/access/gameId/{gameIdentifier}")
	public String accessList(@HeaderParam("Authorization") String token, 
			@PathParam("gameIdentifier") Long gameIdentifier, 
			@PathParam("account") String account, 
			@PathParam("accessRight") Integer accessRight, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GameAccessDelegator gad = new GameAccessDelegator(token);

		return serialise(gad.getAccessList(gameIdentifier), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/removeAccess/gameId/{gameIdentifier}/account/{account}")
	public String removeAccess(@HeaderParam("Authorization") String token, 
			@PathParam("gameIdentifier") Long gameIdentifier, 
			@PathParam("account") String account, 
			@PathParam("accessRight") Integer accessRight, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		//TODO check if owner
		GameAccessDelegator gad = new GameAccessDelegator(token);
		gad.removeAccessWithCheck(gameIdentifier, account);
		return "{}";
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
	@Path("/config/gameId/{gameIdentifier}/sharing/{sharingType}")
	public String setSharing(@HeaderParam("Authorization") String token,
			@PathParam("gameIdentifier") Long gameIdentifier,
			@PathParam("sharingType") Integer sharingType,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		if (sharingType == null || sharingType <=0 || sharingType >= 4) {
			Bean error = new Bean();
			error.setError("invalid sharing type: "+sharingType);
			error.setErrorCode(0);
			return error.toString();
		}
		GameDelegator qg = new GameDelegator(token);
		Game g = qg.setSharing(gameIdentifier, sharingType);
		
		return serialise(g, accept);
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
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/search")
	public String search(@HeaderParam("Authorization") String token, 
			String searchQuery, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GameDelegator qg = new GameDelegator(token);
			return serialise(qg.search(searchQuery), accept);
	}
	
}
