package org.celstec.arlearn2.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.run.Message;
import org.celstec.arlearn2.delegators.MessageDelegator;

import com.google.gdata.util.AuthenticationException;

@Path("/messages")
public class Messages extends Service {

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/userId/{userId}")
	public String sendMessage(@HeaderParam("Authorization") String token, 
			String messageString, 
			@PathParam("userId") String userId,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType, 
			@HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inRun = deserialise(messageString, Message.class, contentType);
		if (inRun instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inRun), accept);
		Message message = (Message) inRun;
		
		MessageDelegator rd = new MessageDelegator(this);
		return serialise(rd.sendMessage(message, userId), accept);
	}
}
