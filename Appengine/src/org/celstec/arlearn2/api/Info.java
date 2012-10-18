package org.celstec.arlearn2.api;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gdata.util.AuthenticationException;

@Path("/info")
public class Info extends Service {
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getInfo( @DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		org.celstec.arlearn2.beans.Info info = new org.celstec.arlearn2.beans.Info();
		info.setVersion("arlearn-testing-1.2.11");
		return serialise(info, accept);
	}
}
