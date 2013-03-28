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
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.android.answerQuestion;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnnotateActivity;
import org.celstec.arlearn2.android.util.MediaFolders;

import com.sun.imageio.plugins.common.InputStreamAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
	
	private int width;
	private int height;



	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}


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
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize = 8;
		options.inPurgeable = true;

		try {
			Bitmap preview_bitmap=BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(pictureUri),null,options);
			width = preview_bitmap.getWidth()*8;
			height = preview_bitmap.getHeight()*8;
			

			photoFrame.setImageBitmap(preview_bitmap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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
