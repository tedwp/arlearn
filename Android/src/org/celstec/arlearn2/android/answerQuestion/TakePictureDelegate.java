package org.celstec.arlearn2.android.answerQuestion;


import java.io.File;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnnotateActivity;
import org.celstec.arlearn2.android.util.MediaFolders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TakePictureDelegate {

	AnnotateActivity ctx;
	private ImageView photoFrame;
	private TextView addImageCaption;
	private LinearLayout picture;

	private File bitmapFile;
	private Uri pictureUri;



	public TakePictureDelegate(AnnotateActivity answerQuestionActivity) {
		this.ctx = answerQuestionActivity;
		photoFrame = (ImageView) ctx.findViewById(R.id.photoFrame);
		addImageCaption = (TextView) ctx.findViewById(R.id.addCaptionPhoto);
		picture = (LinearLayout) ctx.findViewById(R.id.picture);

		initImageCapture();
	}


	public void setCaption() {
		addImageCaption.setText(ctx.getString(R.string.addPicture));

	}
	
	private void initImageCapture() {
		picture.setVisibility(View.VISIBLE);
		addImageCaption.setVisibility(View.VISIBLE);
		photoFrame.setImageResource(R.drawable.add_picture);
		pictureUri = null;

		photoFrame.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				ctx.setRecordingMedia();
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				bitmapFile = MediaFolders.createOutgoingJpgFile();
				cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(bitmapFile));
				ctx.terminateRecordingOrPlaying();
				ctx.startActivityForResult(cameraIntent, ctx.CAMERA_PIC_REQUEST);
			}
		});
	}

	public void onActivityResult(Intent data) {
		if (bitmapFile != null) pictureUri = Uri.fromFile(bitmapFile);
		setPictureInFrame();	
		ctx.displayPublishButton();
		
	}
	
	private void setPictureInFrame() {
		
		photoFrame.setImageURI(pictureUri);
		addImageCaption.setVisibility(View.GONE);
		photoFrame.setOnClickListener(new View.OnClickListener() {
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
