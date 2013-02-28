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
package org.celstec.arlearn2.android.sync;

import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.run.UserScore;
import org.celstec.arlearn2.client.ScoreClient;

import android.content.Context;
import android.content.Intent;

public class ScoreSyncroniser extends GenericSyncroniser {

	PropertiesAdapter pa ;
	
	public ScoreSyncroniser(Context ctx) {
		super(ctx);
		pa = new PropertiesAdapter(ctx);
	}

	@Override
	protected void runAuthenticated() {
		//TODO switch on scoring
//		if (pa.getCurrentRunId() != -1) {
//			boolean increase = syncScore(ctx);
//			if (increase) {
//				increaseDelay();
//			} else {
//				resetDelay();
//			}
//		}	
	}
	
	private boolean syncScore(Context ctx) {
		UserScore userScore = ScoreClient.getScoreClient().getScore(pa.getFusionAuthToken(), pa.getCurrentRunId());
		//TODO reimplement scoring
		//		UpdateScore us = new UpdateScore();
//		us.setTotalScore(userScore.getTotalScore());
//		Intent intent = new Intent(NotificationService.BROADCAST_ACTION);	
//		intent.putExtra("bean", us);
//    	ctx.sendBroadcast(intent);
    	if (pa.getTotalScore() == userScore.getTotalScore()) {
    		return true;
    	}
    	pa.setTotalScore(userScore.getTotalScore());
		return false;
	}

}
