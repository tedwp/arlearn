package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.menu.ActionDispatcher;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.celstec.arlearn2.beans.run.Response;
import android.os.Bundle;
import android.widget.TextView;

public class AnswerQuestionActivity extends AnnotateActivity {
	
	private Long generalItemId;
	private NarratorItem narratorItemBean;
	
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
//			 ((ImageView) findViewById(R.id.photoFrame)).setImageResource(R.drawable.voegfototoe);
//			 ((ImageView) findViewById(R.id.photoFrame)).setVisibility(View.VISIBLE);
		 }
//		 tpd.hide();
		 if (!oq.isWithVideo()) {
			 tvd.hide();
		 } else {
			 tvd.setCaption();
		 }
	}
	
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
