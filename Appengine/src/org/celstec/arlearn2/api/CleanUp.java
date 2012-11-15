package org.celstec.arlearn2.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.tasks.beans.cleanUp.RunIterator;
import org.celstec.arlearn2.tasks.beans.cleanUp.UserIterator;

@Path("/cleanUp")
public class CleanUp {

	@GET
	@Path("/iterateRuns")
	public String iterateRuns(){
		(new RunIterator("*")).scheduleTask();
		return "";
	}
	
	@GET
	@Path("/iterateUsers")
	public String iterateUsers(){
		(new UserIterator("*")).scheduleTask();
		return "";
	}

}
