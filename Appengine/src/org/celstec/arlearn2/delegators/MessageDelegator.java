package org.celstec.arlearn2.delegators;

import java.util.logging.Logger;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.run.Message;

public class MessageDelegator extends GoogleDelegator {
	private static final Logger logger = Logger.getLogger(MessageDelegator.class.getName());

	
	public MessageDelegator(Service service) {
		super(service);
	}


	public Message sendMessage(Message message, String userId) {
		new NotificationDelegator().broadcast(message, account.getFullId());
		return message;
	}
	
	

}
