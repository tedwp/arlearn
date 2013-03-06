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
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.client.GameClient;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class EditGameActivity extends NewGameActivity {

	Game selectedGame = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		final Bundle extras = getIntent().getExtras();
		selectedGame = (Game) extras.get("selectedGame");
		super.onCreate(savedInstanceState);
	}
	
	protected void initGuiElements() {
		setContentView(R.layout.newgamescreen);

		ngButton = (Button) findViewById(R.id.bNewGame);
		ngButton.setText("Update");
		ngButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onClickUpdateButton();
				EditGameActivity.this.finish();
			}
		});
		ngButton.setEnabled(false);

		EditText etName = (EditText) findViewById(R.id.etNewGame);
		etName.setText(selectedGame.getTitle());
		etName.addTextChangedListener(checkWatcher);

		EditText etAuthor = (EditText) findViewById(R.id.etAuthor);
		etAuthor.setText(selectedGame.getCreator());
		etAuthor.addTextChangedListener(checkWatcher);

		CheckBox cbWithMap = (CheckBox) findViewById(R.id.cbWithMap);
		cbWithMap.setChecked(selectedGame.getConfig().getMapAvailable());
	}

	

	private void onClickUpdateButton() {
		Game g = new Game();
		Config c = new Config();
		g.setConfig(c);
		g.setGameId(selectedGame.getGameId());
		g.setTitle(((EditText) findViewById(R.id.etNewGame)).getText() + "");
		g.setCreator(((EditText) findViewById(R.id.etAuthor)).getText() + "");
		c.setMapAvailable(((CheckBox) findViewById(R.id.cbWithMap)).isChecked());
		
		GameDelegator.getInstance().createGame(this, g);
	}

}
