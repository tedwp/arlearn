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
package org.celstec.arlearn2.android.score;

import java.util.ArrayList;
import java.util.Iterator;

import org.celstec.arlearn2.android.R;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ScoreHandler {

	private Activity activity;

	public ScoreHandler(Activity activity) {
		this.activity = activity;
	}
	
//	public void setScore(UpdateScore score) {
//		if (score.getTotalScore() == null) return;
//		setScore((int) score.getTotalScore().longValue());
//		
//	}
	
	public void setScore(Integer newScore) {
		LinearLayout ll = (LinearLayout) activity.findViewById(R.id.scoreLinearLayout);
		ll.removeAllViews();
		ImageView scoreView = new ImageView(activity);
		scoreView.setImageResource(R.drawable.score1);
		ll.addView(scoreView);
		
		if (newScore < 0) {
			scoreView = new ImageView(activity);
			scoreView.setImageResource(R.drawable.scoremin);
			ll.addView(scoreView);
			newScore = newScore * -1;
		}
		Iterator<Integer> it =getScoreParts(newScore).iterator();
		
		while (it.hasNext()) {
			ll.addView(getViewForInt(it.next()));	
		}
		
		scoreView = new ImageView(activity);
		scoreView.setImageResource(R.drawable.score4);
		ll.addView(scoreView);
	}
	
	private ImageView getViewForInt(Integer intNr) {
		ImageView scoreView = new ImageView(activity);
		switch (intNr) {
		case 0:
			scoreView.setImageResource(R.drawable.score20);
			break;
		case 1:
			scoreView.setImageResource(R.drawable.score21);
			break;
		case 2:
			scoreView.setImageResource(R.drawable.score22);
			break;
		case 3:
			scoreView.setImageResource(R.drawable.score23);
			break;
		case 4:
			scoreView.setImageResource(R.drawable.score24);
			break;
		case 5:
			scoreView.setImageResource(R.drawable.score25);
			break;
		case 6:
			scoreView.setImageResource(R.drawable.score26);
			break;
		case 7:
			scoreView.setImageResource(R.drawable.score27);
			break;
		case 8:
			scoreView.setImageResource(R.drawable.score28);
			break;
		case 9:
			scoreView.setImageResource(R.drawable.score29);
			break;

		default:
			break;
		}
		return scoreView;
	}

	private ArrayList<Integer> getScoreParts(Integer score) {
		if (score < 10 && score >= 0) {
			ArrayList<Integer> result = new ArrayList<Integer>();
			result.add(score);
			return result;
		}
		if (score < 0) {
			ArrayList<Integer> result = new ArrayList<Integer>();
			result.add(0);
			return result;
		}
		Integer lastInt = score % 10;
		ArrayList<Integer> result = getScoreParts(score / 10);
		result.add(lastInt);
		return result;

	}

	

}
