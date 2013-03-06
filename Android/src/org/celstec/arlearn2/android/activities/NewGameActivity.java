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
import org.celstec.arlearn2.android.cache.GameCache;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.beans.game.Game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class NewGameActivity extends GeneralActivity {
	protected Button ngButton;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initGuiElements();
	}
	
	protected void initGuiElements() {
		setContentView(R.layout.newgamescreen);

		((EditText) findViewById(R.id.etNewGame)).addTextChangedListener(checkWatcher);
		((EditText) findViewById(R.id.etAuthor)).addTextChangedListener(checkWatcher);
		
		ngButton = (Button) findViewById(R.id.bNewGame);
		ngButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				userClickedNewGameButton();
			}
		});
		ngButton.setEnabled(false);
	}
	
	
	TextWatcher checkWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			ngButton.setEnabled(!"".equals(((EditText) findViewById(R.id.etNewGame)).getText().toString()) && !"".equals(((EditText) findViewById(R.id.etAuthor)).getText().toString()));
		}
	};

	private void userClickedNewGameButton() {
		String sNewGameName = ((EditText) findViewById(R.id.etNewGame)).getText().toString();
		String sGameAuthor = ((EditText) findViewById(R.id.etAuthor)).getText().toString();
		Boolean bWithMap = ((CheckBox) findViewById(R.id.cbWithMap)).isChecked();
		if (checkGameSameTitle(sNewGameName)) {
			GameDelegator.getInstance().createGame(this, sNewGameName, sGameAuthor, bWithMap); //TODO implement a callback to notify user that the deletion worked/failed
			NewGameActivity.this.finish();
		}
	}
	
	
	private boolean checkGameSameTitle(String gameName) {
		for (Game g : GameCache.getInstance().getGames(PropertiesAdapter.getInstance(this).getUsername())) {
			if (gameName.equals(g.getTitle())) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("There is already an existing game with this title").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() { //TODO add string to i18n
					public void onClick(DialogInterface dialog, int id) {
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
}
