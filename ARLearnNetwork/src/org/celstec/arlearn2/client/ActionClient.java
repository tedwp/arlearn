package org.celstec.arlearn2.client;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.network.ConnectionFactory;

public class ActionClient extends GenericClient{
	
	private static ActionClient instance;

	private ActionClient() {
		super("/actions");
	}

	public static ActionClient getActionClient() {
		if (instance == null) {
			instance = new ActionClient();
		}
		return instance;
	}
	
	public Action publishAction(String token, Action action) {
		HttpResponse response = ConnectionFactory.getConnection().executePOST(getUrlPrefix(), token, "application/json", toJson(action), "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(response.getEntity());
			return (Action) jsonDeserialise(entry, Action.class);
		} catch (Exception e) {
			e.printStackTrace();
			Action act = new Action();
			act.setError("exception "+e.getMessage());
			return act;
		}
	}
}
