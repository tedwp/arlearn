package org.celstec.arlearn2.tasks.beans;

import java.util.List;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.delegators.RunDelegator;

import com.google.gdata.util.AuthenticationException;

public class NotifyRunsFromGame extends GenericBean {

	private long gameId;
	private int modificationType;
	private String gi;
	
	public NotifyRunsFromGame() {
		
	}
	
	public NotifyRunsFromGame(String token, Long gameId,  GeneralItem gi, Integer modificationType) {
		super(token);
		this.gameId = gameId;
		this.gi = gi.toString();
		this.modificationType = modificationType;
	}

	
	public long getGameId() {
		return gameId;
	}


	public void setGameId(long gameId) {
		this.gameId = gameId;
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
			RunDelegator rd = new RunDelegator("auth=" + getToken());
			List<Run> runList = rd.getRunsForGame(gameId);
			for (Run r: runList) {
				if (r.getDeleted() == null || !r.getDeleted()) (new NotifyUsersFromGame(getToken(), r.getRunId(), gi, modificationType)).scheduleTask();

			}
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
