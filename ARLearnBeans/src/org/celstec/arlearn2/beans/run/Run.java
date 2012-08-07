package org.celstec.arlearn2.beans.run;

import java.io.Serializable;

import org.celstec.arlearn2.beans.game.Game;


public class Run extends RunBean implements Serializable{
	
	private Long gameId;
	private String title;
	private String owner;
	private Long startTime;
	private Long serverCreationTime;
	private Long lastModificationDate;
	private Game game;
	private RunConfig runConfig;
	
	@Override
	public boolean equals(Object obj) {
		Run other = (Run ) obj;
		return super.equals(obj) && 
			nullSafeEquals(getGameId(), other.getGameId()) && 
			nullSafeEquals(getTitle(), other.getTitle()) && 
			nullSafeEquals(getOwner(), other.getOwner()) && 
			nullSafeEquals(getStartTime(), other.getStartTime()) && 
			nullSafeEquals(getServerCreationTime(), other.getServerCreationTime()) && 
			nullSafeEquals(getLastModificationDate(), other.getLastModificationDate()) && 
			nullSafeEquals(getRunConfig(), other.getRunConfig()) && 
			nullSafeEquals(getGame(), other.getGame()); 

	}

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
	
	public Long getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Long lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	
	
//	@Deprecated
//	public DependsOn getGameOverDependsOn() {
//		return gameOverDependsOn;
//	}
//
//	@Deprecated
//	public void setGameOverDependsOn(DependsOn gameOverDependsOn) {
//		this.gameOverDependsOn = gameOverDependsOn;
//	}

	public RunConfig getRunConfig() {
		return runConfig;
	}

	public void setRunConfig(RunConfig runConfig) {
		this.runConfig = runConfig;
	}

	public boolean timeStampCheck() {
		//TODO implement
		return true;
	}
	
}