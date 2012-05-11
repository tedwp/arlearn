package org.celstec.arlearn2.client;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;

public class GeneralItemClient extends GenericClient{
	private static GeneralItemClient instance;

	private GeneralItemClient() {
		super("/generalItems");
	}

	public static GeneralItemClient getGeneralItemClient() {
		if (instance == null) {
			instance = new GeneralItemClient();
		}
		return instance;
	}
	
	public GeneralItemList getRunGeneralItems(String token, Long runId) {
		HttpResponse response = conn.executeGET(getUrlPrefix()+"/runId/"+runId, token, "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(response.getEntity());
			return (GeneralItemList) jsonDeserialise(entry, GeneralItemList.class);
		} catch (Exception e) {
			e.printStackTrace();
			GeneralItemList gil = new GeneralItemList();
			gil.setError("exception "+e.getMessage());
			return gil;
		}
	}
}
