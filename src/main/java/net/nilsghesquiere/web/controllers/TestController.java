package net.nilsghesquiere.web.controllers;

import javax.servlet.http.HttpServletRequest;

import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.web.annotations.ViewController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@ViewController
@RequestMapping("/test")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class TestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
	private static final String TEST_VIEW = "test/panel";
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@Autowired
	private HttpServletRequest request;
	
	//PANEL
	@RequestMapping(method = RequestMethod.GET)
	String test() {
		LOGGER.info("Loading tests");
		LOGGER.info("request.isUserInRole('ADMIN'): " + request.isUserInRole("ADMIN"));
		LOGGER.info("request.isUserInRole('ROLE_ADMIN'): " + request.isUserInRole("ROLE_ADMIN"));
		LOGGER.info("request.isUserInRole('USER'): " + request.isUserInRole("USER"));
		LOGGER.info("request.isUserInRole('ROLE_USER'): " + request.isUserInRole("ROLE_USER"));
		LOGGER.info("request.isUserInRole('ADMIN'): " + request.isUserInRole("ADMIN"));
		return TEST_VIEW;
	}
	

	
}