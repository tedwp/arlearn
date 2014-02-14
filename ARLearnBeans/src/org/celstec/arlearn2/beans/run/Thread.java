package org.celstec.arlearn2.beans.run;

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
public class Thread extends Bean {

    private Long threadId;
    private Long runId;
    private Boolean deleted;
    private String name;

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static BeanDeserializer deserializer = new BeanDeserializer(){

        @Override
        public Thread toBean(JSONObject object) {
            Thread bean = new Thread();
            try {
                initBean(object, bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            Thread bean = (Thread) genericBean;
            if (object.has("runId")) bean.setRunId(object.getLong("runId"));
            if (object.has("threadId")) bean.setThreadId(object.getLong("threadId"));
            if (object.has("name")) bean.setName(object.getString("name"));
            if (object.has("deleted")) bean.setDeleted(object.getBoolean("deleted"));
        }
    };

    public static BeanSerializer serializer = new BeanSerializer () {

        @Override
        public JSONObject toJSON(Object bean) {
            Thread thread = (Thread) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (thread.getRunId() != null) returnObject.put("runId", thread.getRunId());
                if (thread.getThreadId() != null) returnObject.put("threadId", thread.getThreadId());
                if (thread.getName() != null) returnObject.put("name", thread.getName());
                if (thread.getDeleted() != null) returnObject.put("deleted", thread.getDeleted());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };
}
