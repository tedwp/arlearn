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
