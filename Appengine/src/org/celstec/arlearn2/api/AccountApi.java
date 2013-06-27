package org.celstec.arlearn2.api;

import java.util.UUID;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.delegators.AccountDelegator;
import org.celstec.arlearn2.delegators.CollaborationDelegator;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.jdo.manager.AccountManager;
import org.celstec.arlearn2.jdo.manager.ContactManager;

import com.google.gdata.util.AuthenticationException;

@Path("/account")
public class AccountApi extends Service {
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/createAnonymousContact")
	public String createAnonymContact(@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			String contact
			) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		Object inContact = deserialise(contact, Account.class, contentType);
		if (inContact instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inContact), accept);
		AccountDelegator ad = new AccountDelegator(this);
		return serialise(ad.createAnonymousContact((Account) inContact), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/anonymousLogin/{anonToken}")
	public String anonymousLogin( 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@PathParam("anonToken") String anonToken
			) throws AuthenticationException {
		Account account = AccountManager.getAccount("0:"+anonToken);
		AuthResponse ar = new AuthResponse();

		if (account != null) {
			ar.setAuth(UUID.randomUUID().toString());
			UserLoggedInManager.submitOauthUser("0:"+anonToken, ar.getAuth());		
			return serialise(ar, accept);
		}
		
		ar.setError("Authentication failed");
		return serialise(ar, accept);

	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/accountDetails")
	public String getContactDetails(@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@PathParam("addContactToken") String addContactToken
			) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		AccountDelegator ad = new AccountDelegator(this);
//		String myAccount = UserLoggedInManager.getUser(token);
//		if (myAccount == null) {
//			Account ac = new Account();
//			ac.setError("account is not logged in");
//		}
		return serialise(ad.getContactDetails(this.account.getFullId()), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/accountDetails/{accountFullId}")
	public String getContactDetailsForId(@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@PathParam("accountFullId") String accountFullId

			) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		AccountDelegator ad = new AccountDelegator(this);
		String myAccount = UserLoggedInManager.getUser(token);
		if (myAccount == null) {
			Account ac = new Account();
			ac.setError("account is not logged in");
		}
		return serialise(ad.getContactDetails(accountFullId), accept);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@CacheControlHeader("no-cache")
	@Path("/makesuper/{accountFullId}")
	public String makesuper(@HeaderParam("Authorization") String token, 
			@DefaultValue("application/json") @HeaderParam("Accept") String accept,
			@PathParam("accountFullId") String accountFullId

			) throws AuthenticationException {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		AccountDelegator ad = new AccountDelegator(this);
		String myAccount = UserLoggedInManager.getUser(token);
		if (myAccount == null) {
			Account ac = new Account();
			ac.setError("account is not logged in");
		}
		ad.makeSuper(accountFullId);
		return "{}";
	}
	
}
