package org.celstec.arlearn2.android.genItemActivities;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.beans.GeneralItem;
import org.celstec.arlearn2.android.db.beans.MultipleChoiceTest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

@Deprecated
public class OpenQuestionActivity extends Activity{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gi_detail_openquestion);
		String id = getIntent().getExtras().getString(GeneralItem.ID);
		DBAdapter db = new DBAdapter(OpenQuestionActivity.this);
		db.openForRead();
		GeneralItem gi = (GeneralItem) db.table(DBAdapter.GENERALITEM_ADAPTER).queryById(id);
		MultipleChoiceTest mct = new MultipleChoiceTest(gi);
		db.close();
		TextView tv = (TextView) findViewById(R.id.openQuestionText);
		tv.setText(mct.getQuestion());
	}
}
