package org.celstec.arlearn2.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.game.VariableDefinition;
import org.celstec.arlearn2.delegators.VariableDelegator;

import com.google.gdata.util.AuthenticationException;

@Path("/variables")
public class Variables extends Service {

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/game/{gameId}")
	public String sendMessage(@HeaderParam("Authorization") String token, 
			String messageString, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType, 
			@HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inRun = deserialise(messageString, VariableDefinition.class, contentType);
		if (inRun instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inRun), accept);
		VariableDefinition variableDef = (VariableDefinition) inRun;
		
		VariableDelegator rd = new VariableDelegator(this);
		return serialise(rd.createVariableDefinition(variableDef), accept);
	}
}
