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
public class MessageList extends Bean {

    public static String MessageType = "org.celstec.arlearn2.beans.run.Message";

    private List<Message> messages = new ArrayList<Message>();

    public MessageList() {

    }

    private Long serverTime;


    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
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
            MessageList gal = (MessageList) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (gal.getServerTime() != null)
                    returnObject.put("serverTime", gal.getServerTime());
                if (gal.getMessages() != null)
                    returnObject.put("messages", ListSerializer.toJSON(gal.getMessages()));
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
            MessageList giList = (MessageList) genericBean;
            if (object.has("serverTime"))
                giList.setServerTime(object.getLong("serverTime"));
            if (object.has("messages"))
                giList.setMessages(ListDeserializer.toBean(object.getJSONArray("messages"), Message.class));
        }
    };
}
