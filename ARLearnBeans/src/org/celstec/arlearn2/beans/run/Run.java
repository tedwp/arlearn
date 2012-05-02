package org.celstec.arlearn2.beans.run;

import java.io.Serializable;

import org.celstec.arlearn2.beans.game.DependsOn;
import org.celstec.arlearn2.beans.game.Game;


public class Run extends RunBean implements Serializable{
	
	private Long gameId;
	private String title;
	private String owner;
	private Long startTime;
	private Long serverCreationTime;
	private Game game;
	
	@Deprecated
	private DependsOn gameOverDependsOn;

	public Run(){
		
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getServerCreationTime() {
		return serverCreationTime;
	}

	public void setServerCreationTime(Long serverCreationTime) {
		this.serverCreationTime = serverCreationTime;
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	@Deprecated
	public DependsOn getGameOverDependsOn() {
		return gameOverDependsOn;
	}

	@Deprecated
	public void setGameOverDependsOn(DependsOn gameOverDependsOn) {
		this.gameOverDependsOn = gameOverDependsOn;
	}

	public boolean timeStampCheck() {
		//TODO implement
		return true;
	}
	
}