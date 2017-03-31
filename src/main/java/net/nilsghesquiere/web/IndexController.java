package net.nilsghesquiere.web;

import java.util.Optional;

import net.nilsghesquiere.entities.AppUser;
import net.nilsghesquiere.facades.AuthenticationFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class IndexController {
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	private static final String VIEW = "index";
	
	private final AuthenticationFacade authenticationFacade;
	
	@Autowired
	public IndexController(AuthenticationFacade authenticationFacade) {
		this.authenticationFacade = authenticationFacade;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	ModelAndView index() {
		Optional<AppUser> currentUser = authenticationFacade.getOptionalAuthenticatedUser();
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
	
}