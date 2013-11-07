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

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class NewNarratorItemActivity extends GeneralActivity {

	private String CLASSNAME = this.getClass().getName();

	private NarratorItem ni;
	private int iAction = -1;

	public static int NI_ACTION_EDIT = 0;
	public static int NI_ACTION_CREATE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ni = (NarratorItem) getIntent().getExtras().get("generalItem");
		iAction = getIntent().getExtras().getInt("action");

		setContentView(R.layout.gi_narrator_screen);
		EditText etName = (EditText) findViewById(R.id.etNewGeneralItem);
		EditText etDesc = (EditText) findViewById(R.id.etGIDesc);
		
		
		
		Button ngButton = (Button) findViewById(R.id.bNewGeneralItem);
		if (iAction == NI_ACTION_CREATE) {
			ngButton.setText("Create item");
		} else {
			// Edit
			etName.setText(ni.getName());
			etDesc.setText(ni.getDescription());
			ngButton.setText("Save");
		}

		ngButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				userClickedButton();

			}
		});

	}

	/**
	 * User clicked create item
	 * 
	 */
	private void userClickedButton() {

		String sItemName = ((EditText) findViewById(R.id.etNewGeneralItem)).getText() + "";
		String sDescription = ((EditText) findViewById(R.id.etGIDesc)).getText() + "";

		Log.d(CLASSNAME, " generalItemname:" + sItemName + " description:" + sDescription + ".");
		
		
		// TDOO TRAERSE EL GENERAL ITEM AQUIIIIIIIII ANES DE QUE CONFIRME

		if ((sItemName.length() > 0) && (sDescription.length() > 0)) {

			Log.d(CLASSNAME, "Insert into DB new narrator item.");
			ni.setName(sItemName);
			ni.setDescription(sDescription);
			ni.setType(Constants.GI_TYPE_NARRATOR_ITEM);
			ni.setScope("user");
			ni.setAutoLaunch(false);
			ni.setRichText("aaa");
			OpenQuestion oq = new OpenQuestion();
			oq.setWithAudio(false);
			oq.setWithPicture(true);
			oq.setWithVideo(false);
			ni.setOpenQuestion(oq);

			GeneralItemsDelegator.getInstance().createGeneralItem(this, ni);
			NewNarratorItemActivity.this.finish();

		} else {
			// TODO give alert in order to fill in the fields.
			Log.e(CLASSNAME, "Give alert in order to fill in the fields.");
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Please fill in name and description fields.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// NewGeneralItemActivity.this.finish();
				}
			});

			builder.create();
		}

	}
	
	
	public void onClickLocation(View target){
		ImageView ivTagged = (ImageView) findViewById(R.id.ivTagged);
		ivTagged.setVisibility(View.VISIBLE);
		ivTagged.setImageResource(R.drawable.position_48x);
		
		TextView tvLong = (TextView)findViewById(R.id.tvLongitude);
		TextView tvLat = (TextView)findViewById(R.id.tvLatitude);
		TextView tvRad = (TextView)findViewById(R.id.tvRadius);
		
		Double odLat = null;		
		Double odLng = null;
		Integer oiRad = null;
		try{

			odLat = new Double(tvLat.getText().toString());		
			odLng = new Double(tvLong.getText().toString());
			oiRad = new Integer(tvRad.getText().toString());
			
		}catch(Exception exc){
			exc.printStackTrace();
			odLat = 0.0;		
			odLng = 0.0;
			oiRad = 0;
		}


		//ProximityDependency pd = new ProximityDependency();
		//ni.setDependsOn(pd);
		
		ni.setLat(odLat);
		ni.setLng(odLng);
		ni.setRadius(oiRad);

		TextView tvId = (TextView) findViewById(R.id.tvTagged);
		tvId.setVisibility(View.VISIBLE);
		tvId.setText("Lt:"+odLat+" Ln:"+odLng+" Rd:"+oiRad);

		
		
	}
	
	public void onClickId(View target){

		
		
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
		
		
		
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		ImageView ivTagged = (ImageView) findViewById(R.id.ivTagged);
		ivTagged.setVisibility(View.VISIBLE);
		ivTagged.setImageResource(R.drawable.qrcode_48x);
		
		TextView tvId = (TextView) findViewById(R.id.tvTagged);
		tvId.setVisibility(View.VISIBLE);
		try{

			if(intent.getStringExtra("SCAN_RESULT") != null){		
				tvId.setText(intent.getStringExtra("SCAN_RESULT"));			
			}else{
				tvId.setText("Cold not scan code");
			}
			
		}catch(Exception exc){
			exc.printStackTrace();
			tvId.setText("Cold not scan code");
		}
		System.out.println("Decoded:"+ intent.getStringExtra("SCAN_RESULT"));
		
		
	}



	public boolean isGenItemActivity() {
		return false;
	}

}
