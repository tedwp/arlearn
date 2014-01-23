package org.celstec.arlearn2.android.dataCollection;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.util.MediaFolders;

import java.io.File;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
public class VideoManager extends DataCollectionManager {

    private File videoFile;
    private Uri cameraVideoURI;
    public VideoManager(Activity ctx) {
        super(ctx);
        response.setVideoType();
    }

    public void takeDataSample() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
        String fileName = ARL.time.getServerTime()+".mp4";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.TITLE, fileName);
        cameraVideoURI = ctx.getContentResolver()
                .insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraVideoURI);
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
//        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, MAXIMUM_VIDEO_SIZE);

        ctx.startActivityForResult(cameraIntent, VIDEO_RESULT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            String filePath = null;
            if (data != null) {
                uri = data.getData();
                filePath = data.getData().getPath();
            } else {
                uri = Uri.fromFile(videoFile);
                filePath = videoFile.getAbsolutePath();
            }
            response.setUriAsString(uri.toString());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                Bitmap bmp = null;

                try {
                    retriever.setDataSource(ctx, uri);
                    bmp = retriever.getFrameAtTime();

                    response.setHeight(bmp.getHeight());
                    response.setWidth(bmp.getWidth());
                } catch (RuntimeException ex) {
                    // Assume this is a corrupt video file.
                } finally {
                    try {
                        retriever.release();
                    } catch (RuntimeException ex) {
                        // Ignore failures while cleaning up.
                    }
                }
            }
            saveResponseForSyncing();
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // User cancelled the image capture
        } else {
            // Image capture failed, advise user
        }

    }
}
