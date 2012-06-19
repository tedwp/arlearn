package org.celstec.arlearn2.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.run.LocationUpdate;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.delegators.LocationDelegator;

import com.google.gdata.util.AuthenticationException;

@Path("/location")
public class Location extends Service {

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}")
	public String location(@HeaderParam("Authorization") String token, String locationsString, @PathParam("runIdentifier") Long runIdentifier,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		LocationUpdate inLoc = (LocationUpdate) deserialise(locationsString, LocationUpdate.class, contentType);
		LocationDelegator ld = new LocationDelegator(token);
		ld.processLocation(runIdentifier, inLoc.getLat(), inLoc.getLng());
		return locationsString;
		
	}
}
