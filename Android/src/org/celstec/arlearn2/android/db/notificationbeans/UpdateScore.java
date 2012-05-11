package org.celstec.arlearn2.android.db.notificationbeans;

import org.celstec.arlearn2.android.db.PropertiesAdapter;

import android.content.Context;

public class UpdateScore extends NotificationBean {

	private Long userScore;
	private Long teamScore;
	private Long allScore;

	private Long totalScore;

	public UpdateScore() {
		
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

	public boolean requiresBroadcast() {
		return true;
	}
	
	public void run(Context ctx) {
		PropertiesAdapter pa = new PropertiesAdapter(ctx);
		pa.setTotalScore(getTotalScore());
	}
}
