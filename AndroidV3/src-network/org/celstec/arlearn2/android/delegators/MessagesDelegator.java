package org.celstec.arlearn2.android.delegators;

import daoBase.DaoConfiguration;
import org.celstec.arlearn2.android.events.MessageEvent;
import org.celstec.arlearn2.beans.run.Message;
import org.celstec.arlearn2.beans.run.MessageList;
import org.celstec.arlearn2.client.ThreadsClient;
import org.celstec.dao.gen.MessageLocalObject;

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
public class MessagesDelegator extends AbstractDelegator {

    private static MessagesDelegator instance;


    private MessagesDelegator() {
        ARL.eventBus.register(this);
    }

    public static MessagesDelegator getInstance() {
        if (instance == null) {
            instance = new MessagesDelegator();
        }
        return instance;
    }

    public void syncMessages(Long threadId) {
        ARL.eventBus.post(new SyncMessages(threadId));
    }

    private void onEventAsync(SyncMessages syncMessages) {
        String token = returnTokenIfOnline();
        if (token != null) {
            MessageList ml = ThreadsClient.getThreadsClient().getMessages(token, syncMessages.getThreadId());
            if (ml.getError() ==null) {
                process(ml);
            }
        }

    }

    private void process(MessageList ml) {
        for (Message message: ml.getMessages()) {
            MessageLocalObject existingMessage = DaoConfiguration.getInstance().getMessageLocalObject().load(message.getMessageId());
            MessageLocalObject newMessage = toDaoLocalObject(message);
            if (existingMessage == null) {
                DaoConfiguration.getInstance().getMessageLocalObject().insertOrReplace(newMessage);
                ARL.eventBus.post(new MessageEvent(newMessage.getRunId(), newMessage.getThreadId()));
            }
        }
    }

    private MessageLocalObject toDaoLocalObject(Message message) {
        MessageLocalObject messageLocalObject = new MessageLocalObject();
        messageLocalObject.setId(message.getMessageId());
        messageLocalObject.setBody(message.getBody());
        messageLocalObject.setRunId(message.getRunId());
        messageLocalObject.setSubject(message.getSubject());
        messageLocalObject.setThreadId(message.getThreadId());
        messageLocalObject.setTime(message.getTimestamp());

        return messageLocalObject;
    }


    private class SyncMessages{
        long threadId;

        private SyncMessages(long threadId) {
            this.threadId = threadId;
        }

        public long getThreadId() {
            return threadId;
        }

        public void setThreadId(long threadId) {
            this.threadId = threadId;
        }
    }
}
