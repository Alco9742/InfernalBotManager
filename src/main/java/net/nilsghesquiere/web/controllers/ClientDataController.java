package net.nilsghesquiere.web.controllers;

import java.util.Optional;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.util.facades.AuthenticationFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/clients")
@PreAuthorize("hasAnyAuthority('appadmin','basicuser')")
public class ClientDataController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientDataController.class);
	private static final String LIST_VIEW = "clients/list";
	
	private final AuthenticationFacade authenticationFacade;

	@Autowired
	public ClientDataController(AuthenticationFacade authenticationFacade) {
		this.authenticationFacade = authenticationFacade;
	}

	@RequestMapping(method = RequestMethod.GET)
	ModelAndView list() {
		Optional<User> currentUser = authenticationFacade.getOptionalAuthenticatedUser();
		LOGGER.info("Loading Clients list for user [" + currentUser.get().getEmail() + "].");
		return new ModelAndView(LIST_VIEW).addObject("currentUser", currentUser.get());
		}
}