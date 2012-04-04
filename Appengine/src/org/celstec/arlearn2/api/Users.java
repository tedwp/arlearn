package org.celstec.arlearn2.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.run.Location;
import org.celstec.arlearn2.beans.run.LocationList;
import org.celstec.arlearn2.beans.run.UserList;
import org.celstec.arlearn2.beans.run.User;

import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.delegators.location.QueryLocations;
import org.celstec.arlearn2.delegators.location.SubmitLocations;

import com.google.gdata.util.AuthenticationException;

@Path("/users")
public class Users extends Service {

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String userString, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		Object inUser = deserialise(userString, User.class, contentType);
		if (inUser instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inUser), accept);
		User user = (User) inUser;
		 
		UsersDelegator qu = verifyCredentials(token);
		if (user.getEmail() == null)
			user.setEmail(qu.getCurrentUserAccount());
		return serialise(qu.createUser(user), accept);
	}

	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runId}/email/{email}")
	public String deleteTeam(@HeaderParam("Authorization") String token, @PathParam("runId") Long runId, @PathParam("email") String email,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		UsersDelegator cu = new UsersDelegator(verifyCredentials(token));
		cu.deleteUser(runId, email);
		return null;
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}")
	public String getUsers(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		UsersDelegator qu = new UsersDelegator(token);
		UserList ul = qu.getUsers(runIdentifier, null);
		List<User> ulist = ul.getUsers();
		for (int i = 0; i < ulist.size(); i++) {
			User u = ulist.get(i);
			if (u != null) {
				QueryLocations ql = new QueryLocations(qu);
				Location loc = ql.getLastLocation(u.getEmail(), runIdentifier);
				// return ql.getLastLocation(u.getEmail(), runIdentifier);
				// Location loc = this.getLocation(token, u.getEmail(),
				// runIdentifier, accept);
				if (loc != null) {
					u.setLat(loc.getLat());
					u.setLng(loc.getLng());
				}
			}
		}
		return serialise(ul, accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}/teamId/{teamId}")
	public String getUsersForTeam(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("teamId") String teamId,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		UsersDelegator qu = new UsersDelegator(token);
		return serialise(qu.getUsers(runIdentifier, teamId), accept);
	}

	// TODO refactor what is below this line
	// ========= (line)

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/updateUser/{runIdentifier}/email/{email}")
	public String update(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("email") String email, String userString,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inUser = deserialise(userString, User.class, contentType);
		if (inUser instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inUser), accept);
		 User user = (User) inUser;
		
		// TODO store coordinates
		SubmitLocations sl = new SubmitLocations(token);
		Location l = new Location();
		l.setLng(user.getLng());
		l.setLat(user.getLat());
		l.setTimestamp(System.currentTimeMillis());
		sl.submitLocation(0l, l, runIdentifier);

		return serialise(user, accept);
		// return uu.updateUser(runIdentifier, email, user);

	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/locations/runId/{runIdentifier}")
	public String location(@HeaderParam("Authorization") String token, String locationsString, @PathParam("runIdentifier") Long runIdentifier,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inLocations = deserialise(locationsString, LocationList.class, contentType);
		if (inLocations instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inLocations), accept);
		LocationList locations = (LocationList) inLocations;
		
		if (locations.getLocations().size() >= 500) {
			LocationList error = new LocationList();
			error.setError("The maximum number of locations you can combine in a single request is 500.");

			return serialise(error, accept);

		}
		long delta = System.currentTimeMillis() - locations.getTimestamp();
		SubmitLocations sl = new SubmitLocations(token);
		sl.submitLocations(delta, locations, runIdentifier);
		return serialise(locations, accept);

	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/location/runId/{runIdentifier}/email/{userId}")
	public String getLocation(@HeaderParam("Authorization") String token, @PathParam("userId") String userId, @PathParam("runIdentifier") Long runIdentifier,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		QueryLocations ql = new QueryLocations(token);

		return serialise(ql.getLastLocation(userId, runIdentifier), accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/loggedInUser/{runIdentifier}/email/{email}")
	public String getLoggedInUser(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("email") String email,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		UsersDelegator qu = new UsersDelegator(token);
		return serialise(qu.getUserByEmail(runIdentifier, email), accept);
	}

}
