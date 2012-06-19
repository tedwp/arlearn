package org.celstec.arlearn2.beans.run;

public class UserScore extends RunBean{

	private Long userScore;
	private Long teamScore;
	private Long allScore;
	
	private Long totalScore;
	
	public UserScore() {
		
	}

	public Long getUserScore() {
		return userScore;
	}

	public void setUserScore(Long userScore) {
		this.userScore = userScore;
	}

	public Long getTeamScore() {
		return teamScore;
	}

	public void setTeamScore(Long teamScore) {
		this.teamScore = teamScore;
	}

	public Long getAllScore() {
		return allScore;
	}

	public void setAllScore(Long allScore) {
		this.allScore = allScore;
	}

	public Long getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Long totalScore) {
		this.totalScore = totalScore;
	}

}
