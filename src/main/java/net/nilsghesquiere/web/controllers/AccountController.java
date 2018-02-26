package net.nilsghesquiere.web.controllers;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/accounts")
@PreAuthorize("hasAnyAuthority('appadmin','basicuser')")
public class AccountController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
	private static final String LIST_VIEW = "accounts/list";
	
	private final ILolAccountService lolAccountService;
	private final AuthenticationFacade authenticationFacade;

	@Autowired
	public AccountController(ILolAccountService lolAccountService, AuthenticationFacade authenticationFacade) {
		this.lolAccountService = lolAccountService;
		this.authenticationFacade = authenticationFacade;
	}

	@RequestMapping(method = RequestMethod.GET)
	ModelAndView list() {
		Optional<User> currentUser = authenticationFacade.getOptionalAuthenticatedUser();
		LOGGER.info("Loading Accounts list for user [" + currentUser.get().getEmail() + "].");
		return new ModelAndView(LIST_VIEW).addObject("currentUser", currentUser.get());
		}
}