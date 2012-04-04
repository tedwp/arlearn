package org.celstec.arlearn2.tasks.beans;

import java.util.logging.Logger;

import org.celstec.arlearn2.delegators.notification.Notification;
import org.celstec.arlearn2.delegators.notification.UpdateRunNotification;

public class NotifyUpdateRun extends GenericBean{
    private static final Logger log = Logger.getLogger(NotifyUpdateRun.class.getName());

	private Long runId;
	private Boolean created;
	private Boolean deleted;
	private String user;
	
	public NotifyUpdateRun() {
		
	}

	public NotifyUpdateRun(String token, Long runId, boolean created, boolean deleted, String user) {
		super(token);
		this.runId = runId;
		this.created = created;
		this.deleted = deleted;
		this.user = user;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public Boolean getCreated() {
		return created;
	}

	public void setCreated(Boolean created) {
		this.created = created;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public void run() {
		log.severe("NotifyUpdateRun is run "+getToken());
		//Scope is user. As the user is created he gets a notification that the run is available
		if (!"GoogleLogin auth=".equals(getToken())) {
			UpdateRunNotification urn = new UpdateRunNotification(Notification.USER, getRunId(), getToken());
			if (getCreated() != null && getCreated())
				urn.runCreated(this);
			if (getDeleted() !=null && getDeleted()) urn.runDeleted(this);
		} else {
			log.severe("no authentication token was provided");
		}
	}
	
}
