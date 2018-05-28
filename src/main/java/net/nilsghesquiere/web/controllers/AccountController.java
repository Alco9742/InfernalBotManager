package net.nilsghesquiere.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nilsghesquiere.entities.ImportSettings;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.web.annotations.ViewController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ViewController
@RequestMapping("/accounts")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
public class AccountController{
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
	private static final String LIST_VIEW = "accounts/list";
	
	@Autowired
	private AuthenticationFacade authenticationFacade;

	@RequestMapping(method = RequestMethod.GET)
	ModelAndView list() {
		Map<Long,String> importSettingsMap = new HashMap<>();
		User currentUser = authenticationFacade.getAuthenticatedUser();
		List<ImportSettings> importSettingsList = currentUser.getImportSettingsList();
		for(ImportSettings importSettings : importSettingsList){
			importSettingsMap.put(importSettings.getId(), importSettings.getName());
		}
		Long activeImportSettings = currentUser.getUserSettings().getActiveImportSettings();
		LOGGER.info("Loading Accounts page for user [" + currentUser.getEmail() + "].");
		return new ModelAndView(LIST_VIEW).addObject("currentUser", currentUser).addObject("importSettingsMap",importSettingsMap).addObject("activeImportSettings",activeImportSettings);
	}
}