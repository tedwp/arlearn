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
package org.celstec.arlearn2.android.genItemActivities;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.celstec.arlearn2.android.Constants;
import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.delegators.GeneralItemsDelegator;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.NarratorItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import org.celstec.arlearn2.android.activities.UploadGeneralItemActivity;

public class PictureItemActivity extends Activity {

	private String CLASSNAME = this.getClass().getName();

	// MediaRecorder recorder;

	// private File audiofile = null;
	//
	//
	// // Object to be uploaded
	private GeneralItem pictureBean = null;
	// private Uri newUri = null;

	private static final int CAMERA_REQUEST = 1888;
	private ImageView imageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle extras = getIntent().getExtras();
		pictureBean = (NarratorItem) extras.get("generalItem");

		setContentView(R.layout.gi_picture_screen);

		this.imageView = (ImageView) this.findViewById(R.id.imageViewPicture);
		Button photoButton = (Button) this.findViewById(R.id.buttonPicture);
		photoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			imageView.setImageBitmap(photo);

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					PictureItemActivity.this);
			alertDialog.setTitle("Publish file...");
			alertDialog.setMessage("Do you want to publish this file?");
			alertDialog.setIcon(R.drawable.cloud_up_48);

			alertDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// User pressed YES button. Write Logic Here
							uploadItem();
							Toast.makeText(getApplicationContext(),
									"Picture successfully taken",
									Toast.LENGTH_SHORT).show();
							PictureItemActivity.this.finish();

						}
					});

			alertDialog.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// User pressed No button. Write Logic Here
							Toast.makeText(getApplicationContext(),
									"You clicked on NO", Toast.LENGTH_SHORT)
									.show();
							// TODO delete file from HD??
						}
					});

			// Setting Netural "Cancel" Button
			alertDialog.setNeutralButton("Watch first",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// User pressed Cancel button. Write Logic Here
							Toast.makeText(getApplicationContext(),
									"You clicked on watch it first",
									Toast.LENGTH_SHORT).show();

						}
					});

			// Showing Alert Message
			alertDialog.show();
		}

	}

	/**
	 * Clicked "yes" on alert message prompting to upload the item
	 * 
	 */
	private void uploadItem() {

		PropertiesAdapter pa = new PropertiesAdapter(this);

		pictureBean.setType(Constants.GI_TYPE_PICTURE);
		pictureBean.setDeleted(false);
		pictureBean.setScope("user");

		EditText etName = (EditText) findViewById(R.id.editTextAOName);
		if (etName.getText().length() > 0) {
			pictureBean.setName(etName.getText().toString());
		} else {
			pictureBean.setName(getFileName());
		}
		EditText etDesc = (EditText) findViewById(R.id.editTextAODescription);
		if (etDesc.getText().length() > 0) {
			pictureBean.setDescription(etDesc.getText().toString());
		} else {
			pictureBean.setDescription("");
		}
		// EditText etRichText = (EditText)
		// findViewById(R.id.editTextAORichText);
		// if (etRichText.getText().length() > 0) {
		// pictureBean.setRichText(etRichText.getText().toString());
		// } else {
		// pictureBean.setRichText("");
		// }

		// TODO pending to update this with uploaded url of blob
		// pictureBean.setAudioFeed("http://www.celstec.org" +
		// audiofile.getAbsolutePath());

		// System.out.println("Publishing audio recording ..." +
		// audiofile.getName());

		Toast.makeText(this, "Publishing picture ..." + pictureBean.getName(),
				Toast.LENGTH_LONG).show();

		// Upload file into Blob store
		// GeneralItemsDelegator.getInstance().uploadGeneralItem(this,
		// pictureBean, pa.getUsername(), newUri);

		// Create item in GenealItemJDO
		GeneralItemsDelegator.getInstance()
				.createGeneralItem(this, pictureBean);




	}

	/**
	 * Returns an string with a file name based on current time
	 * 
	 * @return
	 */
	private String getFileName() {

		Format dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Date date = new Date();
		// long lCurrentTime = date.getTime();
		String newDateString = "/arlearn_audio"
				+ dateFormat.format(date).replace(" ", "_").replace(":", "")
				+ ".3gp";
		return newDateString;
	}

}