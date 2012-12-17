package org.celstec.arlearn2.android.genItemActivities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.GeneralActivity;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceImageAnswerItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest;

import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class SingleChoiceImageActivity extends GeneralActivity {

	private long runId;
	private String account;
	
	private SingleChoiceImageTest mct ;
	private ImageView selectedView;
	private MultipleChoiceAnswerItem selected = null;
	
	private static final int COLUMNS = 2;
	
	@Override
	public boolean isGenItemActivity() {
		return true;
	}
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		runId = getMenuHandler().getPropertiesAdapter().getCurrentRunId();
		account =getMenuHandler().getPropertiesAdapter().getUsername();
		setContentView(R.layout.gi_detail_imagechoice);
		Long id = getIntent().getExtras().getLong("id"); //TODO make constant
		GeneralItem bean = (GeneralItem) getIntent().getExtras().getSerializable("generalItem");
		mct = (SingleChoiceImageTest) bean; //TODO check casting
		
		int amountOfItems = 3;
		TableLayout tableView = (TableLayout) findViewById(R.id.multipleChoiceImageTable);
		TableRow row = null;
		
//		int i = -1;
//		for (MultipleChoiceAnswerItem imageAnswer: mct.getAnswers()){
//			i++;
//			((MultipleChoiceImageAnswerItem) imageAnswer).getImageUrl();
//			allMediaURLs.put(gi.getId(), );
//		}
		
		for (int i = 0; i< amountOfItems; i++) {
			if ((i % COLUMNS) == 0 ) {
				System.out.println("new row");
				if (row != null) {
					tableView.addView(row);
				}
				row = new TableRow(this);
				 
			}
			final ImageView im = new ImageView(this);
			im.setPadding(5, 5, 5, 5);
//			im.setImageResource(R.drawable.add_video);
			im.setImageURI(org.celstec.arlearn2.android.cache.MediaCache.getInstance().getLocalUri(mct.getId()));
			row.addView(im);
			im.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int[] colors = { 0xDD000000, 0xAA000000 };
					float[] radii = { 5, 5, 5, 5, 5, 5, 5, 5 };
					GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
					drawable.setCornerRadii(radii);
					drawable.setStroke(1, 0xFF000000);
					if (selectedView != null) selectedView.setBackgroundDrawable(null);
					im.setBackgroundDrawable(drawable);
					selectedView = im;
					System.out.println("clicked view");
				}
			});
			
		}
		tableView.addView(row);
//		im.setImageResource(R.drawable.add_picture);
//		row.addView(im); 
//		
//		
//		im = new ImageView(this);
//		im.setImageResource(R.drawable.add_video);
//		row.addView(im); 
//		tableView.addView(row);
//	
//		tableView = (TableLayout) findViewById(R.id.multipleChoiceImageTable);
//		row = new TableRow(this);
//		im = new ImageView(this);
//		im.setImageResource(R.drawable.black_pause);
//		row.addView(im); 
//		tableView.addView(row);
	}	

}
