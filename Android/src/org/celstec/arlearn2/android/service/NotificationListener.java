package org.celstec.arlearn2.android.service;

import java.util.Iterator;

import org.celstec.arlearn2.android.db.notificationbeans.BeanCreator;
import org.celstec.arlearn2.android.db.notificationbeans.NotificationBean;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.StringUtils;

import android.util.Log;
import android.widget.Toast;

public class NotificationListener implements PacketListener{

	private NotificationService service;
	
	public NotificationListener(NotificationService service) {
		this.service = service;
	}

	
	public void processPacket(Packet packet) {
        Message message = (Message) packet;
        if (message.getBody() != null) {
            String fromName = StringUtils.parseBareAddress(message.getFrom());
        } else {
//            Log.i("xmpp","xml"+ message.toXML());
            PacketExtension pe = message.getExtension("message", "http://celstec.org/xsd/arlearn2"); 
            Iterator<PacketExtension> it = message.getExtensions().iterator();
            while (it.hasNext()) {
            	DefaultPacketExtension packetExtension = (DefaultPacketExtension) it.next();
            	NotificationBean bean = (new BeanCreator(packetExtension)).createBean();
            	service.processXmppNotification(bean);
			}
        }
    }

}
