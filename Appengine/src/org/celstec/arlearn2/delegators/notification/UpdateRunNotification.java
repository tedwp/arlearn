package org.celstec.arlearn2.delegators.notification;

import java.util.HashMap;
import java.util.logging.Logger;

import org.celstec.arlearn2.tasks.beans.NotifyUpdateRun;

public class UpdateRunNotification extends Notification {

	public UpdateRunNotification(int scope, long runId, String authToken) {
		super(scope, runId, authToken);
	}
    private static final Logger log = Logger.getLogger(UpdateRunNotification.class.getName());

	public void runCreated(NotifyUpdateRun notifyUpdateRun) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("action", "createRun");
		if (notifyUpdateRun.getRunId() !=null) hm.put("runId", ""+notifyUpdateRun.getRunId());
		if (notifyUpdateRun.getCreated() !=null) hm.put("created", ""+notifyUpdateRun.getCreated());
		setUser(notifyUpdateRun.getUser());
		notify("Run", hm);
	}
	
	public void runDeleted( NotifyUpdateRun notifyUpdateRun) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("action", "deleteRun");
		if (notifyUpdateRun.getRunId() !=null) hm.put("runId", ""+notifyUpdateRun.getRunId());
		if (notifyUpdateRun.getDeleted() !=null) {
			hm.put("runId", ""+notifyUpdateRun.getRunId());
			hm.put("deleted", ""+notifyUpdateRun.getDeleted());
		}
		setUser(notifyUpdateRun.getUser());
//		notify("Run", hm);
		notify(notifyUpdateRun.getUser(), "Run", hm);
	}
	

}
