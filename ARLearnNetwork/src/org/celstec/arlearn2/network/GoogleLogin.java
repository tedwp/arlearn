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
package org.celstec.arlearn2.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class GoogleLogin {

	public final static String GOOGLE_LOGIN_STRING = "https://www.google.com/accounts/ClientLogin";

	private String user;
	private String password;
	private String service;
	
	public GoogleLogin(String user, String password, String service) throws IOException{
		this.user = user;
		this.password = password;
		this.service = service;
	}

	public GoogleLogin(String user, String password) throws IOException{
		this (user, password, "fusiontables");
	}
	
	public String authenticate() throws Exception{
		try {
			List<NameValuePair> pair = new ArrayList<NameValuePair>();
			pair.add(new BasicNameValuePair("Email", user));
			pair.add(new BasicNameValuePair("Passwd", password));
			pair.add(new BasicNameValuePair("accountType", "HOSTED_OR_GOOGLE"));
			pair.add(new BasicNameValuePair("service", service));
			HttpResponse response = ConnectionFactory.getConnection().executeHttpsPOST(GOOGLE_LOGIN_STRING, null, null, new UrlEncodedFormEntity(pair), null);
			String entry = EntityUtils.toString(response.getEntity());
			System.out.println(entry);
			if (entry.contains("Error=")) return "BadAuthentication";
			return selectAuth(entry);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}
		return "token";
	}
	
	public static String selectAuth(String token) throws IOException {
		token = token.substring(token.indexOf("Auth=") + 5);
		int index = token.indexOf("\n");
		if (index == -1) {
			return token;
		}
		return token.substring(0, index);
	}

}
