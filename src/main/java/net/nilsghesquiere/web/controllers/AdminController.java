package net.nilsghesquiere.web.controllers;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.VerificationToken;
import net.nilsghesquiere.registration.OnRegistrationCompleteEvent;
import net.nilsghesquiere.security.IUserSecurityService;
import net.nilsghesquiere.service.web.GlobalVariableService;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
	private static final String PANEL_VIEW = "admin/panel";
	private static final String GLOBAL_VARS_VIEW = "admin/globalvars";
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private IUserSecurityService userSecurityService;
	
	@Autowired
	private GlobalVariableService globalVariableService;
	
	//PANEL
	@RequestMapping(method = RequestMethod.GET)
	ModelAndView panel() {
		Optional<User> currentUser = authenticationFacade.getOptionalAuthenticatedUser();
		LOGGER.info("Loading admin panel for " + currentUser.get().getEmail());
		return new ModelAndView(PANEL_VIEW).addObject("currentUser",currentUser.get());
	}
	
	@RequestMapping(value = "/globalVars", method = RequestMethod.GET)
	ModelAndView globalVars() {
		Optional<User> currentUser = authenticationFacade.getOptionalAuthenticatedUser();
		LOGGER.info("Loading Accounts list for user [" + currentUser.get().getEmail() + "].");
		return new ModelAndView(GLOBAL_VARS_VIEW).addObject("currentUser", currentUser.get());
		}
}