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

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.beans.game.Game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class GameDescActivity extends Activity {

	private Game selectedGame = null;
	CCLicence[] aLicenses;
	Contact[] aContacts;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_desc_tab);

		final Bundle extras = getIntent().getExtras();

		if (extras != null) {
			selectedGame = (Game) extras.get("selectedGame");
		}

		loadGameData();

	}

	private void loadGameData() {

		TextView tv_1 = (TextView) findViewById(R.id.tvTitleText);
		if (selectedGame.getTitle() != null) {
			tv_1.setText(selectedGame.getTitle());
		} else {
			tv_1.setText("Sample game");
		}

		TextView tv_2 = (TextView) findViewById(R.id.tvGameDescText);
		if (selectedGame.getDescription() != null) {
			tv_2.setText(selectedGame.getDescription());
		} else {
			tv_2.setText("This is a game for JTELSS 2014 and consist in sampling experiences.");
		}

		TextView tv_3 = (TextView) findViewById(R.id.tvGameIdText);
		if (selectedGame.getGameId() != null) {
			tv_3.setText(selectedGame.getGameId().toString());
		} else {
			tv_3.setText("Sample game");
		}

		loadLicence();
		loadContacts();
		loadSpinnerAccess();
		loadSpinnerChange();
		loadWebView();

	}

	private void loadSpinnerAccess() {
		Spinner spinner = (Spinner) findViewById(R.id.spinAccess);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Log.d("", "chosen " + arg2);
				// TODO Auto-generated method stub

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				Log.d("", "Nothing selected ");
			}

		});
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.access_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	private void loadSpinnerChange() {
		Spinner spinner = (Spinner) findViewById(R.id.spinChange);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Log.d("", "chosen " + arg2);
				// TODO Auto-generated method stub

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				Log.d("", "Nothing selected ");
			}

		});
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.change_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	private void loadWebView() {
		WebView webView = (WebView) findViewById(R.id.wvDesc);
		String summary = "<html><body>You scored <b>192</b> points.</body></html>";
		webView.loadData(summary, "text/html", "utf-8");
	}

	private void loadLicence() {
		aLicenses = new CCLicence[8];

		// define the display string, the image, and the value to use
		// when the choice is selected
		aLicenses[0] = new CCLicence(getString(R.string.licenseby),
				getImg(R.drawable.by), getString(R.string.licenseby));
		aLicenses[1] = new CCLicence(getString(R.string.licensebyncsa),
				getImg(R.drawable.by_sa), getString(R.string.licensebysa));
		aLicenses[2] = new CCLicence(getString(R.string.licensebync),
				getImg(R.drawable.by_nc), getString(R.string.licensebync));
		aLicenses[3] = new CCLicence(getString(R.string.licensebyncnd),
				getImg(R.drawable.by_nc_nd), getString(R.string.licensebyncnd));
		aLicenses[4] = new CCLicence(getString(R.string.licensebyncsa),
				getImg(R.drawable.by_nc_sa), getString(R.string.licensebyncsa));
		aLicenses[5] = new CCLicence(getString(R.string.licensebynd),
				getImg(R.drawable.by_nd), getString(R.string.licensebynd));
		aLicenses[6] = new CCLicence(getString(R.string.licensebynpd),
				getImg(R.drawable.by_npd), getString(R.string.licensebynpd));
		aLicenses[7] = new CCLicence(getString(R.string.licensebypd),
				getImg(R.drawable.by_pd), getString(R.string.licensebypd));

	}
	
	private void loadContacts() {
		aContacts = new Contact[6];

		// define the display string, the image, and the value to use
		// when the choice is selected
		aContacts[0] = new Contact("Bernardo Tabuenca 1",
				getImg(R.drawable.user), true);
		aContacts[1] = new Contact("Bernardo Tabuenca 2",
				getImg(R.drawable.user), false);
		aContacts[2] = new Contact("Bernardo Tabuenca 3",
				getImg(R.drawable.user), true);
		aContacts[3] = new Contact("Bernardo Tabuenca 4",
				getImg(R.drawable.user), false);
		aContacts[4] = new Contact("Bernardo Tabuenca 5",
				getImg(R.drawable.user), false);
		aContacts[5] = new Contact("Bernardo Tabuenca 6",
				getImg(R.drawable.user), true);


	}

	private Drawable getImg(int res) {
		Drawable img = getResources().getDrawable(res);
		img.setBounds(2, 2, 267, 50);
		return img;
	}
	
	
	/**
	 * Handle the event to display the AlertDialog with the list
	 */
	public void handleClickShare(View target) {


		ListAdapter adapter = new ContactAdapter(this, aContacts);
		final AlertDialog.Builder ad = new AlertDialog.Builder(this);

		ad.setSingleChoiceItems(adapter, -1, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// a choice has been made!
				boolean selectedVal = aContacts[which].getVal();
				Log.d("", aContacts[which].getName()+" chosen " + selectedVal);


				// ADD images of users here
				ImageView ivcc = (ImageView) findViewById(R.id.ivShare);
				ivcc.setImageResource(R.drawable.add_user_48x);
				dialog.dismiss();
			}
		});

		ad.show();
	}
	
	
	

	/**
	 * Handle the event to display the AlertDialog with the list
	 */
	public void handleClickCC(View target) {
		// define the list adapter with the choices
		ListAdapter adapter = new CCLicenceAdapter(this, aLicenses);

		final AlertDialog.Builder ad = new AlertDialog.Builder(this);

		ad.setSingleChoiceItems(adapter, -1, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// a choice has been made!
				String selectedVal = aLicenses[which].getVal();
				Log.d("", "chosen " + selectedVal);

				ImageView ivcc = (ImageView) findViewById(R.id.ivCC);
				ivcc.setImageResource(R.drawable.by_npd);
				dialog.dismiss();
			}
		});

		ad.show();
	}

	/**
	 * Definition of the list adapter...uses the View Holder pattern to optimize
	 * performance.
	 */
	static class CCLicenceAdapter extends ArrayAdapter {

		private static final int RESOURCE = R.layout.license_row;
		private LayoutInflater inflater;

		static class ViewHolder {
			TextView nameTxVw;
		}

		@SuppressWarnings("unchecked")
		public CCLicenceAdapter(Context context, CCLicence[] objects) {
			super(context, RESOURCE, objects);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				// inflate a new view and setup the view holder for future use
				convertView = inflater.inflate(RESOURCE, null);

				holder = new ViewHolder();
				holder.nameTxVw = (TextView) convertView.findViewById(R.id.tvLicenseRow);
				convertView.setTag(holder);
			} else {
				// view already defined, retrieve view holder
				holder = (ViewHolder) convertView.getTag();
			}

			CCLicence cat = (CCLicence) getItem(position);
			if (cat == null) {
				Log.e("pete", "Invalid category for position: " + position);
			}
			holder.nameTxVw.setText(cat.getName());
			holder.nameTxVw
					.setCompoundDrawables(cat.getImg(), null, null, null);

			return convertView;
		}
	}

	class CCLicence {
		private String _name;
		private Drawable _img;
		private String _val;

		public CCLicence(String name, Drawable img, String val) {
			_name = name;
			_img = img;
			_val = val;
		}

		public String getName() {
			return _name;
		}

		public Drawable getImg() {
			return _img;
		}

		public String getVal() {
			return _val;
		}
	}

	
	/**
	 * Handle the event to display the AlertDialog with the list
	 */
	public void handleClickCntct(View target) {
		// define the list adapter with the choices
		ListAdapter adapter = new ContactAdapter(this, aContacts);

		final AlertDialog.Builder ad = new AlertDialog.Builder(this);

		ad.setSingleChoiceItems(adapter, -1, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// a choice has been made!
				String selectedVal = aLicenses[which].getVal();
				Log.d("", "chosen " + selectedVal);

				//Image View ivcc = (ImageView) findViewById(R.id.ivShare);
				//dialog.set

				dialog.dismiss();
			}
		});

		ad.show();
	}

	/**
	 * Definition of the list adapter...uses the View Holder pattern to optimize
	 * performance.
	 */
	static class ContactAdapter extends ArrayAdapter {

		private static final int RESOURCE = R.layout.license_row;
		private LayoutInflater inflater;

		static class ViewHolder {
			TextView nameTxVw;
		}

		@SuppressWarnings("unchecked")
		public ContactAdapter(Context context, Contact[] objects) {
			super(context, RESOURCE, objects);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				// inflate a new view and setup the view holder for future use
				convertView = inflater.inflate(RESOURCE, null);

				holder = new ViewHolder();
				holder.nameTxVw = (TextView) convertView
						.findViewById(R.id.tvLicenseRow);
				convertView.setTag(holder);
			} else {
				// view already defined, retrieve view holder
				holder = (ViewHolder) convertView.getTag();
			}
		
			
			//holder.nameTxVw.setBackgroundColor(color.background_light);

			
			Contact cat = (Contact) getItem(position);
			if (cat == null) {
				Log.e("pete", "Invalid category for position: " + position);
			}
			holder.nameTxVw.setText(cat.getName());
			holder.nameTxVw.setCompoundDrawables(cat.getImg(), null, null, null);

			return convertView;
		}
	}

	class Contact {
	
		private String _name;
		private Drawable _img;
		private boolean _val;

		public Contact(String name, Drawable img, boolean val) {
			_name = name;
			_img = img;
			_val = val;
		}

		public String getName() {
			return _name;
		}

		public Drawable getImg() {
			return _img;
		}

		public boolean getVal() {
			return _val;
		}
	}


	
	
	
	
	
}
