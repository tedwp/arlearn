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
 * Contributors: Bernardo Tabuenca
 ******************************************************************************/
package org.celstec.arlearn2.android.activities;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest;
import org.celstec.arlearn2.client.GameClient;
import org.celstec.arlearn2.client.GeneralItemClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MultipleChoiceItemActivity extends Activity {

	private String CLASSNAME = this.getClass().getName();
	private MultipleChoiceTest mct;

	TableLayout tl;
	TableRow tr_header;
	EditText etChoiceText;
	CheckBox cbChoiceCorrect;

	int iNumRows = 0;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gi_multchoice_screen);

		final Bundle extras = getIntent().getExtras();

		if (extras != null) {
			mct = (MultipleChoiceTest) extras.get("generalItem");
		}

		tl = (TableLayout) findViewById(R.id.tableLayoutB);
		tr_header = (TableRow) findViewById(R.id.tableRowB1);
		etChoiceText = (EditText) findViewById(R.id.etChoiceText);
		cbChoiceCorrect = (CheckBox) findViewById(R.id.checkBoxA1);

		Button buttonCreateMC = (Button) findViewById(R.id.buttonCreateItem);
		buttonCreateMC.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MultipleChoiceItemActivity.this, "Clicked Add item button", Toast.LENGTH_SHORT).show();
				userClickedAddMCButton();

			}

		});

		Button buttonAddOption = (Button) findViewById(R.id.buttonAdd);
		buttonAddOption.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toast.makeText(MultipleChoiceActivity.this, "Num of rows:" +
				// iNumRows, Toast.LENGTH_SHORT).show();
				iNumRows++;

				TableRow tr = new TableRow(v.getContext());
				tr.setId(iNumRows);

				CheckBox cbCorrect = new CheckBox(v.getContext());
				cbCorrect.setText(etChoiceText.getText());
				cbCorrect.setTextColor(Color.GRAY);
				cbCorrect.setChecked(cbChoiceCorrect.isChecked());
				// android:layout_column="2"
				tr.addView(cbCorrect);

				TextView tvText = new TextView(v.getContext());
				tvText.setId(iNumRows);
				tvText.setTextColor(Color.GRAY);
				// tvText.setPadding(5, 5, 5, 5);
				tr.addView(tvText);// add the column to the table row here

				ImageView img = new ImageView(v.getContext());
				img.setId(iNumRows);
				img.setImageResource(R.drawable.delete_48x48);
				img.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						for (int i = 0; i < tl.getChildCount(); i++) {
							View vChild = tl.getChildAt(i);
							if (vChild.getId() == v.getId()) {
								// Toast.makeText(MultipleChoiceActivity.this,
								// "Clicked delete item " + i,
								// Toast.LENGTH_SHORT).show();
								tl.removeViewAt(i);
							}
						}

					}
				});
				tr.addView(img);

				tr_header.setVisibility(View.VISIBLE);
				tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			}
		});

	}

	private void userClickedAddMCButton() {

		// Capture values to be saved
		String sItemName = ((EditText) findViewById(R.id.etNewMultipleChoice)).getText() + "";
		String sDescription = ((EditText) findViewById(R.id.etGIDesc)).getText() + "";
		Log.d(CLASSNAME, "Creating multiple choice question with " + sItemName + " and " + sDescription);

		if ((sItemName.length() > 0) && (sDescription.length() > 0)) {
			mct.setText(sItemName);
			mct.setRichText(sDescription);
			mct.setDeleted(false);
			mct.setScope("user");
			mct.setName(sItemName);
			mct.setAutoLaunch(false);
			mct.setType(Constants.GI_TYPE_MULTIPLE_CHOICE);

			List<MultipleChoiceAnswerItem> lmcai = new ArrayList<MultipleChoiceAnswerItem>();

			for (int i = 1; i < tl.getChildCount(); i++) {
				TableRow trChild = (TableRow) tl.getChildAt(i);
				CheckBox cbChild = (CheckBox) trChild.getChildAt(0);
				// TextView tvChild = (TextView)trChild.getChildAt(1);

				MultipleChoiceAnswerItem mcai = new MultipleChoiceAnswerItem();
				mcai.setAnswer(cbChild.getText().toString());
				mcai.setType(Constants.GI_TYPE_MULTIPLE_CHOICE_ANSWER);
				mcai.setIsCorrect(cbChild.isChecked());
				lmcai.add(mcai);

				Log.d(CLASSNAME, " - Option " + mcai.getAnswer() + " " + mcai.getIsCorrect() + " ");
			}
			mct.setAnswers(lmcai);

			

			GeneralItemsDelegator.getInstance().createGeneralItem(this, mct);
			MultipleChoiceItemActivity.this.finish();

		} else {
			// TODO give alert in order to fill in the fields.
			Log.e(CLASSNAME, "Fields were not filled in!!!");
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Please fill in name and description fields.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// NewGeneralItemActivity.this.finish();
				}
			});

			builder.create();
		}

	}

//	private boolean insertGeneralItem(GeneralItem theGeneralItem) {
//
//		GeneralItem g = new GeneralItem();
//		PropertiesAdapter pa = new PropertiesAdapter(this);
//
//		g = GeneralItemClient.getGeneralItemClient().createGeneralItem(pa.getFusionAuthToken(), theGeneralItem);
//
//		if (g.getErrorCode() != null) {
//			if (g.getErrorCode() == GameClient.ERROR_DESERIALIZING) {
//				Log.e("Exception deserializing game ", theGeneralItem.getName() + ".");
//				return false;
//			}
//		}
//		return true;
//
//	}

}