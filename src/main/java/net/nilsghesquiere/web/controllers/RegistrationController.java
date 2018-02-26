package net.nilsghesquiere.web.controllers;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.VerificationToken;
import net.nilsghesquiere.registration.OnRegistrationCompleteEvent;
import net.nilsghesquiere.service.web.IUserService;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.web.dto.UserDTO;
import net.nilsghesquiere.web.error.EmailExistsException;
import net.nilsghesquiere.web.util.GenericResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/register")
public class RegistrationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);
	private static final String REGISTER_VIEW = "register";
	private static final String REGISTERED_VIEW = "registered";
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private JavaMailSender mailSender;
	
	// Registration form
	@RequestMapping(method = RequestMethod.GET)
	public String showRegistrationForm(WebRequest request, Model model) {
		LOGGER.info("showRegistrationForm");
		UserDTO userDTO = new UserDTO();
		model.addAttribute("user", userDTO);
		return REGISTER_VIEW;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult result, WebRequest request, Errors errors) {

		if (result.hasErrors()) {
			 return new ModelAndView(REGISTER_VIEW, "user", userDTO);
		}
		
		User registered = createUserAccount(userDTO,result);
		if (registered == null) {
			result.rejectValue("email", "", "Email already in use");
		}
		if(!userDTO.getPassword().equals(userDTO.getMatchingPassword())){
			result.rejectValue("password", "", "Passwords don't match");
		}
		
		try{
			String appUrl = request.getContextPath();
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, appUrl));
		}catch (Exception me) {
			//TODO thuis testen
			LOGGER.info(me.getMessage());
			return new ModelAndView("emailError", "user", userDTO);
		}
		return new ModelAndView(REGISTERED_VIEW, "user", userDTO);
	}
	
	@RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
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

	@RequestMapping(value = "/user/resendRegistrationToken", method = RequestMethod.GET)
	@ResponseBody
	public GenericResponse resendRegistrationToken(HttpServletRequest request, @RequestParam("token") String existingToken) {
		VerificationToken newToken = userService.generateNewVerificationToken(existingToken);

		User user = userService.getUser(newToken.getToken());
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		SimpleMailMessage email = constructResendVerificationTokenEmail(appUrl, newToken, user);
		mailSender.send(email);
	 
		return new GenericResponse("Resend verificationmail");
	}

	private User createUserAccount(UserDTO userDTO, BindingResult result) {
		User registered = null;
		try {
			registered = userService.registerNewUserAccount(userDTO);
		}catch (EmailExistsException e){
			return null;
		}
		return registered;
	}

	private SimpleMailMessage constructResendVerificationTokenEmail(String contextPath, VerificationToken newToken, User user) {
		String confirmationUrl = contextPath + "/regitrationConfirm.html?token=" + newToken.getToken();
		String message = "New verification token: ";
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject("Resend Registration Token");
		email.setText(message + " rn" + confirmationUrl);
		//email.setFrom(env.getProperty("support.email"));
		email.setFrom("ghesquiere.nils@gmail.com");
		email.setTo(user.getEmail());
		return email;
	}
}