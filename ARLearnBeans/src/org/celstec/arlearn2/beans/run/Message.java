package org.celstec.arlearn2.beans.run;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.RunBeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.RunBeanSerialiser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Message extends RunBean{

    private Long messageId;
    private Long threadId;
    private Long runId;
    private String subject;
	private String body;
    private Long date;
    private String userIds;
    private String teamIds;

    public Message() {}


    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public static RunBeanSerialiser serializer = new RunBeanSerialiser(){

		@Override
		public JSONObject toJSON(Object bean) {
			Message message = (Message) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (message.getSubject() != null) returnObject.put("subject", message.getSubject());
				if (message.getBody() != null) returnObject.put("body", message.getBody());
                if (message.getRunId() != null) returnObject.put("runId", message.getRunId());
                if (message.getThreadId() != null) returnObject.put("threadId", message.getThreadId());
                if (message.getMessageId() != null) returnObject.put("messageId", message.getMessageId());
                if (message.getDate() != null) returnObject.put("date", message.getDate());


			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};
	
	public static RunBeanDeserializer deserializer = new RunBeanDeserializer(){
		@Override
		public Message toBean(JSONObject object) {
			Message gi = new Message();
			try {
				initBean(object, gi);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return gi;
		}
		
		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			Message message = (Message) genericBean;
			if (object.has("subject")) message.setSubject(object.getString("subject"));
			if (object.has("body")) message.setBody(object.getString("body"));

            if (object.has("runId")) message.setRunId(object.getLong("runId"));
            if (object.has("threadId")) message.setThreadId(object.getLong("threadId"));
            if (object.has("messageId")) message.setMessageId(object.getLong("messageId"));
            if (object.has("date")) message.setDate(object.getLong("date"));
		}

	};
}
