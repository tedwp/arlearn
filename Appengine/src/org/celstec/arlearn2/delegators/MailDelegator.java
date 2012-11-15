package org.celstec.arlearn2.delegators;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.gdata.util.AuthenticationException;

public class MailDelegator extends GoogleDelegator {

	private static final Logger logger = Logger.getLogger(ActionDelegator.class.getName());

	public MailDelegator(String authtoken) throws AuthenticationException {
		super(authtoken);
	}

	public MailDelegator(GoogleDelegator gd) {
		super(gd);
	}

	public void sendInstructionMail(String from, String fromName, String toMail) {
		if (!toMail.contains("@")) toMail +="@gmail.com";
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = "<html><body>";
		msgBody += "Hi,<br>";
		msgBody += "<p>";
		msgBody += "Welcome to ARLearn. We hope you'll find this a useful tool.";
		msgBody += "</p>";
		msgBody += "<p>";
		msgBody += "ARLearn content is organized in games and runs. Both can be created and managed with the <a href=\"http://streetlearn.appspot.com/Authoring.html\">ARLearn authoring tool</a>.";
		msgBody += "</p>";
		msgBody += "<p>";
		msgBody += "For more information on using ARLearn, visit our <a href=\"http://portal.ou.nl/web/topic-mobile-learning/home/-/wiki/Main/Get%20Started\">get started page</a>.";
		msgBody += "</p>";
		msgBody += "<p>";
		msgBody += "Have fun<br>"; 
		msgBody += "The ARLearn team.";
		msgBody += "</p>";
		msgBody += "</body></html>";

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from, fromName));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
			msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(from));
			msg.setSubject("Instructions for using ARLearn");
			
	        final MimeBodyPart htmlPart = new MimeBodyPart();
	        htmlPart.setContent(msgBody, "text/html");
	        final Multipart mp = new MimeMultipart();
	        mp.addBodyPart(htmlPart);
	        
			msg.setContent(mp);
			Transport.send(msg);

		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} 
	}

}
