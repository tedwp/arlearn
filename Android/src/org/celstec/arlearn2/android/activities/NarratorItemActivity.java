package org.celstec.arlearn2.android.activities;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;
import org.celstec.arlearn2.beans.generalItem.OpenQuestion;
import org.celstec.arlearn2.client.GameClient;
import org.celstec.arlearn2.client.GeneralItemClient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NarratorItemActivity extends GeneralActivity {

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
			this.setTitle("New " + ni.getType());
			ngButton.setText("Create item");
		} else {
			// Edit
			this.setTitle("Editing " + ni.getType());
			etName.setText(ni.getName());
			etDesc.setText(ni.getDescription());
			ngButton.setText("Update item");
		}

		ngButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				userClickedButton();

			}
		});

	}

	private void userClickedButton() {

		String sItemName = ((EditText) findViewById(R.id.etNewGeneralItem)).getText() + "";
		String sDescription = ((EditText) findViewById(R.id.etGIDesc)).getText() + "";

		Log.d(CLASSNAME, " generalItemname:" + sItemName + " description:" + sDescription + ".");

		if ((sItemName.length() > 0) && (sDescription.length() > 0)) {

			Log.d(CLASSNAME, "Insert into DB new narrator item.");
			ni.setName(sItemName);
			ni.setDescription(sDescription);
			ni.setType(Constants.GI_TYPE_NARRATOR_ITEM);

			// TOCHECK
			// Following are default vaules and probably useless
			ni.setScope("user");
			ni.setAutoLaunch(false);
			ni.setRichText("aaa");
			OpenQuestion oq = new OpenQuestion();
			oq.setWithAudio(false);
			oq.setWithPicture(true);
			oq.setWithVideo(false);
			ni.setOpenQuestion(oq);

			if (!insertGeneralItem(ni)) {
				// TODO Could not insert GeneralItem into DB
				Log.e(CLASSNAME, "Could not insert GeneralItem into DB");
				// ProgressDialog.show(NewGeneralItemActivity.this,
				// "ARLearn", "Could not create generalItem");
			}
			NarratorItemActivity.this.finish();

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

	private boolean insertGeneralItem(GeneralItem theGeneralItem) {

		GeneralItem g = new GeneralItem();
		PropertiesAdapter pa = new PropertiesAdapter(this);

		// Commented by btb
		//g = GeneralItemClient.getGeneralItemClient().createGeneralItem(pa.getFusionAuthToken(), theGeneralItem);

		if (g.getErrorCode() != null) {

			Log.e(CLASSNAME, "Exception deserializing narrator item " + theGeneralItem.getName() + ".");
			return false;

		}
		return true;

	}

	public boolean isGenItemActivity() {
		return false;
	}

}
