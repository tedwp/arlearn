package org.celstec.arlearn2.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.google.gdata.util.AuthenticationException;

@Path("/config")
public class NotificationAPI {

	
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, 
			String deviceToken,
			int deviceType,
			String userId,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		
		return null;
	}
}
