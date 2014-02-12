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
import org.celstec.arlearn2.android.cache.GameCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GameAccess;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;

public class NewGameActivity extends GeneralActivity {

	CCLicence[] aLicenses;
	int iLicense = -1;
	String sLicense = "";
	int iAccess = -1;
	int iPermission = -1;
	
//	private Activity context;
//
//	public NewGameActivity(Activity context) {
//		this.context = context;
//	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newgamescreen);

		loadLicenceArray();
		// loadContacts();
		loadSpinnerAccess();
		loadSpinnerPermission();

	}

	private void loadSpinnerAccess() {

		Spinner spinner = (Spinner) findViewById(R.id.spinAccess);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Log.d("", "chosen " + arg2);
				iAccess = arg2 + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Log.d("", "Nothing selected ");
			}

		});

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.access_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

	}

	private void loadSpinnerPermission() {
		Spinner spinner = (Spinner) findViewById(R.id.spinChange);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Log.d("", "chosen " + arg2);
				iPermission = arg2 + 1;

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

	private void loadLicenceArray() {

		aLicenses = new CCLicence[8];

		// define the display string, the image, and the value to use
		// when the choice is selected
		aLicenses[Constants.CC_BYPD] = new CCLicence(getString(R.string.licensebypd),
				getImg(R.drawable.by_pd), getString(R.string.licensebypd));		
		aLicenses[Constants.CC_BY] = new CCLicence(getString(R.string.licenseby),
				getImg(R.drawable.by), getString(R.string.licenseby));
		aLicenses[Constants.CC_BYSA] = new CCLicence(getString(R.string.licensebysa),
				getImg(R.drawable.by_sa), getString(R.string.licensebysa));
		aLicenses[Constants.CC_BYND] = new CCLicence(getString(R.string.licensebynd),
				getImg(R.drawable.by_nd), getString(R.string.licensebynd));		
		aLicenses[Constants.CC_BYNC] = new CCLicence(getString(R.string.licensebync),
				getImg(R.drawable.by_nc), getString(R.string.licensebync));
		aLicenses[Constants.CC_BYNCSA] = new CCLicence(getString(R.string.licensebyncsa),
				getImg(R.drawable.by_nc_sa), getString(R.string.licensebyncsa));
		aLicenses[Constants.CC_BYNCND] = new CCLicence(getString(R.string.licensebyncnd),
				getImg(R.drawable.by_nc_nd), getString(R.string.licensebyncnd));
		aLicenses[Constants.CC_BYNPD] = new CCLicence(getString(R.string.licensebynpd),
				getImg(R.drawable.by_npd), getString(R.string.licensebynpd));
	}


	
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

				switch (which) {
				case Constants.CC_BY:
					ivcc.setImageResource(R.drawable.by);
					break;
				case Constants.CC_BYSA:
					ivcc.setImageResource(R.drawable.by_sa);
					break;
				case Constants.CC_BYNC:
					ivcc.setImageResource(R.drawable.by_nc);
					break;
				case Constants.CC_BYNCND:
					ivcc.setImageResource(R.drawable.by_nc_nd);
					break;
				case Constants.CC_BYNCSA:
					ivcc.setImageResource(R.drawable.by_nc_sa);
					break;
				case Constants.CC_BYND:
					ivcc.setImageResource(R.drawable.by_nd);
					break;
				case Constants.CC_BYNPD:
					ivcc.setImageResource(R.drawable.by_npd);
					break;
				case Constants.CC_BYPD:
					ivcc.setImageResource(R.drawable.by_pd);
					break;

				default:
					break;
				}
				dialog.dismiss();
			}
		});

		ad.show();
	}


	public void onClickCreate(View target) {
		String sNewGameName = ((EditText) findViewById(R.id.etNewGame))
				.getText().toString();
		String sGameDesc = ((EditText) findViewById(R.id.etGameDescText))
				.getText().toString();
		Boolean bWithMap = ((CheckBox) findViewById(R.id.cbWithMap))
				.isChecked();
		// Boolean bWithMap = ((Spinner)
		// findViewById(R.id.spinAccess)).getSelectedItem()

		System.out.println("Name:" + sNewGameName);
		System.out.println("Desc:" + sGameDesc);
		System.out.println("Map:" + bWithMap);
		System.out.println("CCLic:" + sLicense);
		System.out.println("Access:" + iAccess);
		System.out.println("iPerm:" + iPermission);

		Game newGame = new Game();
		newGame.setTitle(sNewGameName);
		newGame.setDescription(sGameDesc);
		newGame.setLicenseCode(sLicense);
		newGame.setSharing(iAccess);

		Config c = new Config();
		c.setMapAvailable(bWithMap);
		newGame.setConfig(c);

		// TODO Pending to link to created game
		GameAccess ga = new GameAccess();
		ga.setAccessRights(iPermission);

		if (checkGameSameTitle(sNewGameName)) {
			GameDelegator.getInstance().createGame(this, newGame);
			
//			GameAccess gaAux = new GameAccess();
//			gaAux = GameCache.getInstance().getGameAccess(newGame.getGameId());			
//
			Intent intent = new Intent(NewGameActivity.this, ListGamesActivity.class);
//			intent.putExtra("selectedAction", Constants.AUTHORING_ACTION_EDIT);
//			intent.putExtra("selectedGame", newGame);
//			intent.putExtra("selectedGameAccess", gaAux);
			NewGameActivity.this.startActivity(intent);		
			NewGameActivity.this.finish();

		}
	}

	private boolean checkGameSameTitle(String gameName) {
		for (Game g : GameCache.getInstance().getGames(
				PropertiesAdapter.getInstance(this).getFullId())) {
			if (gameName.equals(g.getTitle())) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(
						"There is already an existing game with this title")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() { // TODO
									// add
									// string
									// to
									// i18n
									public void onClick(DialogInterface dialog,
											int id) {
									}
								});
				builder.create();
				return false;
			}
		}
		return true;
	}

	public boolean isGenItemActivity() {
		return false;
	}

	private Drawable getImg(int res) {
		Drawable img = getResources().getDrawable(res);
		img.setBounds(2, 2, 267, 50);
		return img;
	}
}
