package org.celstec.arlearn2.beans.generalItem;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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
public class FileReference extends Bean {

    private String key;

    private String md5Hash;

    private String fileReference;

    public FileReference() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMd5Hash() {
        return md5Hash;
    }

    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }

    public String getFileReference() {
        return fileReference;
    }

    public void setFileReference(String fileReference) {
        this.fileReference = fileReference;
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        FileReference other = (FileReference) obj;
        return nullSafeEquals(getKey(), other.getKey())
                && nullSafeEquals(getMd5Hash(), other.getMd5Hash())
                && nullSafeEquals(getFileReference(), other.getFileReference());
    }

    public static BeanDeserializer deserializer = new BeanDeserializer(){

        @Override
        public FileReference toBean(JSONObject object) {
            FileReference mct = new FileReference();
            try {
                initBean(object, mct);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mct;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            FileReference fileReference = (FileReference) genericBean;
            if (object.has("key")) fileReference.setKey(object.getString("key"));
            if (object.has("md5Hash")) fileReference.setMd5Hash(object.getString("md5Hash"));
            if (object.has("fileReference")) fileReference.setFileReference(object.getString("fileReference"));
        }
    };

    public static BeanSerializer serializer = new BeanSerializer(){

        @Override
        public JSONObject toJSON(Object bean) {
            FileReference fileReference = (FileReference) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (fileReference.getKey() != null) returnObject.put("key", fileReference.getKey());
                if (fileReference.getMd5Hash() != null) returnObject.put("md5Hash", fileReference.getMd5Hash());
                if (fileReference.getFileReference() != null) returnObject.put("fileReference", fileReference.getFileReference());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }

    };
}
