package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.menu.ActionDispatcher;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.celstec.arlearn2.beans.run.Response;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
		setVisibility(oq.isWithAudio(), oq.isWithText(), oq.isWithVideo(), oq.isWithPicture());
		mHandler.removeCallbacks(counterTask);
		mHandler.postDelayed(counterTask, 100);
	}

	private void setVisibility(boolean audio, boolean text, boolean video, boolean picture) {
		if (!audio && !text) {
			((LinearLayout) findViewById(R.id.textOrAudio)).setVisibility(View.GONE);
		} else {
			((LinearLayout) findViewById(R.id.textOrAudio)).setVisibility(View.VISIBLE);
			if (audio) {
				((LinearLayout) findViewById(R.id.audio)).setVisibility(View.VISIBLE);
				((LinearLayout) findViewById(R.id.text)).setVisibility(View.GONE);
			} else {
				((LinearLayout) findViewById(R.id.audio)).setVisibility(View.GONE);
				((LinearLayout) findViewById(R.id.text)).setVisibility(View.VISIBLE);

			}
		}
		if (!video && !picture) {
			((LinearLayout) findViewById(R.id.videoOrPicture)).setVisibility(View.GONE);
		} else {
			((LinearLayout) findViewById(R.id.videoOrPicture)).setVisibility(View.VISIBLE);
			if (video) {
				((LinearLayout) findViewById(R.id.picture)).setVisibility(View.GONE);
				((LinearLayout) findViewById(R.id.video)).setVisibility(View.VISIBLE);
			} else {
				((LinearLayout) findViewById(R.id.picture)).setVisibility(View.VISIBLE);
				((LinearLayout) findViewById(R.id.video)).setVisibility(View.GONE);
			}
		}
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
				((LinearLayout) findViewById(R.id.counterLayout)).setVisibility(View.INVISIBLE);
			}
			if (gi.getShowCountDown() != null && gi.getShowCountDown()) {
				long disappearTime = GeneralItemVisibilityCache.getInstance().disappearedAt(menuHandler.getPropertiesAdapter().getCurrentRunId(), gi.getId());
				if (disappearTime == -1) {
//					countDownTextView.setVisibility(View.INVISIBLE);
					((LinearLayout) findViewById(R.id.counterLayout)).setVisibility(View.INVISIBLE);
				} else {
//					countDownTextView.setVisibility(View.VISIBLE);
					((LinearLayout) findViewById(R.id.counterLayout)).setVisibility(View.VISIBLE);
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
//				countDownTextView.setVisibility(View.INVISIBLE);
				((LinearLayout) findViewById(R.id.counterLayout)).setVisibility(View.INVISIBLE);
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
