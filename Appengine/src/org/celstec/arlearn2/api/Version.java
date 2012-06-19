package org.celstec.arlearn2.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.jdo.manager.VersionManager;

import com.google.gdata.util.AuthenticationException;

@Path("/version")
public class Version extends Service {

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String newVersion(String versionString, @DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		Object vObject = deserialise(versionString, org.celstec.arlearn2.beans.Version.class, accept);
		if (vObject instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) vObject), accept);
		org.celstec.arlearn2.beans.Version v = (org.celstec.arlearn2.beans.Version) vObject;
		return serialise(VersionManager.addVersion(v), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{versionCode}")
	public String get(@HeaderParam("Authorization") String token, @PathParam("versionCode") Integer versionCode, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		return serialise(VersionManager.getVersion(versionCode), accept);
	}
}
