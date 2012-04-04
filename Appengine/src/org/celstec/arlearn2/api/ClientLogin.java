package org.celstec.arlearn2.api;


import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.tasks.beans.UpdateScore;

import com.google.gdata.client.ClientLoginAccountType;
import com.google.gdata.client.GoogleAuthTokenFactory.UserToken;
import com.google.gdata.client.GoogleService;
import com.google.gdata.util.AuthenticationException;

@Path("/login")
public class ClientLogin extends Service {
	private static final Logger log = Logger.getLogger(ClientLogin.class.getName());

	private GoogleService service;
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes("text/plain")
	public String clientData(String data, @DefaultValue("application/json") @HeaderParam("Accept") String accept) {
		String username = data.substring(0, data.indexOf("\n"));
		String password = data.substring(data.indexOf("\n")+1);
		service = new GoogleService("fusiontables", "ARLearn2");
		AuthResponse ar = new AuthResponse();
		try {
			service.setUserCredentials(username, password,ClientLoginAccountType.GOOGLE);
			
		} catch (AuthenticationException e) {
			log.log(Level.SEVERE, "failed "+e.getMessage());
			ar.setError("Authentication failed: "+e.getMessage());
			return null;
		}
		String token = ((UserToken)service.getAuthTokenFactory().getAuthToken()).getValue();
		ar.setAuth(token);
		UserLoggedInManager.submitUser(username, token);		
		return serialise(ar, accept);
	}
	
}
