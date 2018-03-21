package net.nilsghesquiere.web.controllers;

import java.util.Calendar;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.VerificationToken;
import net.nilsghesquiere.registration.OnRegistrationCompleteEvent;
import net.nilsghesquiere.security.IUserSecurityService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.web.dto.PasswordDTO;
import net.nilsghesquiere.web.dto.UserDTO;
import net.nilsghesquiere.web.error.EmailExistsException;
import net.nilsghesquiere.web.error.EmailNotFoundException;
import net.nilsghesquiere.web.util.GenericResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class RegistrationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private IUserSecurityService userSecurityService;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public String registerForm(){
		return "registration";
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public GenericResponse registerUserAccount(@ModelAttribute("user") @Valid UserDTO userDTO, HttpServletRequest request) {
		LOGGER.debug("Registering user account with information: {}", userDTO);
		User registered = createUserAccount(userDTO);
		if (registered == null) {
			throw new EmailExistsException(userDTO.getEmail());
		}
		String url = getAppUrl(request);
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, url));
		return new GenericResponse("Success");
	}
	
	@RequestMapping(value = "/registered", method = RequestMethod.GET)
	public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token){
		VerificationToken verificationToken = userService.getVerificationToken(token);
		if (verificationToken == null) {
			String message = "Invalid authentication token";
			model.addAttribute("message", message);
			return "redirect:/baduser";
		}

		User user = verificationToken.getUser();
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			String messageValue = "Authentication token expired";
			model.addAttribute("message", messageValue);
			 model.addAttribute("expired", true);
			 model.addAttribute("token", token);
			return "redirect:/baduser";
		}
		
		user.setEnabled(true); 
		userService.saveRegisteredUser(user); 
		model.addAttribute("message","Your account has been verified");
		return "redirect:/login"; 
	}

	@RequestMapping(value = "/resendRegistrationToken", method = RequestMethod.GET)
	@ResponseBody
	public GenericResponse resendRegistrationToken(HttpServletRequest request, @RequestParam("token") String existingToken) {
		VerificationToken newToken = userService.generateNewVerificationToken(existingToken);

		User user = userService.getUser(newToken.getToken());
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		SimpleMailMessage email = constructResendVerificationTokenEmail(appUrl, newToken, user);
		mailSender.send(email);
	 
		return new GenericResponse("Resend verificationmail");
	}
	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse resetPassword(HttpServletRequest request, @RequestParam("email") String email) {
		User user = userService.findUserByEmail(email);
		if (user == null) {
			throw new EmailNotFoundException(email);
		}
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		mailSender.send(constructResetTokenEmail(getAppUrl(request), token, user));
		return new GenericResponse("Password reset");
	}
		
	@RequestMapping(value = "/user/changePassword", method = RequestMethod.GET)
	public String showChangePasswordPage(Model model, @RequestParam("id") long id, @RequestParam("token") String token) {
		String result = userSecurityService.validatePasswordResetToken(id, token);
		if (result != null) {
			model.addAttribute("message", "Authentication error");
			return "redirect:/login";
		}
		return "redirect:/updatePassword";
	}
	
	@RequestMapping(value = "/user/savePassword", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse savePassword( @Valid PasswordDTO passwordDTO) {
		User user = authenticationFacade.getAuthenticatedUser();
		userService.changeUserPassword(user, passwordDTO.getNewPassword());
		return new GenericResponse("Succesfully changed password");
	}
	
	@RequestMapping(value = "/user/updatePassword", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public GenericResponse changeUserPassword(@RequestParam("password") String password,@RequestParam("oldpassword") String oldPassword) {
		User user = authenticationFacade.getAuthenticatedUser();
		//TODO
		//if (!userService.checkIfValidOldPassword(user, oldPassword)) {
		//	throw new InvalidOldPasswordException();
		//}
		userService.changeUserPassword(user, password);
		return new GenericResponse("Succesfully updated password");
	}
	
	//PRIVATE METHODS
	
	private User createUserAccount(UserDTO userDTO) {
		User registered = null;
		try {
			registered = userService.registerNewUserAccount(userDTO);
		}catch (EmailExistsException e){
			return null;
		}
		return registered;
	}
	
	private SimpleMailMessage constructResendVerificationTokenEmail(String contextPath, VerificationToken newToken, User user) {
		String url = contextPath + "/registered?token=" + newToken.getToken();
		String message = "New verification token: ";
		return constructEmail("Reset Password", message + " \r\n" + url, user);
	}
	
	private SimpleMailMessage constructResetTokenEmail(String contextPath, String token, User user) {
		String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
		String message ="Reset Password: ";
		return constructEmail("Reset Password", message + " \r\n" + url, user);
	}
	
	private SimpleMailMessage constructEmail(String subject, String body, User user) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		//email.setFrom(env.getProperty("support.email"));
		email.setFrom("ghesquiere.nils@gmail.com");
		return email;
	}
	
	private String getAppUrl(HttpServletRequest request) {
			return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}