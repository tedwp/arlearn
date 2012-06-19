package org.celstec.arlearn2.android.sync;

import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.notificationbeans.UpdateScore;
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
		UpdateScore us = new UpdateScore();
		us.setTotalScore(userScore.getTotalScore());
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