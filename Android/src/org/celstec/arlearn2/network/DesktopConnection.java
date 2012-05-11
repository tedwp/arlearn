package org.celstec.arlearn2.network;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class DesktopConnection implements HttpConnection {

	private HttpClient getHttpsClient() {
		HttpClient httpClient = new DefaultHttpClient();
		SSLContext ctx;
		try {
			ctx = SSLContext.getInstance("TLS");

			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = null;// new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = httpClient.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			return new DefaultHttpClient(ccm, httpClient.getParams());
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	
	public HttpResponse executeHttpsPOST(String url, String token, String accept, HttpEntity postData, String contentType) {
		HttpClient httpClient = getHttpsClient();
		try {
			HttpPost request = new HttpPost(url);
			if (token != null) 
				request.setHeader("Authorization", "GoogleLogin auth=" + token);
			if (accept != null)
				request.setHeader("Accept", accept);
			if (contentType != null)
				request.setHeader("Content-Type", contentType);
			request.setEntity(postData);
			return httpClient.execute(request);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public HttpResponse executeGET(String url, String token, String accept) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpGet request = new HttpGet(url);
			request.setHeader("Authorization", "GoogleLogin auth=" + token);
			if (accept != null)
				request.setHeader("Accept", accept);
			return httpClient.execute(request);
		} catch (ParseException e) {
			System.err.print("exception in executeGET");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public HttpResponse executePOST(String url, String token, String accept,
			String postData, String contentType) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost request = new HttpPost(url);
			request.setHeader("Authorization", "GoogleLogin auth=" + token);
			if (accept != null)
				request.setHeader("Accept", accept);
			if (contentType != null)
				request.setHeader("Content-Type", contentType);
			StringEntity entity = new StringEntity(postData);
			request.setEntity(entity);
			return httpClient.execute(request);
		} catch (ParseException e) {
			System.err.print("exception in executePOST");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public HttpResponse executeDELETE(String url, String token, String accept) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpDelete request = new HttpDelete(url);
			request.setHeader("Authorization", "GoogleLogin auth=" + token);
			if (accept != null)
				request.setHeader("Accept", accept);
//			if (contentType != null)
//				request.setHeader("Content-Type", contentType);
//			StringEntity entity = new StringEntity(postData);
//			request.setEntity(entity);
			return httpClient.execute(request);
		} catch (ParseException e) {
			System.err.print("exception in executePOST");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}