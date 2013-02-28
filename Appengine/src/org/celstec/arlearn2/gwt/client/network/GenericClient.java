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


import org.celstec.arlearn2.gwt.client.control.Authentication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.util.SC;

public class GenericClient {

	public String getUrl() {
		return "rest/";
	}

	public RequestBuilder getRequestBuilder(String urlPostfix) {
		return getRequestBuilder(urlPostfix, RequestBuilder.GET);
	}

	public RequestBuilder getRequestBuilder(String urlPostfix, RequestBuilder.Method m) {
		String url = urlPostfix == null ? getUrl() : getUrl() + urlPostfix;
		RequestBuilder builder = new RequestBuilder(m, url);
		if (Authentication.getInstance().getAuthenticationToken() == null) return null;
		String authorization = "GoogleLogin auth=" + Authentication.getInstance().getAuthenticationToken();
		builder.setHeader("Authorization", authorization);
		builder.setHeader("Accept", "application/json");
		return builder;
	}

	protected void invokeJsonPOST(String urlPostfix, JSONObject object, final JsonCallback jcb) {
		invokeJsonPOST(urlPostfix, object == null ? "" : object.toString(), jcb);
	}

	protected void invokeJsonPUT(String urlPostfix, JSONObject object, final JsonCallback jcb) {
		invokeJsonPUT(urlPostfix, object == null ? "" : object.toString(), jcb);
	}

	protected void invokeJsonPOST(String urlPostfix, String object, final JsonCallback jcb) {
		RequestBuilder builder = getRequestBuilder(urlPostfix, RequestBuilder.POST);
		builder.setHeader("Content-Type", "application/json");
		try {
			Request request = builder.sendRequest(object, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						try {
							JSONValue jsonValue = JSONParser.parseLenient(response.getText());
							if (jcb != null)
								jcb.onJsonReceived(jsonValue);
						} catch (JSONException e) {
							if (jcb != null)
								jcb.onError();

						}
					}

				}

				@Override
				public void onError(Request request, Throwable exception) {

				}
			});
		} catch (RequestException e) {
			jcb.onError();
		}
	}

	protected void invokeJsonPUT(String urlPostfix, String object, final JsonCallback jcb) {
		RequestBuilder builder = getRequestBuilder(urlPostfix, RequestBuilder.PUT);
		builder.setHeader("Content-Type", "application/json");
		try {
			Request request = builder.sendRequest(object, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						try {
							JSONValue jsonValue = JSONParser.parseLenient(response.getText());
							if (jcb != null)
								jcb.onJsonReceived(jsonValue);
						} catch (JSONException e) {
							if (jcb != null)
								jcb.onError();

						}
					}

				}

				@Override
				public void onError(Request request, Throwable exception) {

				}
			});
		} catch (RequestException e) {
			jcb.onError();
		}
	}

	protected void invokeJsonDELETE(String urlPostfix, final JsonCallback jcb) {
		RequestBuilder builder = getRequestBuilder(urlPostfix, RequestBuilder.DELETE);
		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						if (jcb != null) {
							try {
								if (response.getText().equals("")) {
									jcb.onJsonReceived(new JSONObject());
									return;
								}
								JSONValue jsonValue = JSONParser.parseLenient(response.getText());
								if (jsonValue.isObject() != null) {
									jcb.onJsonReceived(jsonValue);
								}

							} catch (JSONException e) {
								jcb.onError();
							}
						}
					}

				}

				@Override
				public void onError(Request request, Throwable exception) {

				}
			});
		} catch (RequestException e) {
			jcb.onError();
		}
	}

	protected void invokeJsonGET(String urlPostfix, final JsonCallback jcb) {
		RequestBuilder builder = getRequestBuilder(urlPostfix);
		if (builder == null) return;
		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						try {
							JSONValue jsonValue = JSONParser.parseLenient(response.getText());
							if (jsonValue.isObject() != null) {
								jcb.onJsonReceived(jsonValue);
							}

						} catch (JSONException e) {
							jcb.onError();
						}
					}

				}

				@Override
				public void onError(Request request, Throwable exception) {
					exception.printStackTrace();
				}
			});
		} catch (RequestException e) {
			jcb.onError();
		}
	}

	public void getItemsForRun(long runId, JsonCallback jsonCallback) {
		invokeJsonGET(getRunUrlPostfix(runId), jsonCallback);
	}

	public void getItemsForGame(long gameId, JsonCallback jsonCallback) {
		invokeJsonGET(getGameUrlPostfix(gameId), jsonCallback);
	}

	public void getItems(JsonCallback jsonCallback) {
		invokeJsonGET(null, jsonCallback);
	}

	public void deleteItemsForGame(long gameId, JsonCallback jsonCallback) {
		invokeJsonDELETE(getGameUrlPostfix(gameId), jsonCallback);
	}

	public void deleteItemsForRun(long runId, JsonCallback jsonCallback) {
		invokeJsonDELETE(getRunUrlPostfix(runId), jsonCallback);
	}

	protected String getRunUrlPostfix(long runId) {
		return "/runId/" + runId;
	}

	protected String getGameUrlPostfix(long gameId) {
		return "/gameId/" + gameId;
	}
}
