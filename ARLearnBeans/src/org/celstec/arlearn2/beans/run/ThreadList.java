package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
public class ThreadList extends Bean {

    public static String ThreadType = "org.celstec.arlearn2.beans.run.Thread";

    private List<Thread> threads = new ArrayList<Thread>();

    public ThreadList() {

    }

    private Long serverTime;


    public List<Thread> getThreads() {
        return threads;
    }

    public void setThreads(List<Thread> threads) {
        this.threads = threads;
    }

    public void addThread(Thread thread) {
        this.threads.add(thread);
    }

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }

    public static BeanSerializer serializer = new BeanSerializer() {

        @Override
        public JSONObject toJSON(Object bean) {
            ThreadList gal = (ThreadList) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (gal.getServerTime() != null)
                    returnObject.put("serverTime", gal.getServerTime());
                if (gal.getThreads() != null)
                    returnObject.put("threads", ListSerializer.toJSON(gal.getThreads()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };

    public static BeanDeserializer deserializer = new BeanDeserializer() {

        @Override
        public ThreadList toBean(JSONObject object) {
            ThreadList tl = new ThreadList();
            try {
                initBean(object, tl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return tl;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            ThreadList giList = (ThreadList) genericBean;
            if (object.has("serverTime"))
                giList.setServerTime(object.getLong("serverTime"));
            if (object.has("threads"))
                giList.setThreads(ListDeserializer.toBean(object.getJSONArray("threads"), Thread.class));
        }
    };
}
