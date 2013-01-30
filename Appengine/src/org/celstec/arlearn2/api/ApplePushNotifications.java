package org.celstec.arlearn2.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.notification.APNDeviceDescription;
import org.celstec.arlearn2.delegators.ApplePushNotificationDelegator;

import com.google.gdata.util.AuthenticationException;

@Path("/apn")
public class ApplePushNotifications extends Service {

	@POST
	@Consumes({MediaType.APPLICATION_JSON })
	public String put(@HeaderParam("Authorization") String token, String deviceDescString, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		Object inApnDesc = deserialise(deviceDescString, APNDeviceDescription.class, contentType);
		if (inApnDesc instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inApnDesc), accept);
		APNDeviceDescription description = (APNDeviceDescription) inApnDesc;
		(new ApplePushNotificationDelegator(token)).registerDescription(description);
		return serialise(description, accept);
	}
	
	@POST
	@Path("/sendNotification/user/{account}")
	public String sendNotification(@HeaderParam("Authorization") String token, String text, 
			@PathParam("account") String account,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		(new ApplePushNotificationDelegator(token)).sendNotification(account, text);

		return "sent";
	}
	
	@POST
	@Path("/json/user/{account}")
	public String notificationAsJson(@HeaderParam("Authorization") String token, String text, 
			@PathParam("account") String account,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		(new ApplePushNotificationDelegator(token)).sendNotificationAsJson(account, text);

		return "sent";
	}
	
}
