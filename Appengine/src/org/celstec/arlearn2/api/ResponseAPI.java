package org.celstec.arlearn2.api;

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.ResponseDelegator;

import com.google.gdata.util.AuthenticationException;

@Path("/response")
public class ResponseAPI extends Service {

	// @POST
	// @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	// @Path("/runId/{runIdentifier}/generalItemId/{generalItemId}")
	// public Response put(@HeaderParam("Authorization") String token, String
	// json,
	// @PathParam("runIdentifier") Long runIdentifier,
	// @PathParam("generalItemId") String generalItemIdentifier) {
	// try {
	// Response r = new Response();
	// r.setGeneralItemId(generalItemIdentifier);
	// r.setResponseValue(json);
	// CreateResponse cr = new CreateResponse(token);
	// return cr.createResponse(runIdentifier, r);
	// } catch (AuthenticationException e) {
	// Response response = new Response();
	// response.setError("authentication did not succeed");
	// return response;
	// }
	//
	// }

	// TODO work out and return responses
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}/account/{account}")
	public String getAnswers(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("account") String account, @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		ResponseDelegator rd = new ResponseDelegator(token);
		return serialise(rd.getResponses(runIdentifier, account), accept);
//		List<Response> list = rd.getResponses(runIdentifier, account);
//		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
//			Response response = (Response) iterator.next();
//			System.out.println(response.getResponseValue());
//		}
//
//		return "true";
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String rString,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inResponse = deserialise(rString, Response.class, contentType);
		if (inResponse instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inResponse), accept);
		Response r = (Response) inResponse;
		
		ResponseDelegator cr = new ResponseDelegator(token);
		if (r.getRevoked() != null && r.getRevoked()) {
			return serialise(cr.revokeResponse(r), accept);
		} else {
			return serialise(cr.createResponse(r.getRunId(), r), accept);
		}
	}

}