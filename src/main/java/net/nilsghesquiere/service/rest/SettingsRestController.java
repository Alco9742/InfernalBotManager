package net.nilsghesquiere.service.rest;

import javax.validation.Valid;

import net.nilsghesquiere.entities.ClientSettings;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.ClientSettingsService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.util.wrappers.ClientSettingsWrapper;
import net.nilsghesquiere.web.dto.ClientSettingsDTO;
import net.nilsghesquiere.web.error.SettingsAlreadyExistException;
import net.nilsghesquiere.web.error.SettingsNotFoundException;
import net.nilsghesquiere.web.error.UserIsNotOwnerOfResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
public class SettingsRestController {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SettingsRestController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private ClientSettingsService clientSettingsService;
	
	@Autowired
	private AuthenticationFacade authenticationFacade;	
	
	//clientSettings
	@RequestMapping(path = "/user/{userid}/clientsettings/{clientsettingsid}", method = RequestMethod.GET)
	public ResponseEntity<ClientSettingsWrapper> findInfernalSettingsByUserId(@PathVariable Long userid, @PathVariable Long clientsettingsid) {
		//VARS
		ClientSettingsWrapper wrapper;
		String error = "";
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//PROCESSING
		ClientSettings clientSettings = clientSettingsService.read(clientsettingsid);
		
		//NULL CHECK
		if(clientSettings == null){
			throw new SettingsNotFoundException(clientsettingsid);
		}
		
		//USER CLIENTSETTINGS CHECK
		if(!clientSettings.getUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//RESPONSE
		wrapper = new ClientSettingsWrapper();
		wrapper.add("data",clientSettings);
		wrapper.setError(error);
		
		//RETURN
		return new ResponseEntity<ClientSettingsWrapper>(wrapper, HttpStatus.OK);
	}

	@RequestMapping(value = "/user/{userid}/clientsettings", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResponseEntity<ClientSettingsWrapper> newClientSettings(@PathVariable Long userid, @ModelAttribute("clientsettings") @Valid ClientSettingsDTO dto) throws BindException {
		//VARS
		ClientSettingsWrapper wrapper;
		String error = "";
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//Check if name exists already for that user
		ClientSettings alreadyExistingSettings = clientSettingsService.findByUserIdAndName(user.getId(), dto.getName());
		if(alreadyExistingSettings != null){
			throw new SettingsAlreadyExistException(dto.getName());
		}
		//build clientSettings from the dto
		ClientSettings newClientSettings = new ClientSettings(dto);
		
		//set user
		newClientSettings.setUser(user);
		
		//create the settings in the database
		ClientSettings createdClientSettings = clientSettingsService.create(newClientSettings);
		
		//RESPONSE
		wrapper = new ClientSettingsWrapper();
		wrapper.add("data",createdClientSettings);
		wrapper.setError(error);
		
		return new ResponseEntity<ClientSettingsWrapper>(wrapper, HttpStatus.OK);
	}
}
