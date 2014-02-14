package org.celstec.arlearn2.android.util;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.celstec.arlearn2.client.GenericClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
public class AppengineFileUploader {

    private Map<String, String> parameters;

    public AppengineFileUploader(long runId, String account, String fileName) {
        this.parameters = new HashMap<String, String>();
        this.parameters.put("runId", ""+runId);
        this.parameters.put("account", ""+account);
        this.parameters.put("fileName", ""+fileName);

    }
    public AppengineFileUploader(Map<String, String> parameters) {
        this.parameters = parameters;

    }

    public String requestUploadUrl(String token) {
        try {
            URL url = new URL(GenericClient.urlPrefix + "/uploadServiceWithUrl");
            if (GenericClient.urlPrefix.endsWith("/")) {
                url = new URL(GenericClient.urlPrefix + "uploadServiceWithUrl");
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("GData-Version", "1.2");
            conn.setRequestProperty("Authorization", "GoogleLogin auth=" + token);

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

            // write parameters

            String parameters = "";
            for (Map.Entry<String, String> entry: this.parameters.entrySet()) {
                parameters = addToParameters(parameters, entry.getKey(), entry.getValue());
            }

            writer.write(parameters);
            writer.close();
            StringBuffer answer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }

            reader.close();

            return answer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String addToParameters(String parameters, String key, String value) {
        if (!"".equals(parameters)) {
            parameters += "&";
        }
        parameters+= key+"="+value;
        return parameters;
    }

    public boolean publishData(String urlString, InputStream is, String mimeType, String fileName) {
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext httpContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(urlString);

            CustomMultiPartEntity multiPartContent = new CustomMultiPartEntity();

            if (mimeType == null) mimeType = "application/octet-stream";
            multiPartContent.addPart("uploaded_file", new InputStreamBody(is, mimeType, fileName));

            httpPost.setEntity(multiPartContent);
            HttpResponse response = httpClient.execute(httpPost, httpContext);
            httpPost.getEntity();
            return true;
        } catch (Exception e) {
            if (e != null)
                Log.e("ARLearn", e.getMessage(), e);

        }
        return false;
    }

    public class CustomMultiPartEntity extends MultipartEntity {

        @Override
        public void writeTo(final OutputStream outStream) throws IOException {
            super.writeTo(new CountingOutputStream(outStream));
        }
    }

    public class CountingOutputStream extends FilterOutputStream {

        private long transferred;
        private long lastUpdateActivities = System.currentTimeMillis();

        public CountingOutputStream(final OutputStream out) {
            super(out);
            this.transferred = 0;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            this.transferred += len;
            if ((System.currentTimeMillis() - lastUpdateActivities) > 1000) {
//				DBAdapter.getAdapter(ctx).getMediaCache().registerBytesAvailable(uri, (int) (totalSize - transferred));
                lastUpdateActivities = System.currentTimeMillis();
            }
        }

        public void write(int b) throws IOException {
            out.write(b);
            this.transferred++;
        }

    }
}
