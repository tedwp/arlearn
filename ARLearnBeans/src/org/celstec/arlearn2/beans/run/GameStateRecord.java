package org.celstec.arlearn2.beans.run;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;

//TODO: revisit this class
public class GameStateRecord extends RunBean {
	
	private HashMap<String, Long> userScores = new HashMap<String, Long>();
	private HashMap<String, Long> teamScores = new HashMap<String, Long>();
	private HashMap<String, Long> allScores = new HashMap<String, Long>();
	private HashMap<String, Long> userProgress = new HashMap<String, Long>();
	private HashMap<String, Long> teamProgress = new HashMap<String, Long>();
	private HashMap<String, Long> allProgress = new HashMap<String, Long>();
	private String runState = "";
	
	public GameStateRecord() {
		super();
	}

	public HashMap<String, Long> getUserScores() {
		return userScores;
	}

	public void setUserScores(HashMap<String, Long> userScores) {
		this.userScores = userScores;
	}

	public HashMap<String, Long> getTeamScores() {
		return teamScores;
	}

	public void setTeamScores(HashMap<String, Long> teamScores) {
		this.teamScores = teamScores;
	}

	public HashMap<String, Long> getAllScores() {
		return allScores;
	}

	public void setAllScores(HashMap<String, Long> allScores) {
		this.allScores = allScores;
	}

	public HashMap<String, Long> getUserProgress() {
		return userProgress;
	}

	public void setUserProgress(HashMap<String, Long> userProgress) {
		this.userProgress = userProgress;
	}

	public HashMap<String, Long> getTeamProgress() {
		return teamProgress;
	}

	public void setTeamProgress(HashMap<String, Long> teamProgress) {
		this.teamProgress = teamProgress;
	}

	@XmlElement(name="allProgress", nillable = true,required = true)

	public HashMap<String, Long> getAllProgress() {
		return allProgress;
	}

	public void setAllProgress(HashMap<String, Long> allProgress) {
		this.allProgress = allProgress;
	}

	public String getRunState() {
		return runState;
	}

	public void setRunState(String runState) {
		this.runState = runState;
	}

}
