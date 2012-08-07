package org.celstec.arlearn2.android.answerQuestion;

import java.io.File;
import java.io.FileOutputStream;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnnotateActivity;
import org.celstec.arlearn2.android.activities.AnswerQuestionActivity;
import org.celstec.arlearn2.android.activities.ViewAnswerActivity;
import org.celstec.arlearn2.android.util.MediaFolders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class TakePictureDelegate {

	AnnotateActivity ctx;
	private ImageView image;
	private File bitmapFile;
	private Uri pictureUri;



	public TakePictureDelegate(AnnotateActivity answerQuestionActivity) {
		this.ctx = answerQuestionActivity;
		image = (ImageView) ctx.findViewById(R.id.photoFrame);
		initImageCapture();
	}

	public void hide() {
		image.setVisibility(View.GONE);
	}
	
	private void initImageCapture() {
		image.setImageResource(R.drawable.voegfototoeen);
		bitmapFile = null;
		image.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {

				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//				bitmapFile = MediaFolders.createOutgoingJpgFile();
//				cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(MediaFolders.getOutgoingFilesDir(), "tmp.jpg")));
				// cameraIntent = new
				// Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				ctx.terminateRecordingOrPlaying();
				ctx.startActivityForResult(cameraIntent, ctx.CAMERA_PIC_REQUEST);
			}
		});
	}

	public void onActivityResult(Intent data) {
		pictureUri = data.getData();
		Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
		
//		Bitmap thumbnail = ViewAnswerActivity.decodeFile(new File(MediaFolders.getOutgoingFilesDir(), "tmp.jpg"), 900);
		try {
			bitmapFile = MediaFolders.createOutgoingJpgFile();
			FileOutputStream out = new FileOutputStream(bitmapFile);
			thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, out);
		} catch (Exception e) {
			Log.e("bitmap", "failed to save", e);
		}
		setPictureInFrame(thumbnail);
		ctx.displayPublishButton();
		
	}
	
	private void setPictureInFrame(Bitmap thumbnail) {
		image.setImageBitmap(thumbnail);
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
	public File getBitmapFile(){
		return bitmapFile;
	}

	public String getImagePath() {
		if (bitmapFile == null) return null;
		return bitmapFile.getAbsolutePath();
	}
	
	public Uri getUri(){
		return pictureUri;
	}

	
}
