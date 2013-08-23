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
	Animal[] _animals;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_desc_tab);
		
		final Bundle extras = getIntent().getExtras();

		if (extras != null) {
			selectedGame = (Game) extras.get("selectedGame");
		}

		loadAnimals();
		loadSpinnerAccess();
		loadSpinnerChange();
		loadWebView();

		
	}

	private void loadSpinnerAccess() {
		Spinner spinner = (Spinner) findViewById(R.id.spinAccess);
	
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

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
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.access_array,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	
	private void loadSpinnerChange() {
		Spinner spinner = (Spinner) findViewById(R.id.spinChange);
	
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

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
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.change_array,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
	
	
	private void loadWebView() {
		WebView webView = (WebView) findViewById(R.id.wvDesc);
		String summary = "<html><body>You scored <b>192</b> points.</body></html>";
		webView.loadData(summary, "text/html", "utf-8");
	}

	private void loadAnimals() {
		_animals = new Animal[8];

		// define the display string, the image, and the value to use
		// when the choice is selected
		_animals[0] = new Animal(getString(R.string.licenseby),
				getImg(R.drawable.by), getString(R.string.licenseby));
		_animals[1] = new Animal(getString(R.string.licensebyncsa),
				getImg(R.drawable.by_sa), getString(R.string.licensebysa));
		_animals[2] = new Animal(getString(R.string.licensebync),
				getImg(R.drawable.by_nc), getString(R.string.licensebync));
		_animals[3] = new Animal(getString(R.string.licensebyncnd),
				getImg(R.drawable.by_nc_nd), getString(R.string.licensebyncnd));
		_animals[4] = new Animal(getString(R.string.licensebyncsa),
				getImg(R.drawable.by_nc_sa), getString(R.string.licensebyncsa));
		_animals[5] = new Animal(getString(R.string.licensebynd),
				getImg(R.drawable.by_nd), getString(R.string.licensebynd));
		_animals[6] = new Animal(getString(R.string.licensebynpd),
				getImg(R.drawable.by_npd), getString(R.string.licensebynpd));
		_animals[7] = new Animal(getString(R.string.licensebypd),
				getImg(R.drawable.by_pd), getString(R.string.licensebypd));

	}

	private Drawable getImg(int res) {
		Drawable img = getResources().getDrawable(res);
		img.setBounds(2, 2, 267, 50);
		return img;
	}

	/**
	 * Handle the event to display the AlertDialog with the list
	 */
	public void handlePush(View target) {
		// define the list adapter with the choices
		ListAdapter adapter = new AnimalAdapter(this, _animals);

		final AlertDialog.Builder ad = new AlertDialog.Builder(this);

		ad.setSingleChoiceItems(adapter, -1, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// a choice has been made!
				String selectedVal = _animals[which].getVal();
				Log.d("", "chosen " + selectedVal);
				
				ImageView ivcc = (ImageView)findViewById(R.id.ivCC);
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
	static class AnimalAdapter extends ArrayAdapter {

		private static final int RESOURCE = R.layout.license_row;
		private LayoutInflater inflater;

		static class ViewHolder {
			TextView nameTxVw;
		}

		@SuppressWarnings("unchecked")
		public AnimalAdapter(Context context, Animal[] objects) {
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
						.findViewById(R.id.animal_name_txtvw);
				convertView.setTag(holder);
			} else {
				// view already defined, retrieve view holder
				holder = (ViewHolder) convertView.getTag();
			}

			Animal cat = (Animal) getItem(position);
			if (cat == null) {
				Log.e("pete", "Invalid category for position: " + position);
			}
			holder.nameTxVw.setText(cat.getName());
			holder.nameTxVw
					.setCompoundDrawables(cat.getImg(), null, null, null);

			return convertView;
		}
	}

	/**
	 * POJO for holding each list choice
	 * 
	 */
	class Animal {
		private String _name;
		private Drawable _img;
		private String _val;

		public Animal(String name, Drawable img, String val) {
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

}
