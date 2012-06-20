package org.celstec.arlearn2.api;

import java.util.HashMap;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ActionList;
import org.celstec.arlearn2.beans.run.User;

import org.celstec.arlearn2.delegators.ActionDelegator;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;
import org.celstec.arlearn2.delegators.notification.Notification;
import org.codehaus.jettison.json.JSONException;

import com.google.gdata.util.AuthenticationException;

@Path("/actions")
public class Actions extends Service {
	private static final Logger logger = Logger.getLogger(Actions.class.getName());

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/runId/{runIdentifier}")
	public String getActions(@HeaderParam("Authorization") String token, @PathParam("runIdentifier") Long runIdentifier, @DefaultValue("application/json") @HeaderParam("Accept") String accept)
			throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		ActionDelegator ad = new ActionDelegator(token);
		ActionList al = ad.getActionList(runIdentifier);
//		Action a = new Action();
//		a.setGeneralItemId(801l);
//		a.setAction("read");
//		al.addAction(a);
		return serialise(al, accept);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String action, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		Object inAction = deserialise(action, Action.class, contentType);
		if (inAction instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inAction), accept);
		Action act = (new ActionDelegator(token)).createAction((Action) inAction);
		return serialise(act, accept);
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/notify/{account}/{runId}/{gId}")
	public void notify(
			@HeaderParam("Authorization") String token, 
			String action, 
			@PathParam("account") String account,
			@PathParam("runId") Long runId,
			@PathParam("gId") Long gId,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
//		Notification not = new Notification("all", runId, token);
//		HashMap<String, String> hm = new HashMap<String, String>();
//		hm.put("action", "visible");
//		hm.put("itemId",""+gId);
//		hm.put("name", "extra opdracht");
//		hm.put("runId", ""+runId);
//		not.notify(account, "GeneralItem", hm);
		
		GeneralItemModification gim = new GeneralItemModification();
		gim.setModificationType(GeneralItemModification.VISIBLE);
		gim.setRunId(runId);
		gim.setGeneralItem(new GeneralItem());
		gim.getGeneralItem().setId(gId);
		ChannelNotificator.getInstance().notify(account, gim);
		
	}
	
	

}
