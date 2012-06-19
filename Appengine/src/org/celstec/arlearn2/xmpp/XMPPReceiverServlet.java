package org.celstec.arlearn2.xmpp;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.PresenceShow;
import com.google.appengine.api.xmpp.PresenceType;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@Deprecated
public class XMPPReceiverServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
          XMPPService xmpp = XMPPServiceFactory.getXMPPService();
          
          Message message = xmpp.parseMessage(req);

          JID fromJid = message.getFromJid();
          String body = message.getBody();
          JID toJid = new JID("stefaan.ternier@gmail.com/arlearn");
          //XMPPService xmppService = XMPPServiceFactory.getXMPPService();
          xmpp.sendPresence( toJid, PresenceType.AVAILABLE, PresenceShow.NONE, "Dit is mijn status");
          
          Message msg = new MessageBuilder()
          .asXml(true)
          .withRecipientJids( toJid)
          .withBody("Dit is een bericht voor stefaan op android")
          .build();
          
          xmpp.sendMessage(msg);
      }

}
