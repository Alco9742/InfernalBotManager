package net.nilsghesquiere.registration;

import java.util.UUID;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent>{
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationListener.class);
	
	
	@Autowired
	private IUserService userService;
		
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event){
		this.confirmRegistration(event);
	}
	
	private void confirmRegistration(OnRegistrationCompleteEvent event){
		LOGGER.info("confirmRegistration");
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		userService.createVerificationTokenForUser(user,token);
		
		String recipientAdress = user.getEmail();
		String subject = "Registration Confirmation";
		String confirmationUrl = event.getAppUrl() + "/registered.html?token=" + token;
		String message = "Confirm your account: ";
		
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAdress);
		email.setSubject(subject);
		email.setText(message + " \r\n" + confirmationUrl);
		mailSender.send(email);
	}
}
