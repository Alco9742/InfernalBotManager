package net.nilsghesquiere.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.service.web.InfernalSettingsService;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.web.annotations.ViewController;
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
@RequestMapping("/infernalsettings")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
public class InfernalSettingsController {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(InfernalSettingsController.class);
	private static final String VIEW = "infernalsettings/main";
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@Autowired InfernalSettingsService infernalSettingsService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView view(HttpServletRequest request){
		InfernalSettings settings = infernalSettingsService.getByUserId(authenticationFacade.getAuthenticatedUser().getId());
		InfernalSettingsDTO dto = new InfernalSettingsDTO(settings);
		String result = (String) request.getSession().getAttribute("resultM");
		if (result != null && !result.isEmpty()){
			request.getSession().removeAttribute("resultM");
			if(result.toLowerCase().contains("success")){
				return new ModelAndView(VIEW).addObject("settings",dto).addObject("successM", result);
			}
			return new ModelAndView(VIEW).addObject("settings",dto).addObject("resultM", result);
		} else {
			return new ModelAndView(VIEW).addObject("settings",dto);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String edit(@ModelAttribute("settings") @Valid InfernalSettingsDTO settingsDTO,BindingResult bindingResult, Model model, HttpServletRequest request) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("failM", "Failure updating InfernalBot settings");
			return VIEW;
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
		return "redirect:/infernalsettings";
	}

}