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
    private String title;
	private String messageBody;
    private Long date;
    private String userIds;
    private String teamIds;

    public Message() {}

    
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public static RunBeanSerialiser serializer = new RunBeanSerialiser(){

		@Override
		public JSONObject toJSON(Object bean) {
			Message message = (Message) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (message.getTitle() != null) returnObject.put("title", message.getTitle());
				if (message.getMessageBody() != null) returnObject.put("messageBody", message.getMessageBody());
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
			if (object.has("title")) message.setTitle(object.getString("title"));
			if (object.has("messageBody")) message.setMessageBody(object.getString("messageBody"));
		}

	};
}
