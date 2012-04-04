package org.celstec.arlearn2.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.GameDelegator;

import com.google.gdata.util.AuthenticationException;

@Path("/myGames")
public class MyGames extends Service {

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getGames(@HeaderParam("Authorization") String token, @DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GameDelegator qg = new GameDelegator(token);
		return serialise(qg.getGames(), accept);
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
		return serialise(cg.createGame((Game) inGame), accept);
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
}
