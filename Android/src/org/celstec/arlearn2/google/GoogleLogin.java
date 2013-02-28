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
package org.celstec.arlearn2.google;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
//import org.celstec.arlearn2.android.util.HttpUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.util.Log;

public class GoogleLogin {

//	public final static String GOOGLE_LOGIN_STRING = "https://www.google.com/accounts/ClientLogin";
//
//	public static String authenticate(String user, String pasword) throws IOException{
//		return authenticate(user, pasword, "local");
//	}
//
//	public static String authenticate(String user, String password,
//			String service) throws IOException{
//		HttpClient httpClient = new DefaultHttpClient();	
//		try {
//			HttpPost request = new HttpPost(GOOGLE_LOGIN_STRING);
//			List<NameValuePair> pair = new ArrayList<NameValuePair>();
//			pair.add(new BasicNameValuePair("Email", user));
//			pair.add(new BasicNameValuePair("Passwd", password));
//			pair.add(new BasicNameValuePair("accountType", "HOSTED_OR_GOOGLE"));
//			pair.add(new BasicNameValuePair("service", service));
//			request.setEntity(new UrlEncodedFormEntity(pair));
//			HttpResponse response = httpClient.execute(request);
//			String entry = EntityUtils.toString(response.getEntity());
//			if (entry.contains("Error=")) return "BadAuthentication";
//			return selectAuth(entry);
//		} catch (UnsupportedEncodingException e) {
//			Log.e("UnsupportedEncodingException", e.getMessage(), e);
//		} catch (ClientProtocolException e) {
//			Log.e("ClientProtocolException", e.getMessage(), e);
//		}
//		return "token";
//	}
//	
//	public static String authenticateAppEngineProject(String user, String password) throws IOException{
//		HttpClient httpClient = new DefaultHttpClient();	
//		try {
//			HttpPost request = new HttpPost(HttpUtils.serviceUrl+"login");
////			List<NameValuePair> pair = new ArrayList<NameValuePair>();
////			pair.add(new BasicNameValuePair("Email", user));
////			pair.add(new BasicNameValuePair("Passwd", password));
////			pair.add(new BasicNameValuePair("accountType", "HOSTED_OR_GOOGLE"));
//			request.setHeader("Accept", "application/json");
//			StringEntity se = new StringEntity(user+"\n"+password);
//			request.setEntity(se);
//			HttpResponse response = httpClient.execute(request);
//			if (response.getEntity() == null) return null;
//			String entry = EntityUtils.toString(response.getEntity());
//			if (entry.contains("Error=")) return "BadAuthentication";
//			JSONObject json = new JSONObject(entry);
//			return json.getString("auth");
//		} catch (UnsupportedEncodingException e) {
//			Log.e("UnsupportedEncodingException", e.getMessage(), e);
//		} catch (ClientProtocolException e) {
//			Log.e("ClientProtocolException", e.getMessage(), e);
//		} catch (JSONException e) {
//			Log.e("json parse exception", e.getMessage(), e);
//		}catch (Exception e) {
//			Log.e(" exception", e.getMessage(), e);
//		}
//		return "token";
//	}
//
//	public static String selectAuth(String token) throws IOException {
//		token = token.substring(token.indexOf("Auth=") + 5);
//		int index = token.indexOf("\n");
//		if (index == -1) {
//			return token;
//		}
//		return token.substring(0, index);
//	}

}
