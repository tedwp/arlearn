package org.celstec.arlearn2.api;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;

import org.celstec.arlearn2.util.FusionCache;

@Path("/cache")
public class Cache {
	
	@GET
	@Path("/clearAll")
	public void flush(@HeaderParam("Authorization") String token) {
		FusionCache.getInstance().clearAll();
	}

}
