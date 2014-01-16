package org.celstec.arlearn2.android.events;

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
public class FileDownloadStatus {

    public static final int INITIALISING = 1;
    public static final int DOWNLOADING = 2;
    public static final int FINISHED = 3;

    public long amountOfBytes;
    public long bytesDownloaded;
    public int status;
    public String fileName;

    public FileDownloadStatus(long amountOfBytes, long bytesDownloaded, int status, String fileName) {
        this.amountOfBytes = amountOfBytes;
        this.bytesDownloaded = bytesDownloaded;
        this.status = status;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getAmountOfBytes() {
        return amountOfBytes;
    }

    public void setAmountOfBytes(long amountOfBytes) {
        this.amountOfBytes = amountOfBytes;
    }

    public long getBytesDownloaded() {
        return bytesDownloaded;
    }

    public void setBytesDownloaded(long bytesDownloaded) {
        this.bytesDownloaded = bytesDownloaded;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
