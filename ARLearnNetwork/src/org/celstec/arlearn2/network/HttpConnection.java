package org.celstec.arlearn2.network;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

public interface HttpConnection {

	public HttpResponse executeHttpsPOST(String url, String token, String accept, HttpEntity postData, String contentType);

	public HttpResponse executeGET(String url, String token, String accept);
	
	public HttpResponse executePOST(String url, String token, String accept, String postData, String contentType);

	public HttpResponse executeDELETE(String url, String token, String accept);

}
