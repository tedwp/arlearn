package org.celstec.arlearn2.util;

import java.util.HashMap;

import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.ActionDelegator;
import org.celstec.arlearn2.delegators.generalitems.QueryGeneralItems;
import org.celstec.arlearn2.delegators.notification.Notification;
import org.celstec.arlearn2.jdo.manager.ActionManager;
import org.celstec.arlearn2.tasks.beans.NotifyItemVisible;
import org.celstec.arlearn2.tasks.beans.UpdateGeneralItems;

import com.google.gdata.util.AuthenticationException;

public class ExtraVraagManager {

	
	public static void fireAction(Long runId, String account, String actionString, String teamId, String authToken, Long gId){
		ActionDelegator ad;
		try {
			ad = new ActionDelegator("auth="+authToken);
			Action action = new Action();
			action.setAction(actionString);
			action.setRunId(runId);
			action.setUserEmail(account);
			action.setTimestamp(System.currentTimeMillis());
//			ActionManager.addAction(action.getRunId(), action.getAction(), action.getUserEmail(), action.getGeneralItemId(), action.getGeneralItemType(), action.getTime());
////			(new UpdateGeneralItems("auth=token", action.getRunId(), action.getAction(), action.getUserEmail())).scheduleTask();
//			QueryGeneralItems qgi = new QueryGeneralItems(ad);
//			User u = new User();
//			u.setEmail(account);
//			u.setRunId(runId);
//			u.setTeamId(teamId);
//			qgi.checkActionEffect(action, runId, u);
			Action act = ad.createAction(action);
			GeneralItem generalItem = new GeneralItem();
			generalItem.setId(gId);
			Notification not = new Notification("all", runId, authToken);
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("action", "visible");
			hm.put("itemId",""+gId);
			hm.put("name", "extra opdracht");
			hm.put("runId", ""+runId);
			not.notify(account, "GeneralItem", hm);
//			(new NotifyItemVisible(authToken, runId, generalItem)).scheduleTask();

		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		ActionManager.addAction(runId, action, account, null,null, System.currentTimeMillis());
	}
}
