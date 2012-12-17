package org.celstec.arlearn2.android.activities;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.delegators.GameDelegator;
import org.celstec.arlearn2.beans.game.Game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class NewGameActivity extends GeneralActivity {
	
	Game[] existingGames = null;
	List<String> lGameTitles;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.newgamescreen);

		lGameTitles = new ArrayList<String>();

		Button ngButton = (Button) findViewById(R.id.bNewGame);
		ngButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				userClickedNewGameButton();

			}
		});
	}

	private void userClickedNewGameButton() {

		String sNewGameName = ((EditText) findViewById(R.id.etNewGame)).getText() + "";
		String sGameAuthor = ((EditText) findViewById(R.id.etAuthor)).getText() + "";
		Boolean bWithMap = ((CheckBox) findViewById(R.id.cbWithMap)).isChecked();
		if (checkFields(sNewGameName, sGameAuthor) && checkGameSameTitle(sNewGameName)) {
			GameDelegator.getInstance().createGame(this, sNewGameName, sGameAuthor, bWithMap);
		}
	}
	
	private boolean checkFields(String gamename, String gameauthor) {
		if ((gamename.length() == 0) || (gameauthor.length() == 0)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Name and author cannot be empty fields.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// NewGameActivity.this.finish();
				}
			});
			builder.create();
			return false;
		}
		return true;
	}
	
	private boolean checkGameSameTitle(String gameName) {
		if (lGameTitles.contains(gameName)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("There is already an existing game with this title").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// NewGameActivity.this.finish();
				}
			});

			builder.create();
			return false;
		}
		return true;
	}

	public boolean isGenItemActivity() {
		return false;
	}
}
