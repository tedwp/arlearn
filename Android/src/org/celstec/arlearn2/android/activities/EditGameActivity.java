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
