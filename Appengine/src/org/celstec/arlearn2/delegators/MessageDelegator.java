package org.celstec.arlearn2.delegators;

import java.util.logging.Logger;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.run.Message;
import org.celstec.arlearn2.beans.run.MessageList;
import org.celstec.arlearn2.jdo.manager.MessageManager;

public class MessageDelegator extends GoogleDelegator {
	private static final Logger logger = Logger.getLogger(MessageDelegator.class.getName());

	
	public MessageDelegator(Service service) {
		super(service);
	}


	public Message sendMessage(Message message, String userId) {
		new NotificationDelegator(this).broadcast(message, account.getFullId());
		return message;
	}

    public Message createMessage(Message message) {
        message.setDate(System.currentTimeMillis());
        return MessageManager.createMessage(message);
    }

    public MessageList getMessagesForThread(long threadId){
        return MessageManager.getMessagesByThreadId(threadId);
    }
	

}
