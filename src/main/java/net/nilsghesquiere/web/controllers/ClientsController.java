package net.nilsghesquiere.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nilsghesquiere.entities.ClientSettings;
import net.nilsghesquiere.entities.InfernalSettings;
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
@RequestMapping("/clients")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
public class ClientsController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientsController.class);
	private static final String LIST_VIEW = "clients/list";
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@RequestMapping(method = RequestMethod.GET)
	ModelAndView list() {
		Map<Long,String> clientSettingsMap = new HashMap<>();
		Map<Long,String> infernalSettingsMap = new HashMap<>();
		User currentUser = authenticationFacade.getAuthenticatedUser();
		List<ClientSettings> clientSettingsList = currentUser.getClientSettingsList();
		List<InfernalSettings> infernalSettingsList = currentUser.getInfernalSettingsList();
		for(ClientSettings clientSettings : clientSettingsList){
			clientSettingsMap.put(clientSettings.getId(), clientSettings.getName());
		}
		for(InfernalSettings infernalSettings : infernalSettingsList){
			infernalSettingsMap.put(infernalSettings.getId(), infernalSettings.getSets());
		}
		LOGGER.info("Loading Clients page for user [" + currentUser.getEmail() + "].");
		return new ModelAndView(LIST_VIEW).addObject("currentUser", currentUser).addObject("clientSettingsMap", clientSettingsMap).addObject("infernalSettingsMap", infernalSettingsMap);
		}
}