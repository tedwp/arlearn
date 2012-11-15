package org.celstec.arlearn2.client;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.run.ActionList;
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
	
	//TODO credentials are invalid exception
	public Action publishAction(String token, Action action) {
		HttpResponse response = ConnectionFactory.getConnection().executePOST(getUrlPrefix(), token, "application/json", toJson(action), "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(response.getEntity(), "utf-8");
			return (Action) jsonDeserialise(entry, Action.class);
		} catch (Exception e) {
			e.printStackTrace();
			Action act = new Action();
			act.setError("exception "+e.getMessage());
			return act;
		}
	}
	
	public ActionList getRunActions(String token, Long runId) {
		return (ActionList)  executeGet(getUrlPrefix()+"/runId/"+runId, token, ActionList.class);
	}
}
