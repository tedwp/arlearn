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
