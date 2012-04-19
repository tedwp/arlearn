package org.celstec.arlearn2.gwt.client.network;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class LoginClient {

	private String username;
	private String password;

	public LoginClient(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public void authenticate(final LoginCallback lc) {
		String url = "rest/login";
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		try {
			Request request = builder.sendRequest(username+"\n"+password,
					new RequestCallback() {

						@Override
						public void onResponseReceived(Request request,
								Response response) {
							if (200 == response.getStatusCode()) {
								try {
									JSONValue jsonValue = JSONParser
											.parseLenient(response.getText());
									if (jsonValue.isObject() != null) {
										lc.onAuthenticationTokenReceived(jsonValue
												.isObject().get("auth")
												.toString());
									}

								} catch (JSONException e) {
								}
							}

						}

						@Override
						public void onError(Request request, Throwable exception) {
							// TODO Auto-generated method stub

						}
					});
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
