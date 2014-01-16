package org.celstec.arlearn2.android.genItemActivities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.android.cache.GeneralItemVisibilityCache;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public abstract class GeneralItemActivity extends GeneralActivity {

	protected Handler mHandler = new Handler();
	
	private TextView countDownTextView;

	
	@Override
	protected void onPause() {
		super.onPause();
		mHandler.removeCallbacks(counterTask);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mHandler.postDelayed(counterTask, 300);
	}
	
	protected Runnable counterTask = new Runnable() {
		public void run() {
			Long disappearTime = GeneralItemVisibilityCache.getInstance().disappearedAt(menuHandler.getPropertiesAdapter().getCurrentRunId(), getGeneralItem().getId());
			if (disappearTime == null || disappearTime == -1) {
				mHandler.postDelayed(counterTask, 1000);
				return;
			}
			long millis = (disappearTime - System.currentTimeMillis());
			if (millis <= 0) {
				GeneralItemActivity.this.finish();
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
			if (countDownTextView != null)
				countDownTextView.setText(defaultCountingText);
			mHandler.postDelayed(counterTask, 100);

		}
	};
	
	protected void initCountdownView() {
		if (countDownTextView != null && getGeneralItem().getShowCountDown() != null && getGeneralItem().getShowCountDown()) {
			long disappearTime = GeneralItemVisibilityCache.getInstance().disappearedAt(menuHandler.getPropertiesAdapter().getCurrentRunId(), getGeneralItem().getId());
			if (disappearTime == -1) {
				countDownTextView.setVisibility(View.GONE);
			} else {
				countDownTextView.setVisibility(View.VISIBLE);

			}
		} else {
			if (countDownTextView != null)
				countDownTextView.setVisibility(View.GONE);
		}
	}
	
	protected void getGuiComponents() {
		countDownTextView = (TextView) findViewById(R.id.timeLeftBeforeDisappear);
	}
}
