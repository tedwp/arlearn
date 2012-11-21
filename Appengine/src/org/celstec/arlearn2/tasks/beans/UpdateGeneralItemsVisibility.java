package org.celstec.arlearn2.tasks.beans;

import java.util.logging.Logger;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.jdo.manager.GeneralItemVisibilityManager;

import com.google.gdata.util.AuthenticationException;

public class UpdateGeneralItemsVisibility extends GenericBean{
	
	private Long runId;
	private String userEmail;
	private Integer updateType;
	

	private static final Logger log = Logger.getLogger(UpdateGeneralItemsVisibility.class.getName());

	public UpdateGeneralItemsVisibility() {
		

	}
	
	public UpdateGeneralItemsVisibility(String token,Long runId, String userEmail, Integer updateType) {
		super(token);
		this.runId = runId;
		this.userEmail = userEmail;
		this.updateType = updateType;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	

	public Integer getUpdateType() {
		return updateType;
	}

	public void setUpdateType(Integer updateType) {
		this.updateType = updateType;
	}

	@Override
	public void run() {
		switch (updateType) {
		case 1:
			create();
			break;
		case 2:
			delete();
			break;
		default:
			break;
		}
	}
	
	private void delete() {
		GeneralItemVisibilityManager.delete(getRunId(), getUserEmail());
	}

	private void create() {
		GeneralItemDelegator gid = null;
		try {
			gid = new GeneralItemDelegator("auth=" + getToken());
		} catch (AuthenticationException e) {
			e.printStackTrace();
			log.severe("exception "+e.getMessage());
			return;
		}
		GeneralItemList gil = gid.getGeneralItemsRun(getRunId());
		if (gil != null && gil.getGeneralItems() != null) {
			for (GeneralItem gi: gil.getGeneralItems()) {
				if (gi.getDependsOn() == null && (gi.getDeleted() == null || !gi.getDeleted())) {
					GeneralItemVisibilityManager.setItemVisible(gi.getId(), getRunId(), getUserEmail(), GeneralItemVisibilityManager.VISIBLE_STATUS);
				}
			}
		}
	}
}