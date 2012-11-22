package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.db.DBAdapter.DatabaseHandler;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.menu.ActionDispatcher;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.celstec.arlearn2.beans.run.Response;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

public class AnswerQuestionActivity extends AnnotateActivity {
	
	private Long generalItemId;
	private NarratorItem narratorItemBean;
	protected TextView countDownTextView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 generalItemId = getIntent().getLongExtra("generalItemId", 0l);
		 narratorItemBean = (NarratorItem) getIntent().getSerializableExtra("bean");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		OpenQuestion oq = narratorItemBean.getOpenQuestion();
		 if (!oq.isWithAudio()) {
			 rad.hide();
			 
		 }
		 if (!oq.isWithPicture() &&!oq.isWithVideo()) {
			 ((TextView) findViewById(R.id.addCaption)).setText("");
		 }
		 if (!oq.isWithPicture()) {
			 tpd.hide();
		 } else {
			 tpd.setCaption();
		 }
		 if (!oq.isWithVideo()) {
			 tvd.hide();
		 } else {
			 tvd.setCaption();
		 }
		 mHandler.removeCallbacks(counterTask);
	     mHandler.postDelayed(counterTask, 100);		    	   
	}
	
	private Handler mHandler = new Handler();

	@Override
	public void onPause() {
		super.onPause();

		mHandler.removeCallbacks(counterTask);
	}
	
	private Runnable counterTask = new Runnable() {
		public void run() {
			countDownTextView = (TextView) findViewById(R.id.timeLeftBeforeDisappear);
			GeneralItem gi = GeneralItemsCache.getInstance().getGeneralItems(generalItemId);
			if (gi == null) {
				countDownTextView.setVisibility(View.INVISIBLE);
			}
			if (gi.getShowCountDown() != null && gi.getShowCountDown()) {
				long disappearTime = GeneralItemVisibilityCache.getInstance().disappearedAt(menuHandler.getPropertiesAdapter().getCurrentRunId(), gi.getId());
				if (disappearTime == -1) {
					countDownTextView.setVisibility(View.INVISIBLE);
				} else {
					countDownTextView.setVisibility(View.VISIBLE);
					long millis = (disappearTime - System.currentTimeMillis());
					if (millis <= 0) {
						AnswerQuestionActivity.this.finish();
						mHandler.removeCallbacks(counterTask);
					}
					String defaultCountingText = "";
					int tens = ((int) millis / 100) % 10;
					int seconds = (int) (millis / 1000);
					int minutes = seconds / 60;
					seconds = seconds % 60;
					if (seconds < 10) {
						defaultCountingText = minutes + ":0" + seconds + "." + tens;
					} else {
						defaultCountingText = minutes + ":" + seconds + "." + tens;
					}
					countDownTextView.setText(defaultCountingText);
				    mHandler.postDelayed(counterTask, 100);		    	   
				}
			} else {
				countDownTextView.setVisibility(View.INVISIBLE);
			}
		}
	};
	
	public Response createResponse(long currentTime) {
		Response r = super.createResponse(currentTime);
		r.setGeneralItemId(generalItemId);
		return r; 
	}
	
	public void publish() {
		PropertiesAdapter pa = new PropertiesAdapter(this);
		ActionDispatcher.publishAction(this, "answer_given", runId, pa.getUsername(), generalItemId, null);
		super.publish();		
	}

}
