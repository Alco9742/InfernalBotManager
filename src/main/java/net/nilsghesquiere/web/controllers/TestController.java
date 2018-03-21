package net.nilsghesquiere.web.controllers;

import java.util.Collection;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.LolAccountService;
import net.nilsghesquiere.util.facades.AuthenticationFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/test")
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
		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)    SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		LOGGER.info("AUTHORITIES:");
		for(SimpleGrantedAuthority authority : authorities){
			LOGGER.info(authority.toString());
		}
		return TEST_VIEW;
	}
	

	
}