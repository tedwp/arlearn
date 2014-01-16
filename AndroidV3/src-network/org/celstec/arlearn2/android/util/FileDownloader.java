package org.celstec.arlearn2.android.util;

import android.util.Log;
import org.celstec.arlearn2.android.delegators.ARL;
import org.celstec.arlearn2.android.events.FileDownloadStatus;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;

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
public class FileDownloader {

    private URL originalUrl;
    private File targetLocation;
    private File md5Location;
    private String md5Hash;
    private URL urlCopy;

    public FileDownloader(String url, String targetLocation) throws MalformedURLException{
        this(new URL(url), targetLocation);
    }

    public FileDownloader(String url, File targetLocation) throws MalformedURLException{
        this(url, targetLocation.getAbsolutePath());
    }

    public String getMd5Hash() {
        return md5Hash;
    }

    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }

    public File getTargetLocation() {
        return targetLocation;
    }

    public FileDownloader(URL url, String targetLocation) {
        this.originalUrl = url;
        this.targetLocation = new File(targetLocation);
        this.md5Location  = new File (targetLocation + ".md5");
    }


    public void download() throws FileNotFoundException {
        if (md5HashIsTheSameAsExistingFile()) return;
        try {
            if (urlCopy == null) urlCopy = new URL(originalUrl.toString());
        } catch (MalformedURLException e) {
        }
        try {
            HttpURLConnection.setFollowRedirects(false); // new
            HttpURLConnection conn = (HttpURLConnection) urlCopy.openConnection();
            conn.setDoInput(true);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                urlCopy = new URL(conn.getHeaderField("location"));
                conn.disconnect();
                download();
            }
            conn.connect();

            InputStream is = conn.getInputStream();
            String cLength = conn.getHeaderField("Content-Length");
            int contentLength = 0;

            if (cLength != null) {
                contentLength = Integer.parseInt(cLength);
            }

            FileOutputStream fos = new FileOutputStream(targetLocation);
            int len1;
            long byteCounter = 0;
            byte[] buffer = new byte[1024];
            long startTime = System.currentTimeMillis();
            FileDownloadStatus statusEvent =new FileDownloadStatus(contentLength, 0, FileDownloadStatus.INITIALISING, targetLocation.getName());
            ARL.eventBus.postSticky(statusEvent);
            statusEvent.setStatus(FileDownloadStatus.DOWNLOADING);
            while ((len1 = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len1);
                byteCounter += 1024;
                if ((startTime + 1500) < System.currentTimeMillis()) {
                    startTime = System.currentTimeMillis();
                    statusEvent.setBytesDownloaded(byteCounter);
                    ARL.eventBus.postSticky(statusEvent);
                }
            }
            statusEvent.setStatus(FileDownloadStatus.FINISHED);
            statusEvent.setBytesDownloaded(statusEvent.amountOfBytes);
            ARL.eventBus.postSticky(statusEvent);
            fos.flush();
            fos.close();
            is.close();
            PrintStream out = null;
            try {
                String md5Hash = getMD5Checksum(targetLocation.getAbsolutePath());
                out = new PrintStream(new FileOutputStream(md5Location));
                out.print(md5Hash);
            } catch (Exception e) {
                e.printStackTrace();  //TODO do something with this exception
            } finally {
                if (out != null) out.close();
            }

        } catch (FileNotFoundException e) {
            targetLocation.delete();
            throw e;
        } catch (IOException e) {
            targetLocation.delete();
            Log.e("error while retrieve media item - addToCache", e.getMessage() + " url " + originalUrl, e);
            throw new FileNotFoundException(e.getMessage());

        }
    }

    private boolean md5HashIsTheSameAsExistingFile()  {
        if (md5Hash != null) {
            try {
                String oldMd5Hash = deSerializeString(md5Location);
                if (oldMd5Hash.trim().equals(md5Hash)){
                    return targetLocation.exists();
                }
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    private String deSerializeString(File file)
            throws IOException {
        int len;
        char[] chr = new char[4096];
        final StringBuffer buffer = new StringBuffer();
        final FileReader reader = new FileReader(file);
        try {
            while ((len = reader.read(chr)) > 0) {
                buffer.append(chr, 0, len);
            }
        } finally {
            reader.close();
        }
        return buffer.toString();
    }

    public static String getMD5Checksum(String filename) throws Exception {
        InputStream fis =  new FileInputStream(filename);

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        byte[] b = complete.digest();
        String result = "";

        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

}
