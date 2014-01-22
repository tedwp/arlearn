package org.celstec.arlearn2.android.util;

import java.io.File;
import java.io.IOException;

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

import android.os.Environment;
import org.celstec.arlearn2.android.db.Constants;

public class MediaFolders {

    public static File getIncommingFilesDir(){
        File sdcard = new File(Environment
                .getExternalStorageDirectory().getAbsolutePath());
        File cacheDirFile = new File(sdcard, Constants.CACHE_DIR);
        if (!cacheDirFile.exists()) cacheDirFile.mkdir();
        File incommingDir = new File(cacheDirFile, Constants.INCOMMING);
        if (!incommingDir.exists()) incommingDir.mkdir();
        return incommingDir;
    }

    public static File getOutgoingFilesDir(){
        File sdcard = new File(Environment
                .getExternalStorageDirectory().getAbsolutePath());
        File cacheDirFile = new File(sdcard, Constants.CACHE_DIR);
        if (!cacheDirFile.exists()) cacheDirFile.mkdir();
        File incommingDir = new File(cacheDirFile, Constants.OUTGOING);
        if (!incommingDir.exists()) incommingDir.mkdir();
        return incommingDir;
    }

    public static File createOutgoingJpgFile(){
        File outFolder = getOutgoingFilesDir();
        try {
            return File.createTempFile("image", ".jpg", outFolder);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static File createOutgoingAmrFile(){
        File outFolder = getOutgoingFilesDir();
        try {
            return File.createTempFile("recording", ".amr", outFolder);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
