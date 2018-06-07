package net.nilsghesquiere.util.mailing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MailBuilders {
	private static final Logger LOGGER = LoggerFactory.getLogger(MailBuilders.class);
	
	public static SimpleMailMessage buildClientDisconnectedMail(String clientTag, String userEmail, long seconds){
		LOGGER.info("Client '" + clientTag + "' has been disconnected for more than 5 minutes, sending mail to '" + userEmail +"'");
		
		String recipientAdress = userEmail;
		String subject = "Client disconnected";
		String message = "Client '" + clientTag + "' has been disconnected for " + seconds + " seconds, please check your server. ";
		
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAdress);
		email.setSubject(subject);
		email.setText(message );
		return email;
	}
}
