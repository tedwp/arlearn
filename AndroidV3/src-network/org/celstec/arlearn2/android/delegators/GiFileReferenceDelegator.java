package org.celstec.arlearn2.android.delegators;

import android.os.DropBoxManager;
import android.os.Environment;
import android.util.Log;
import daoBase.DaoConfiguration;
import org.celstec.arlearn2.android.db.Constants;
import org.celstec.arlearn2.android.util.FileDownloader;
import org.celstec.arlearn2.beans.generalItem.FileReference;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.dao.gen.GeneralItemLocalObject;
import org.celstec.dao.gen.GeneralItemMediaLocalObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
public class GiFileReferenceDelegator extends AbstractDelegator{

    private static GiFileReferenceDelegator instance;



    private GiFileReferenceDelegator() {
        ARL.eventBus.register(this);
    }

    public static GiFileReferenceDelegator getInstance() {
        if (instance == null) {
            instance = new GiFileReferenceDelegator();
        }
        return instance;
    }

    public void syncGeneralItemMediaFiles(long giId) {
        syncGeneralItemMediaFiles(DaoConfiguration.getInstance().getGeneralItemLocalObjectDao().load(giId));
    }

    public void syncGeneralItemMediaFiles(GeneralItemLocalObject giLo) {
        if (giLo == null) {
            Log.e("ARLearn", "trying to sync generalItem Media Files for giId that does not exist ");
        } else {
            ARL.eventBus.post(new SyncGeneralItemMediaFiles(giLo));
        }
    }

    public void createReference(GeneralItem gi, GeneralItemLocalObject giDao) {
        if (gi.getFileReferences()== null || gi.getFileReferences().isEmpty()) return;
        HashMap<String, GeneralItemMediaLocalObject> hashMap = new HashMap<String, GeneralItemMediaLocalObject>();
        for (GeneralItemMediaLocalObject gimLO: giDao.getGeneralItemMedia()){
            hashMap.put(gimLO.getLocalId(), gimLO);
        }
        for (FileReference fileReference: gi.getFileReferences()) {
            GeneralItemMediaLocalObject giMediaLO = null;
            if (hashMap.containsKey(fileReference.getKey())) {
                giMediaLO = hashMap.get(fileReference.getKey());
                if (
                        (giMediaLO.getMd5Hash() != null && !giMediaLO.getMd5Hash().equals(fileReference.getMd5Hash()))
                                ||
                        (giMediaLO.getMd5Hash() == null && fileReference.getMd5Hash() != null )
                                ||
                        (giMediaLO.getMd5Hash() == null && !giMediaLO.getRemoteFile().equals(fileReference.getFileReference()))
                   ) {
                    giMediaLO.setReplicated(false);
                    giMediaLO.setMd5Hash(fileReference.getMd5Hash());
                }
                giMediaLO.setRemoteFile(fileReference.getFileReference());
                giMediaLO.setPreferredFileName(fileReference.getKey());
                hashMap.remove(giMediaLO.getLocalId());
            } else {
                giMediaLO = new GeneralItemMediaLocalObject();
                giMediaLO.setGeneralItem(gi.getId());
                giMediaLO.setLocalId(fileReference.getKey());
                giMediaLO.setMd5Hash(fileReference.getMd5Hash());
                giMediaLO.setRemoteFile(fileReference.getFileReference());
                giMediaLO.setPreferredFileName(fileReference.getKey());
                giMediaLO.setReplicated(false);
            }
            DaoConfiguration.getInstance().getGeneralItemMediaLocalObject().insertOrReplace(giMediaLO);
        }
        for (GeneralItemMediaLocalObject gimLO: hashMap.values()){
            new File(gimLO.getLocalUri()).delete();
            new File(gimLO.getLocalUri()+".md5").delete();
            DaoConfiguration.getInstance().getGeneralItemMediaLocalObject().delete(gimLO);
        }
        giDao.resetGeneralItemMedia();
        syncGeneralItemMediaFiles(giDao.getId());
    }

    private synchronized void onEventAsync(SyncGeneralItemMediaFiles sgi) {
        Log.e("ARLearn", "in - "+sgi.getGeneralItem().getTitle());
        String token = returnTokenIfOnline();
        if (token != null) {
            if (sgi.getGeneralItem() != null)
            for (GeneralItemMediaLocalObject mediaObject: sgi.getGeneralItem().getGeneralItemMedia()) {
                if (!mediaObject.getReplicated()) {
                    try {
                        String url = mediaObject.getRemoteFile();
                        FileDownloader fd = new FileDownloader(url, urlToCacheFile(url, mediaObject));
                        fd.setMd5Hash(mediaObject.getMd5Hash());
                        fd.download();
                        if (fd.getTargetLocation().exists()) {
                            mediaObject.setReplicated(true);
                            mediaObject.setLocalUri(fd.getTargetLocation().getAbsolutePath());
                        }
                    } catch (FileNotFoundException fne) {
                        mediaObject.setReplicated(false);
                    } catch (MalformedURLException e) {
                        Log.e("ARLearn", "malformed url", e);
                        mediaObject.setReplicated(true);
                    }
                    DaoConfiguration.getInstance().getGeneralItemMediaLocalObject().insertOrReplace(mediaObject);
                }
            }
        }
            Log.e("ARLearn", "out - "+sgi.getGeneralItem().getTitle());
    }

    private class SyncGeneralItemMediaFiles {
        private GeneralItemLocalObject generalItem;

        private SyncGeneralItemMediaFiles(GeneralItemLocalObject generalItem) {
            this.generalItem = generalItem;
        }

        public GeneralItemLocalObject getGeneralItem() {
            return generalItem;
        }

        public void setGeneralItem(GeneralItemLocalObject generalItem) {
            this.generalItem = generalItem;
        }
    }

    private String downloadFile(String url, GeneralItemMediaLocalObject di, String origUrl) throws FileNotFoundException {
        try {

            URL myFileUrl = new URL(url);
            if (origUrl == null) origUrl = url;
            File outputFile = urlToCacheFile(origUrl, di);
            if (di.getMd5Hash()!= null) {
                File md5file = new File(outputFile+".md5");
                if (md5file.exists()) {
                    String oldmd5 = deserializeString(md5file);
                    if (oldmd5.trim().equals(di.getMd5Hash())){
                        if (outputFile.exists()) {
                            return outputFile.getAbsolutePath();
                        }
                    }
                }
            }
            if (outputFile.exists() && ((outputFile.lastModified() +60000)>System.currentTimeMillis())) {
                return outputFile.getAbsolutePath();
            }

            HttpURLConnection.setFollowRedirects(false); // new
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                String moveUrl = conn.getHeaderField("location");
                conn.disconnect();
                return downloadFile(moveUrl, di, origUrl);
            }
            conn.connect();

            InputStream is = conn.getInputStream();
            // File cacheDir = getCacheDir();
            String cLength = conn.getHeaderField("Content-Length");
            int contentLength = 0;

            if (cLength != null) {
                contentLength = Integer.parseInt(cLength);
                //contentLength is total amount of bytes
//                MediaGeneralItemCache.getInstance(gameId).registerTotalAmountofBytes(di, contentLength);
            }

            FileOutputStream fos = new FileOutputStream(outputFile);
            int len1;
            long byteCounter = 0;
            byte[] buffer = new byte[1024];
            long startTime = System.currentTimeMillis();
            while ((len1 = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len1);
                byteCounter += 1024;
                if ((startTime + 1500) < System.currentTimeMillis()) {
                    startTime = System.currentTimeMillis();
//                    MediaGeneralItemCache.getInstance(gameId).setBytesDownloaded(di, byteCounter);
//                    ActivityUpdater.updateActivities(ctx, ListMessagesActivity.class.getCanonicalName());
                }
            }
//            MediaGeneralItemCache.getInstance(gameId).setBytesDownloaded(di, byteCounter);
//            ActivityUpdater.updateActivities(ctx, ListMessagesActivity.class.getCanonicalName());
            fos.flush();
            fos.close();
            is.close();
            PrintStream out = null;
            try {
                String md5Hash = getMD5Checksum(outputFile.getAbsolutePath());
                out = new PrintStream(new FileOutputStream(outputFile.getAbsolutePath()+".md5"));
                out.print(md5Hash);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } finally {
                if (out != null) out.close();
            }
            return outputFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            Log.e("error while retrieve media item - addToCache", e.getMessage()+" item "+di.getLocalId(), e);
            throw new FileNotFoundException(e.getMessage());

        }
//		return null;
    }

    private String deserializeString(File file)
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
        byte[] b = createChecksum(filename);
        String result = "";

        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

    public static byte[] createChecksum(String filename) throws Exception {
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
        return complete.digest();
    }

    private File urlToCacheFile(String url, GeneralItemMediaLocalObject di) {
        String urlRet = url.hashCode() + getURLSuffix(url);
        if (di.getPreferredFileName()!= null && !"".equals(di.getPreferredFileName().trim()))
            urlRet = di.getPreferredFileName().trim();
        if (urlRet.contains("?"))
            urlRet = urlRet.substring(0, urlRet.indexOf("?"));
        return new File(getCacheDir2(di.getGeneralItemLocalObject().getGameId()), urlRet);

    }

    private File getCacheDir2(long gameId) {
        File sdcard = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        File cacheDirFile = new File(sdcard, Constants.CACHE_DIR);
        if (!cacheDirFile.exists())
            cacheDirFile.mkdir();
        File incommingDir = new File(cacheDirFile, Constants.INCOMMING);
        if (!incommingDir.exists())
            incommingDir.mkdir();
        File gameDir = new File(incommingDir, "" + gameId);
        if (!gameDir.exists())
            gameDir.mkdir();

        return gameDir;
    }

    private String getURLSuffix(String url) {
        String suffix = "";
        int index = url.lastIndexOf("/");
        if (index != -1) {
            suffix = url.substring(index + 1, url.length());
        } else {
            return "";
        }
        if (suffix.contains("/") || suffix.contains("\\")) {
            return "";
        }
        return suffix;
    }

}
