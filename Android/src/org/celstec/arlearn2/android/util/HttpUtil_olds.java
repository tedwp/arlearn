package org.celstec.arlearn2.android.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.ParseException;
import org.celstec.arlearn2.android.db.beans.BeanSerialiser;
import org.codehaus.jettison.json.JSONObject;
import org.celstec.arlearn2.android.db.beans.BeanSerialiser;
import org.codehaus.jettison.json.JSONObject;

import android.os.Build;
import android.util.Log;

public class HttpUtil_olds{
	
	public static String baseUrlAppengine = "http://streetlearn.appspot.com/";
	public static String baseUrlLocal = "http://10.0.2.2:9999/";
//	public static String baseUrlLocal = "http://10.0.2.2:8888/";
	public static String baseUrl = baseUrlAppengine;
	public static String serviceUrl = "http://streetlearn.appspot.com/rest/";
	
	static {
//		if (isGoogleSdk()) serviceUrl = "http://10.0.2.2:9999/rest/";
		if (isGoogleSdk()) baseUrl = baseUrlLocal;
		serviceUrl = baseUrl + "rest/";
	}

	public static boolean isGoogleSdk(){
		return "google_sdk".equals(Build.PRODUCT);
	}
	
	public static InputStream getAsStream(String url, String token)
			throws IOException {
		HttpResponse response = executeGET(url, token, null);
		if (response == null)
			return null;
		return response.getEntity().getContent();

	}

	public static String getAsString(String url, String token) throws IOException {
		HttpResponse response = executeGET(url, token, null);
		if (response == null)
			return null;
		return EntityUtils.toString(response.getEntity());
	}

	public static String getAsJSON(String url, String token) throws IOException {
		HttpResponse response = executeGET(url, token, "application/json");
		if (response == null)
			return null;
		return EntityUtils.toString(response.getEntity());
	}	
	
	
	@Deprecated
	public static String postAsJSON_old(String url, String token, Object bean) throws IOException {
		return postAsJSON(url, token, (new BeanSerialiser(bean)).serialise());
	}
	
	public static String postAsJSON(String url, String token, JSONObject postdata) throws IOException {
		return postAsJSON(url, token, postdata.toString());
	}
	
	public static String postAsJSON(String url, String token, String postdata) throws IOException {
		HttpResponse response = executePOST(url, token, "application/json", postdata, "application/json");
		if (response == null)
			return null;
		return EntityUtils.toString(response.getEntity());
	}

	private static HttpResponse executeGET(String url, String token, String accept)
			throws IOException {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpGet request = new HttpGet(url);
			request.setHeader("Authorization", "GoogleLogin auth=" + token);
			if (accept !=null) request.setHeader("Accept", accept);
			return httpClient.execute(request);
		} catch (ParseException e) {
			Log.e("exception in executeGET", e.getMessage());
		}
		return null;
	}

	
	private static HttpResponse executePOST(String url, String token, String accept, String postData, String contentType) throws IOException {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost request = new HttpPost(url);
			request.setHeader("Authorization", "GoogleLogin auth=" + token);
			if (accept !=null) request.setHeader("Accept", accept);
			if (contentType != null) request.setHeader("Content-Type", contentType);
			StringEntity entity = new StringEntity(postData);
			request.setEntity(entity);
			return httpClient.execute(request);
		} catch (ParseException e) {
			Log.e("exception in executePOST", e.getMessage());
		}
		return null;
	}
}
