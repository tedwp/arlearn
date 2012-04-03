package org.celstec.arlearn2.beans.run;

public class UserScore extends RunBean{

	private long userScore;
	private long teamScore;
	private long allScore;
	
	private long totalScore;
	
	public UserScore() {
		
	}

	public long getUserScore() {
		return userScore;
	}

	public void setUserScore(long userScore) {
		this.userScore = userScore;
	}

	public long getTeamScore() {
		return teamScore;
	}

	public void setTeamScore(long teamScore) {
		this.teamScore = teamScore;
	}

	public long getAllScore() {
		return allScore;
	}

	public void setAllScore(long allScore) {
		this.allScore = allScore;
	}

	public long getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(long totalScore) {
		this.totalScore = totalScore;
	}

}
