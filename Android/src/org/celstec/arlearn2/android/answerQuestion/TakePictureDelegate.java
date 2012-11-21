package org.celstec.arlearn2.android.answerQuestion;


import java.io.File;
import java.io.FileOutputStream;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnnotateActivity;
import org.celstec.arlearn2.android.util.MediaFolders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TakePictureDelegate {

	AnnotateActivity ctx;
	private ImageView image;
	private TextView addImageCaption;

	private File bitmapFile;
	private Uri pictureUri;



	public TakePictureDelegate(AnnotateActivity answerQuestionActivity) {
		this.ctx = answerQuestionActivity;
		image = (ImageView) ctx.findViewById(R.id.photoFrame);
		addImageCaption = (TextView) ctx.findViewById(R.id.addCaption);

		initImageCapture();
	}

	public void hide() {
		image.setVisibility(View.GONE);
	}
	
	public void setCaption() {
		addImageCaption.setText(ctx.getString(R.string.addPicture));

	}
	
	private void initImageCapture() {
		image.setImageResource(R.drawable.add_picture);
		pictureUri = null;

		image.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				ctx.setRecordingMedia();
//				Intent cameraIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				bitmapFile = MediaFolders.createOutgoingJpgFile();
				cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(bitmapFile));
				ctx.terminateRecordingOrPlaying();
				ctx.startActivityForResult(cameraIntent, ctx.CAMERA_PIC_REQUEST);
			}
		});
	}

	public void onActivityResult(Intent data) {
//		if (data != null) { // TODO: I guess data is always null when extra_ouput is set
//		pictureUri = data.getData();
//		pictureUri = null;
//		if (pictureUri == null) {
//			Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//			try {
//                bitmapFile = MediaFolders.createOutgoingJpgFile();
//                FileOutputStream out = new FileOutputStream(bitmapFile);
//                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                pictureUri = Uri.fromFile(bitmapFile);
//			} catch (Exception e) {
//                Log.e("bitmap", "failed to save", e);
//			}
//		}
//		} else {
		if (bitmapFile != null) pictureUri = Uri.fromFile(bitmapFile);
//		}
		setPictureInFrame();	
		ctx.displayPublishButton();
		
	}
	
	private void setPictureInFrame() {
		
		image.setImageURI(pictureUri);
		image.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
				builder.setMessage(ctx.getString(R.string.removePicture));
				builder.setCancelable(false);
				builder.setPositiveButton(ctx.getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						initImageCapture();

						ctx.displayPublishButton();
					}
				});
				builder.setNegativeButton(ctx.getString(R.string.no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
				// removeButton.setVisibility(removeButton.INVISIBLE);
			}
		});
	}
	
	public Uri getUri(){
		if (bitmapFile == null && pictureUri== null) return null;
		if (pictureUri != null) return pictureUri;
		return Uri.fromFile(bitmapFile);
	}

	public void reset() {
		bitmapFile = null;
		pictureUri = null;
		initImageCapture();
	}

	public void setPictureUri(Uri uri) {
		pictureUri = uri;
		if (uri != null) {
			setPictureInFrame();
			ctx.displayPublishButton();
		}
	}

	
}
