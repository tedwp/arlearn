package org.celstec.arlearn2.tasks.beans;

import org.celstec.arlearn2.delegators.generalitems.CreateGeneralItems;

import com.google.gdata.util.AuthenticationException;

public class DeleteGeneralItems  extends GenericBean {

	private Long gameId;

	public DeleteGeneralItems() {
		super();
	}

	public DeleteGeneralItems(String token, Long gameId) {
		super(token);
		this.gameId = gameId;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	@Override
	public void run() {
		try {
			CreateGeneralItems cgi = new CreateGeneralItems("auth=" + getToken());
			if (getGameId() !=null) cgi.deleteGeneralItems(getGameId());
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		
	}

}
