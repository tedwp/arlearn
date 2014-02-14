package org.celstec.arlearn2.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.run.*;
import org.celstec.arlearn2.beans.run.Thread;
import org.celstec.arlearn2.delegators.MessageDelegator;

import com.google.gdata.util.AuthenticationException;
import org.celstec.arlearn2.delegators.ThreadDelegator;

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

    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/thread")
    public String createThread(
            @HeaderParam("Authorization") String token,
            String thread,
            @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
            @DefaultValue("application/json") @HeaderParam("Accept") String accept)
            throws AuthenticationException {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);

        Object inThread = deserialise(thread, org.celstec.arlearn2.beans.run.Thread.class, contentType);
        if (inThread instanceof java.lang.String)
            return serialise(getBeanDoesNotParseException((String) inThread), accept);

        ThreadDelegator td = new ThreadDelegator(this);
        return serialise(td.createThread((Thread)inThread), accept);

    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/thread/runId/{runId}")
    public String getThreads(@HeaderParam("Authorization") String token, @PathParam("runId") Long runId,
                             @DefaultValue("application/json") @HeaderParam("Accept") String accept)
            throws AuthenticationException {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        ThreadDelegator td = new ThreadDelegator(this);


        return serialise(td.getThreads(runId), accept);
    }


}
