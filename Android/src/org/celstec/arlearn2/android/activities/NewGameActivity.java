package org.celstec.arlearn2.android.activities;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.client.GameClient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class NewGameActivity extends GeneralActivity {
	
	private String CLASSNAME = this.getClass().getName();

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
		storeNewGame(sNewGameName, sGameAuthor, bWithMap);

	}

	private void storeNewGame(String gamename, String gameauthor, Boolean withmap) {


		if ((gamename.length() > 0) && (gameauthor.length() > 0)) {
			if (!lGameTitles.contains(gamename)) {

				Log.d(CLASSNAME, "Insert into DB new game.");
				Game newGame = new Game();
				newGame.setCreator(gameauthor);
				newGame.setTitle(gamename);

				Config newConfig = new Config();
				newConfig.setMapAvailable(withmap);
				newGame.setConfig(newConfig);
				if (!insertGame(newGame)) {
					Log.e(CLASSNAME, "Could not insert Game into DB");
				}

				NewGameActivity.this.finish();

			} else {
				Log.e(CLASSNAME, "There is already an existing game with this title");
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("There is already an existing game with this title").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// NewGameActivity.this.finish();
					}
				});

				builder.create();
			}

		} else {
			Log.e(CLASSNAME, "Give alert in order to fill in the fields.");
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Name and author cannot be empty fields.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// NewGameActivity.this.finish();
				}
			});

			builder.create();
		}

	}

	private boolean insertGame(Game theGame) {

		Game g = new Game();
		PropertiesAdapter pa = new PropertiesAdapter(this);

		g = GameClient.getGameClient().createGame(pa.getFusionAuthToken(), theGame);
		if (g.getErrorCode() != null) {
			if (g.getErrorCode() == 0) { //TODO reactivate the following code
//			if (g.getErrorCode() == GameClient.ERROR_DESERIALIZING) {
				Log.e(CLASSNAME, "Exception deserializing game " + theGame.getTitle() + ".");
				return false;
			}
		}
		return true;
	}

	public boolean isGenItemActivity() {
		return false;
	}
}
