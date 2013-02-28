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
package org.celstec.arlearn2.beans.run;

import java.io.Serializable;

import org.celstec.arlearn2.beans.game.Game;


public class Run extends RunBean implements Serializable, Comparable<Run>{
	
	private Long gameId;
	private String title;
	private String owner;
	private String tagId;
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
			nullSafeEquals(getTagId(), other.getTagId()) && 
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

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
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

	@Override
	public int compareTo(Run o) {
		int result = getTitle().compareToIgnoreCase(o.getTitle());
		if (result != 0) return result;
		return getRunId().compareTo(o.getRunId());
	}
	
}
