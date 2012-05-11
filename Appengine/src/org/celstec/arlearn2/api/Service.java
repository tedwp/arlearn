package org.celstec.arlearn2.api;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.codehaus.jettison.json.JSONException;

import com.google.gdata.util.AuthenticationException;

public class Service {

	protected UsersDelegator verifyCredentials(String authToken) throws AuthenticationException {
		UsersDelegator qu = new UsersDelegator(authToken);
		if (qu.getCurrentUserAccount()== null) throw new AuthenticationException("");
		return qu;
	}
	
	protected Object getInvalidCredentialsBean() {
		Bean error = new Bean();
		error.setError("credentials are invalid");
		return error;
	}
	
	protected Object getBeanDoesNotParseException(String specificErrorMessage) {
		Bean error = new Bean();
		error.setError("Could not parse bean: "+specificErrorMessage);
		return error;
	}

	protected boolean validCredentials(String authToken) {
		UsersDelegator qu;
		try {
			qu = new UsersDelegator(authToken);
			return (qu.getCurrentUserAccount() != null);
		} catch (AuthenticationException e) {
			return false;
		}
	}
	
	protected String toJson(Object bean) {
		JsonBeanSerialiser jbs = new JsonBeanSerialiser(bean);
		return jbs.serialiseToJson().toString();
	}
	
	protected String serialise(Object bean, String accept) {
		if  ("application/json".equalsIgnoreCase(accept)) return toJson(bean);
		if  (accept != null && accept.contains("application/json")) return toJson(bean);
		if  ("*/*".equalsIgnoreCase(accept)) return toJson(bean);
		return accept + " is not yet supported\n";
	}
	
	protected Object deserialise(String beanString, Class beanClass, String contentType) {
		if  (contentType == null || "application/json".equalsIgnoreCase(contentType) ||  "*/*".equalsIgnoreCase(contentType)) 
			return jsonDeserialise(beanString, beanClass);
		return contentType + " is not yet supported\n";
	}
	
	protected Object jsonDeserialise(String beanString, Class beanClass) {
		JsonBeanDeserializer jbd;
		try {
			jbd = new JsonBeanDeserializer(beanString);
			return (Bean) jbd.deserialize(beanClass);
		} catch (JSONException e) {
			return e.getMessage();
		} catch (Exception e) {
			return e.getMessage();
		}
	}
}