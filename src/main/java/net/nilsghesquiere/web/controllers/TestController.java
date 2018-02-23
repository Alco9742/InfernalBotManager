package net.nilsghesquiere.web.controllers;

import java.util.Optional;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.ILolAccountService;
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
@RequestMapping("/test")
@PreAuthorize("hasAuthority('appadmin')")
public class TestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
	private static final String PANEL_VIEW = "test/panel";
	private static final String RATES_VIEW = "test/rates";
	private static final String SUMMONER_VIEW = "test/summoner";
	
	private final AuthenticationFacade authenticationFacade;
	private final ILolAccountService lolAccountService;

	@Autowired
	public TestController(AuthenticationFacade authenticationFacade, ILolAccountService lolAccountService) {
		this.lolAccountService = lolAccountService;
		this.authenticationFacade = authenticationFacade;
	}
	
	//PANEL
	@RequestMapping(method = RequestMethod.GET)
	ModelAndView panel() {
		Optional<User> currentUser = authenticationFacade.getOptionalAuthenticatedUser();
		LOGGER.info("Loading test panel for admin " + currentUser.get().getUsername());
		return new ModelAndView(PANEL_VIEW).addObject("currentUser",currentUser.get());
	}
	
	@RequestMapping(path="rates", method = RequestMethod.GET)
	ModelAndView rates() {
		return new ModelAndView(RATES_VIEW);
	}

	@RequestMapping(path="summoner", method = RequestMethod.GET)
	ModelAndView summoner() {
		return new ModelAndView(SUMMONER_VIEW);
	}
	
}