/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

//import org.celstec.arlearn2.beans.BeanDeserialiser;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.run.Inventory;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
//import org.celstec.arlearn2.delegators.generalitems.CreateGeneralItems;
//import org.celstec.arlearn2.delegators.generalitems.QueryGeneralItems;
import org.celstec.arlearn2.delegators.inventory.UpdateInventoryRecord;
import org.celstec.arlearn2.jdo.manager.GeneralItemVisibilityManager;
import org.codehaus.jettison.json.JSONObject;

import com.google.gdata.util.AuthenticationException;

@Path("/generalItems")
public class GeneralItems extends Service {

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/gameId/{gameIdentifier}")
	public String getArtifacts(
			@HeaderParam("Authorization") String token, 
			@PathParam("gameIdentifier") Long gameIdentifier, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@QueryParam("from") Long from,
			@QueryParam("until") Long until)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GeneralItemDelegator gid = new GeneralItemDelegator(token);
//		QueryGeneralItems gi = new QueryGeneralItems(token);
		if (from == null && until == null) {
			return serialise(gid.getGeneralItems(gameIdentifier), accept);
		} else {
			return serialise(gid.getGeneralItems(gameIdentifier, from, until), accept);
		}
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/gameId/{gameIdentifier}/generalItem/{itemId}")
	public String getArtifact(@HeaderParam("Authorization") String token, 
			@PathParam("gameIdentifier") Long gameIdentifier, 
			@PathParam("itemId") Long itemId, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
//		QueryGeneralItems gi = new QueryGeneralItems(token);
		GeneralItemDelegator gid = new GeneralItemDelegator(token);

		for (GeneralItem item : gid.getGeneralItems(gameIdentifier).getGeneralItems()) {
			if (item.getId().equals(itemId)) {
				return serialise(item, accept);
			}
			
		}
		GeneralItem giError = new GeneralItem();
		giError.setError("id "+itemId+" does not exist");
		return serialise(giError, accept);
	}
	
	@DELETE
	@Path("/gameId/{gameIdentifier}/generalItem/{itemId}")
	public String deleteItem(@HeaderParam("Authorization") String token, 
			@PathParam("gameIdentifier") Long gameIdentifier, 
			@PathParam("itemId") String itemId, 
			@HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GeneralItemDelegator gid = new GeneralItemDelegator(token);
		gid.deleteGeneralItem(gameIdentifier, itemId);
		return "";
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}")
	public String getRunGeneralItems(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GeneralItemDelegator gid = new GeneralItemDelegator(token);
		UsersDelegator ud = new UsersDelegator(gid);
		String email = ud.getCurrentUserAccount();
		return serialise(gid.getItems(runIdentifier, email, GeneralItemVisibilityManager.VISIBLE_STATUS), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}/disappeared")
	public String getRunGeneralItemsDisappeared(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GeneralItemDelegator gid = new GeneralItemDelegator(token);
		UsersDelegator ud = new UsersDelegator(gid);
		String email = ud.getCurrentUserAccount();
		return serialise(gid.getItems(runIdentifier, email, GeneralItemVisibilityManager.DISAPPEARED_STATUS), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}/generalItem/{itemId}")
	public String getRunGeneralItem(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier,@PathParam("itemId") Long itemId,  @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GeneralItemDelegator gid = new GeneralItemDelegator(token);
		UsersDelegator ud = new UsersDelegator(gid);
		String email = ud.getCurrentUserAccount();
		
		for (GeneralItem item : gid.getNonPickableItems(runIdentifier, email).getGeneralItems()) {
			if (item.getId().equals(itemId)) return serialise(item, accept);
			
		}
		GeneralItem giError = new GeneralItem();
		giError.setError("id "+itemId+" does not exist");
		return serialise(giError, accept);
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}/all")
	public String getRunGeneralItemsAll(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		GeneralItemDelegator gid = new GeneralItemDelegator(token);
		UsersDelegator ud = new UsersDelegator(gid);
		return serialise(gid.getNonPickableItemsAll(runIdentifier), accept);
	}

	// TODO : switch the following code to new beans infrastructure/
	// serialisers/deserialisers
//	@GET
//	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	@Path("/inventory/{runIdentifier}/team/{teamId}")
//	public String getInventory(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("teamId") String teamId,
//			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
//		// TODO teamId must be deleted... should be extracted from table
//		// structure...
//		GeneralItemDelegator gid = new GeneralItemDelegator(token);
//		UsersDelegator qu = new UsersDelegator(gid);
//		Inventory inventory = new Inventory();
//		inventory.setRunId(runIdentifier);
//		String user = qu.getCurrentUserAccount();
//		teamId = qu.getUserByEmail(runIdentifier, user).getTeamId();
//		// TODO switch on inventory
//		// TODO optimize... each invocation results in a get general items
//		// that issues a database request.
//		inventory.setMapInventoryItems(qa.getMapInventoryItems(runIdentifier, user, teamId));
//		inventory.setUserInventoryItems(qa.getUserInventoryItems(runIdentifier, user, teamId));
//		inventory.setTeamInventoryItems(qa.getTeamInventoryItems(runIdentifier, user, teamId));
//
//		return serialise(inventory, accept);
//	}

//	@GET
//	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	@Path("/pickableItems/{runIdentifier}/team/{teamId}")
//	public String getPickableMapItems(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("teamId") String teamId,
//			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
//		GeneralItemDelegator gid = new GeneralItemDelegator(token);
//		UsersDelegator qu = new UsersDelegator(gid);
//		String email = qu.getCurrentUserAccount();
//		return serialise(gid.getMapInventoryItems(runIdentifier, email, teamId), accept);
//
//	}

//	@GET
//	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	@Path("/userInventory/{runIdentifier}/team/{teamId}")
//	public String getUserInventoryItems(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("teamId") String teamId,
//			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
//		GeneralItemDelegator gid = new GeneralItemDelegator(token);
//		UsersDelegator qu = new UsersDelegator(gid);
//		String email = qu.getCurrentUserAccount();
//
//		return serialise(gid.getUserInventoryItems(runIdentifier, email, teamId), accept);
//	}

//	@GET
//	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	@Path("/teamInventory/{runIdentifier}/team/{teamId}")
//	public String getTeamInventoryItems(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("teamId") String teamId,
//			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
//		QueryGeneralItems qa = new QueryGeneralItems(token);
//		UsersDelegator qu = new UsersDelegator(qa);
//		String email = qu.getCurrentUserAccount();
//
//		return serialise(qa.getTeamInventoryItems(runIdentifier, email, teamId), accept);
//	}
//
//	@GET
//	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	@Path("/pickupItem/{runIdentifier}/team/{teamId}/item/{generalItemId}/")
//	public String pickupItem(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("teamId") String teamId,
//			@PathParam("generalItemId") Long generalItemId, @DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
//		UpdateInventoryRecord uir = new UpdateInventoryRecord(token);
//		UsersDelegator qu = new UsersDelegator(uir);
//		String email = qu.getCurrentUserAccount();
//		uir.pickupItem(runIdentifier, email, teamId, generalItemId);
//		return getInventory(token, runIdentifier, teamId, accept);
//	}

//	@GET
//	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	@Path("/dropItem/{runIdentifier}/team/{teamId}/item/{generalItemId}/lat/{lat}/lng/{lng}")
//	public String dropItem(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("teamId") String teamId,
//			@PathParam("generalItemId") Long generalItemId, @PathParam("lat") Double lat, @PathParam("lng") Double lng, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
//			throws AuthenticationException {
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
//		UpdateInventoryRecord uir = new UpdateInventoryRecord(token);
//		UsersDelegator qu = new UsersDelegator(uir);
//		String email = qu.getCurrentUserAccount();
//		uir.dropItem(runIdentifier, email, teamId, generalItemId, lat, lng);
//		return getInventory(token, runIdentifier, teamId, accept);
//	}
//
//	@GET
//	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	@Path("/dropAtDropzone/{runIdentifier}/team/{teamId}/item/{generalItemId}/dropZone/{dropZoneId}")
//	public String dropAtDropzone(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @PathParam("teamId") String teamId,
//			@PathParam("generalItemId") Long generalItemId, @PathParam("dropZoneId") Long dropZoneId, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
//			throws AuthenticationException {
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
//		UpdateInventoryRecord uir = new UpdateInventoryRecord(token);
//		UsersDelegator qu = new UsersDelegator(uir);
//		String email = qu.getCurrentUserAccount();
//		uir.dropAtDropZone(runIdentifier, email, teamId, generalItemId, dropZoneId);
//		return getInventory(token, runIdentifier, teamId, accept);
//	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String gi, @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);

		Object inItem = deserialise(gi, GeneralItem.class, contentType);
		if (inItem instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inItem), accept);
		try {
			inItem = deserialise(gi, Class.forName(((GeneralItem) inItem).getType()), contentType);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		GeneralItemDelegator gid = new GeneralItemDelegator(token);

		return serialise(gid.createGeneralItem((GeneralItem) inItem), accept);

	}
	
	

}
