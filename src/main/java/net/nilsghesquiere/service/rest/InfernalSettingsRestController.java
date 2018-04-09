package net.nilsghesquiere.service.rest;

import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.InfernalSettingsService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.util.wrappers.InfernalSettingsWrapper;
import net.nilsghesquiere.web.error.UserIsNotOwnerOfResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/infernalsettings")
public class InfernalSettingsRestController {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(InfernalSettingsRestController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private InfernalSettingsService infernalSettingsService;
	
	@Autowired
	private AuthenticationFacade authenticationFacade;	
	
	@RequestMapping(path = "/user/{userid}", method = RequestMethod.GET)
	public ResponseEntity<InfernalSettingsWrapper> findInfernalSettingsByUserId(@PathVariable Long userid) {
		//VARS
		InfernalSettingsWrapper wrapper;
		String error = "";
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//PROCESSING
		InfernalSettings infernalSettings = infernalSettingsService.getByUserId(userid);
		
		//RESPONSE
		wrapper = new InfernalSettingsWrapper();
		wrapper.add("data",infernalSettings);
		wrapper.setError(error);
		
		//RETURN
		return new ResponseEntity<InfernalSettingsWrapper>(wrapper, HttpStatus.OK);
	}
	
}
