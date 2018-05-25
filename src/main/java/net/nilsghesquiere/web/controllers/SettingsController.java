package net.nilsghesquiere.web.controllers;

import java.util.List;

import net.nilsghesquiere.entities.ClientSettings;
import net.nilsghesquiere.entities.ImportSettings;
import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.UserSettings;
import net.nilsghesquiere.service.web.ClientSettingsService;
import net.nilsghesquiere.service.web.ImportSettingsService;
import net.nilsghesquiere.service.web.InfernalSettingsService;
import net.nilsghesquiere.service.web.UserSettingsService;
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
@RequestMapping("/settings")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
public class SettingsController {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SettingsController.class);
	private static final String CLIENTSETTINGS_VIEW = "settings/clientsettings";
	private static final String INFERNALSETTINGS_VIEW = "settings/infernalsettings";
	private static final String IMPORTSETTINGS_VIEW = "settings/importsettings";
	private static final String USERSETTINGS_VIEW = "settings/usersettings";
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@Autowired 
	private InfernalSettingsService infernalSettingsService;
	
	@Autowired 
	private ClientSettingsService clientSettingsService;
	
	@Autowired 
	private ImportSettingsService importSettingsService;
	
	@Autowired 
	private UserSettingsService userSettingsService;

	
	@RequestMapping(value = "/clientsettings", method = RequestMethod.GET)
	public ModelAndView viewClientSettings(){
		User currentUser = authenticationFacade.getAuthenticatedUser();
		List<ClientSettings> clientSettingsList = clientSettingsService.findByUser(currentUser);
		return new ModelAndView(CLIENTSETTINGS_VIEW).addObject("clientSettingsList",clientSettingsList).addObject("currentUser", currentUser);
	}
	
	@RequestMapping(value = "/infernalsettings", method = RequestMethod.GET)
	public ModelAndView viewInfernalSettings(){
		User currentUser = authenticationFacade.getAuthenticatedUser();
		List<InfernalSettings> infernalSettingsList = infernalSettingsService.findByUser(currentUser);
		return new ModelAndView(INFERNALSETTINGS_VIEW).addObject("infernalSettingsList",infernalSettingsList).addObject("currentUser", currentUser);
	}
	
	@RequestMapping(value = "/importsettings", method = RequestMethod.GET)
	public ModelAndView viewImportSettings(){
		User currentUser = authenticationFacade.getAuthenticatedUser();
		List<ImportSettings> importSettingsList = importSettingsService.findByUser(currentUser);
		return new ModelAndView(IMPORTSETTINGS_VIEW).addObject("importSettingsList",importSettingsList).addObject("currentUser", currentUser);
	}
	
	@RequestMapping(value = "/usersettings", method = RequestMethod.GET)
	public ModelAndView viewUserSettings(){
		User currentUser = authenticationFacade.getAuthenticatedUser();
		UserSettings userSettings = userSettingsService.getByUser(currentUser);
		List<ImportSettings> importSettingsList = importSettingsService.findByUser(currentUser);
		return new ModelAndView(USERSETTINGS_VIEW).addObject("userSettings",userSettings).addObject("importSettingsList",importSettingsList).addObject("currentUser", currentUser);
	}
	
}