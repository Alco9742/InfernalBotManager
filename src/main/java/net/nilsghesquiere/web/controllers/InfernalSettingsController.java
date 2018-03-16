package net.nilsghesquiere.web.controllers;

import java.util.Calendar;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.VerificationToken;
import net.nilsghesquiere.registration.OnRegistrationCompleteEvent;
import net.nilsghesquiere.security.IUserSecurityService;
import net.nilsghesquiere.service.web.InfernalSettingsService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.web.dto.InfernalSettingsDTO;
import net.nilsghesquiere.web.dto.PasswordDTO;
import net.nilsghesquiere.web.dto.UserDTO;
import net.nilsghesquiere.web.error.EmailExistsException;
import net.nilsghesquiere.web.error.EmailNotFoundException;
import net.nilsghesquiere.web.util.GenericResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InfernalSettingsController {
	private static final Logger LOGGER = LoggerFactory.getLogger(InfernalSettingsController.class);
	private static final String VIEW = "infernalsettings/main";
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@Autowired InfernalSettingsService infernalSettingsService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView view(){
		InfernalSettings settings = infernalSettingsService.getByUserId(authenticationFacade.getAuthenticatedUser().getId());
		InfernalSettingsDTO dto = new InfernalSettingsDTO(settings);
		return new ModelAndView().addObject("settings",dto);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String edit(@ModelAttribute("settings") @Valid InfernalSettingsDTO settingsDTO, HttpServletRequest request) {
		InfernalSettings oldSettings = infernalSettingsService.getByUserId(authenticationFacade.getAuthenticatedUser().getId());
		InfernalSettings newSettings = new InfernalSettings(settingsDTO);
		newSettings.setUser(oldSettings.getUser());
		newSettings.setSets(oldSettings.getSets());
		newSettings.setId(oldSettings.getId());
		newSettings.setReplaceConfig(oldSettings.getReplaceConfig());
		newSettings.setLolHeight(oldSettings.getLolHeight());
		newSettings.setLolWidth(oldSettings.getLolWidth());
		infernalSettingsService.update(newSettings);
		return "redirect:/register/success";
	}

}