package org.celstec.arlearn2.tasks.beans;

import java.util.List;

import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.celstec.arlearn2.beans.notification.RunModification;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.UserList;
import org.celstec.arlearn2.cache.VisibleGeneralItemsCache;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;
import org.celstec.arlearn2.jdo.manager.GeneralItemVisibilityManager;
import org.codehaus.jettison.json.JSONException;

import com.google.gdata.util.AuthenticationException;

public class NotifyUsersFromGame extends GenericBean {

	private long runId;
	private String gi;
	private int modificationType;

	public NotifyUsersFromGame(){
		
	}
	
	public NotifyUsersFromGame(String token, long runId, String gi, int modificationType) {
		super(token);
		this.runId = runId;
		this.gi = gi;
		this.modificationType = modificationType;

	}
	public long getRunId() {
		return runId;
	}

	public void setRunId(long runId) {
		this.runId = runId;
	}

	public String getGi() {
		return gi;
	}

	public void setGi(String gi) {
		this.gi = gi;
	}

	public int getModificationType() {
		return modificationType;
	}

	public void setModificationType(int modificationType) {
		this.modificationType = modificationType;
	}
	
	@Override
	public void run() {
		try {
			UsersDelegator ud = new UsersDelegator("auth=" + getToken());
			UserList ul = ud.getUsers(runId);
			GeneralItemModification gim = new GeneralItemModification();
			gim.setModificationType(modificationType);
			gim.setRunId(runId);
			gim.setGeneralItem((GeneralItem) JsonBeanDeserializer.deserialize(gi));
			
			for (User u : ul.getUsers()) {
				if (u.getDeleted() == null || !u.getDeleted()) {
//				RunModification rm = new RunModification();
//				rm.setModificationType(RunModification.CREATED);
//				rm.setRun((new RunDelegator(ud)).getRun(getRunId()));
				VisibleGeneralItemsCache.getInstance().removeGeneralItemList(runId, u.getEmail());
				if (modificationType == GeneralItemModification.CREATED) {
					if (gim.getGeneralItem().getDependsOn() == null && (gim.getGeneralItem().getDeleted() == null || !gim.getGeneralItem().getDeleted())) {
						GeneralItemVisibilityManager.setItemVisible(gim.getGeneralItem().getId(), getRunId(), u.getEmail(), GeneralItemVisibilityManager.VISIBLE_STATUS);
					}
				}
				if (modificationType == GeneralItemModification.VISIBLE) {
					if (gim.getGeneralItem().getDeleted() == null || !gim.getGeneralItem().getDeleted()) {
						GeneralItemVisibilityManager.setItemVisible(gim.getGeneralItem().getId(), getRunId(), u.getEmail(), GeneralItemVisibilityManager.VISIBLE_STATUS);
					}
				}
				if (modificationType == GeneralItemModification.DELETED) {
					GeneralItemVisibilityManager.delete(getRunId(), gim.getGeneralItem().getId(), u.getEmail(), null);
				}
				ChannelNotificator.getInstance().notify(u.getEmail(), gim);
				}
			}
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
