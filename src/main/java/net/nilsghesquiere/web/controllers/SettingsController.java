package net.nilsghesquiere.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.nilsghesquiere.entities.ClientSettings;
import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.ClientSettingsService;
import net.nilsghesquiere.service.web.InfernalSettingsService;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.web.annotations.ViewController;
import net.nilsghesquiere.web.dto.ClientSettingsDTO;
import net.nilsghesquiere.web.dto.InfernalSettingsDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	private static final String CLIENTS_VIEW = "settings/clients";
	private static final String CLIENTSETTINGS_VIEW = "settings/clientsettings";
	private static final String INFERNALSETTINGS_VIEW = "settings/infernalsettings";
	//TODO:DELETE
	private static final String INFERNAL_VIEW = "settings/infernal";
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@Autowired InfernalSettingsService infernalSettingsService;
	
	@Autowired ClientSettingsService clientSettingsService;

	@RequestMapping(value = "/clients", method = RequestMethod.GET)
	public ModelAndView viewClients(HttpServletRequest request){
		return new ModelAndView(CLIENTS_VIEW);
	}
	
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
	
	@RequestMapping(value = "/infernal", method = RequestMethod.GET)
	public ModelAndView viewInfernal(HttpServletRequest request){
		InfernalSettings settings = infernalSettingsService.getByUserId(authenticationFacade.getAuthenticatedUser().getId());
		InfernalSettingsDTO dto = new InfernalSettingsDTO(settings);
		String result = (String) request.getSession().getAttribute("resultM");
		if (result != null && !result.isEmpty()){
			request.getSession().removeAttribute("resultM");
			if(result.toLowerCase().contains("success")){
				return new ModelAndView(INFERNAL_VIEW).addObject("settings",dto).addObject("successM", result);
			}
			return new ModelAndView(INFERNAL_VIEW).addObject("settings",dto).addObject("resultM", result);
		} else {
			return new ModelAndView(INFERNAL_VIEW).addObject("settings",dto);
		}
	}
	
	@RequestMapping(value = "/infernal", method = RequestMethod.POST)
	public String editInfernalSettings(@ModelAttribute("settings") @Valid InfernalSettingsDTO settingsDTO,BindingResult bindingResult, Model model, HttpServletRequest request) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("failM", "Failure updating InfernalBot settings");
			return INFERNAL_VIEW;
		}
		InfernalSettings oldSettings = infernalSettingsService.getByUserId(authenticationFacade.getAuthenticatedUser().getId());
		InfernalSettings newSettings = new InfernalSettings(settingsDTO);
		newSettings.setUser(oldSettings.getUser());
		newSettings.setSets(oldSettings.getSets());
		newSettings.setId(oldSettings.getId());
		newSettings.setReplaceConfig(oldSettings.getReplaceConfig());
		newSettings.setLolHeight(oldSettings.getLolHeight());
		newSettings.setLolWidth(oldSettings.getLolWidth());
		infernalSettingsService.update(newSettings);
		request.getSession().setAttribute("resultM","Succesfully changed InfernalBot settings!");
		return "redirect:/settings/infernal";
	}

}