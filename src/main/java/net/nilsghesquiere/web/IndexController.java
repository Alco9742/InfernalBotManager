package net.nilsghesquiere.web;

import java.util.Optional;

import javax.validation.Valid;

import net.nilsghesquiere.dto.UserDTO;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.exceptions.EmailExistsException;
import net.nilsghesquiere.exceptions.UsernameExistsException;
import net.nilsghesquiere.facades.AuthenticationFacade;
import net.nilsghesquiere.services.IUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class IndexController {
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	private static final String VIEW = "index";
	private static final String REGISTER_VIEW = "register";
	private static final String REGISTERED_VIEW = "registered";
	
	private final AuthenticationFacade authenticationFacade;
	private final IUserService userService;
	
	@Autowired
	public IndexController(AuthenticationFacade authenticationFacade, IUserService userService) {
		this.authenticationFacade = authenticationFacade;
		this.userService = userService;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() {
		Optional<User> currentUser = authenticationFacade.getOptionalAuthenticatedUser();
		if (currentUser.isPresent()){
			LOGGER.info("Index page requested by " + currentUser.get().getUsername());
			return new ModelAndView(VIEW).addObject("currentUser", currentUser);
		} else {
			LOGGER.info("Index page requested by anonymous user");
			return new ModelAndView(VIEW);
		}
	}	
	
	// Login form
	@RequestMapping("/login.html")
	public String login() {
		return "login";
	}

	// Login form with error
	@RequestMapping("/login-error.html")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "login";
	}
	
	// Registration form
	@RequestMapping(value = "/register.html", method = RequestMethod.GET)
	public String showRegistrationForm(WebRequest request, Model model) {
		UserDTO userDTO = new UserDTO();
		model.addAttribute("user", userDTO);
		return REGISTER_VIEW;
	}
	
	@RequestMapping(value = "register.html", method = RequestMethod.POST)
	public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult result, WebRequest request, Errors errors) {
		User registered = new User();
		if (!result.hasErrors()) {
			registered = createUserAccount(userDTO, result);
		}
		if (registered == null) {
			if(userService.emailExist(userDTO.getEmail())){
				result.rejectValue("email", "message.regError");
			} else {
				result.rejectValue("username", "message.regError");
			}
		}
		if (result.hasErrors()){
			return new ModelAndView(REGISTER_VIEW, "user", userDTO);
		} else {
			return new ModelAndView(REGISTERED_VIEW, "user", userDTO);
		}

	}
	private User createUserAccount(UserDTO userDTO, BindingResult result) {
		User registered = null;
		try {
			registered = userService.registerNewUserAccount(userDTO);
		}catch (EmailExistsException|UsernameExistsException e) {
			return null;
		}
		return registered;
	}
}