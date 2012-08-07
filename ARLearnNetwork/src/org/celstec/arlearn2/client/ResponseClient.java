package org.celstec.arlearn2.client;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.network.ConnectionFactory;

public class ResponseClient extends GenericClient{
	

	private static ResponseClient instance;

	private ResponseClient() {
		super("/response");
	}

	public static ResponseClient getResponseClient() {
		if (instance == null) {
			instance = new ResponseClient();
		}
		return instance;
	}
	
	public Response publishAction(String token, Response response) {
		HttpResponse httpResp = ConnectionFactory.getConnection().executePOST(getUrlPrefix(), token, "application/json", toJson(response), "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(httpResp.getEntity());
			System.out.println(entry);
			return (Response) jsonDeserialise(entry, Response.class);
		} catch (Exception e) {
			e.printStackTrace();
			Response respError = new Response();
			respError.setError("exception "+e.getMessage());
			return respError;
		}
	}
}
