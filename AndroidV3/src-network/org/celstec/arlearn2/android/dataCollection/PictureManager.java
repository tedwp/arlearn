package org.celstec.arlearn2.android.dataCollection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import daoBase.DaoConfiguration;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.util.MediaFolders;
import org.celstec.dao.gen.ResponseLocalObject;

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
public class PictureManager extends DataCollectionManager {

    private File bitmapFile;


    public PictureManager(Activity ctx) {
        super(ctx);
        response.setPictureType();
    }

    public void takeDataSample() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        bitmapFile = MediaFolders.createOutgoingJpgFile();
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(bitmapFile));
        ctx.startActivityForResult(cameraIntent, PICTURE_RESULT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = null;
                String filePath = null;
                if (data != null) {
                    uri = data.getData();
                    filePath = data.getData().getPath();
                } else {
                    uri = Uri.fromFile(bitmapFile);
                    filePath = bitmapFile.getAbsolutePath();
                }
                response.setUriAsString(uri.toString());

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                BitmapFactory.decodeFile(filePath, options);
                response.setContentType(options.outMimeType);
                response.setWidth(options.outWidth);
                response.setHeight(options.outHeight);

                saveResponseForSyncing();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }

    }


}
