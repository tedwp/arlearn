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
package org.celstec.arlearn2.gwt.client.network;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.util.SC;

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
		builder.setHeader("accept", "application/json");
		builder.setHeader("Content-Type", "text/plain");
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
									SC.say("jsonException: "+e);
								}
							} else {
								
								lc.onError();
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
