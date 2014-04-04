package org.celstec.arlearn2.beans.notification;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
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
public class MessageNotification extends NotificationBean {

    private Long runId;
    private Long threadId;
    private Long messageId;

    public MessageNotification() {
    }

    public MessageNotification(Long runId, Long threadId, Long messageId) {
        this.runId = runId;
        this.threadId = threadId;
        this.messageId = messageId;
    }
    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public static BeanDeserializer deserializer = new BeanDeserializer(){

        @Override
        public MessageNotification toBean(JSONObject object) {
            MessageNotification bean = new MessageNotification();
            try {
                initBean(object, bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }

        public void initBean(JSONObject object, Bean genericBean) throws JSONException {
            super.initBean(object, genericBean);
            MessageNotification bean = (MessageNotification) genericBean;
            if (object.has("runId")) bean.setRunId(object.getLong("runId"));
            if (object.has("threadId")) bean.setThreadId(object.getLong("threadId"));
            if (object.has("messageId")) bean.setMessageId(object.getLong("messageId"));

        }
    };

    public static BeanSerializer serializer = new BeanSerializer () {

        @Override
        public JSONObject toJSON(Object bean) {
            MessageNotification gameBean = (MessageNotification) bean;
            JSONObject returnObject = super.toJSON(bean);
            try {
                if (gameBean.getRunId() != null) returnObject.put("runId", gameBean.getRunId());
                if (gameBean.getThreadId() != null) returnObject.put("threadId", gameBean.getThreadId());
                if (gameBean.getMessageId() != null) returnObject.put("messageId", gameBean.getMessageId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnObject;
        }
    };
}
