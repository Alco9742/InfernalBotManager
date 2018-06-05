package net.nilsghesquiere.service.rest;

import javax.validation.Valid;

import net.nilsghesquiere.entities.ClientSettings;
import net.nilsghesquiere.entities.ImportSettings;
import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.UserSettings;
import net.nilsghesquiere.service.web.ClientSettingsService;
import net.nilsghesquiere.service.web.ImportSettingsService;
import net.nilsghesquiere.service.web.InfernalSettingsService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.service.web.UserSettingsService;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.util.wrappers.ClientSettingsWrapper;
import net.nilsghesquiere.util.wrappers.ImportSettingsWrapper;
import net.nilsghesquiere.util.wrappers.InfernalSettingsWrapper;
import net.nilsghesquiere.util.wrappers.UserSettingsWrapper;
import net.nilsghesquiere.web.dto.ClientSettingsDTO;
import net.nilsghesquiere.web.dto.ImportSettingsDTO;
import net.nilsghesquiere.web.dto.InfernalSettingsDTO;
import net.nilsghesquiere.web.dto.UserSettingsDTO;
import net.nilsghesquiere.web.error.ClientSettingsInUseException;
import net.nilsghesquiere.web.error.ImportSettingsInUseException;
import net.nilsghesquiere.web.error.InfernalSettingsInUseException;
import net.nilsghesquiere.web.error.SettingsAlreadyExistException;
import net.nilsghesquiere.web.error.SettingsNotFoundException;
import net.nilsghesquiere.web.error.UserIsNotOwnerOfResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
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
	private InfernalSettingsService infernalSettingsService;
	
	@Autowired
	private ImportSettingsService importSettingsService;
	
	@Autowired
	private UserSettingsService userSettingsService;
	
	@Autowired
	private AuthenticationFacade authenticationFacade;	
	
	//clientSettings
	@RequestMapping(path = "/user/{userid}/clientsettings/{clientsettingsid}", method = RequestMethod.GET)
	public ResponseEntity<ClientSettingsWrapper> findClientsSettingsById(@PathVariable Long userid, @PathVariable Long clientsettingsid) {
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
	
	@RequestMapping(value = "/user/{userid}/clientsettings/{clientsettingsid}", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody
	public ResponseEntity<ClientSettingsWrapper> deleteClientSettingsById(@PathVariable Long userid, @PathVariable Long clientsettingsid){
		//VARS
		ClientSettingsWrapper wrapper;
		String error = "";
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//ID should not be null or 0 cause its a delete
		if(clientsettingsid == null || clientsettingsid == 0L){
			throw new SettingsNotFoundException(clientsettingsid);
		}
		
		//search settings by ID
		ClientSettings clientSettingsById = clientSettingsService.read(clientsettingsid);
		
		//settings with that ID should exist cause its a delete
		if (clientSettingsById == null){
			throw new SettingsNotFoundException(clientsettingsid);
		}
		
		//USER CHECK 2 
		if(!clientSettingsById.getUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//delete the settings
		try{
			clientSettingsService.delete(clientSettingsById);
		} catch (DataIntegrityViolationException e){
			throw new ClientSettingsInUseException(clientSettingsById.getName());
		}
		
		//RESPONSE
		wrapper = new ClientSettingsWrapper();
		wrapper.add("data",clientSettingsById);
		wrapper.setError(error);
		
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
		
		//If for some reason the ID of the dto is not equal to 0, set it to 0 (this would mean people are trying to do funny business)
		if(dto.getId() != 0){
			dto.setId(0L);
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
	
	@RequestMapping(value = "/user/{userid}/clientsettings", method = RequestMethod.PUT, produces = "application/json")
	@ResponseBody
	public ResponseEntity<ClientSettingsWrapper> updateClientSettings(@PathVariable Long userid, @ModelAttribute("clientsettings") @Valid ClientSettingsDTO dto) throws BindException {
		//VARS
		ClientSettingsWrapper wrapper;
		String error = "";
			
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//search settings by ID
		ClientSettings clientSettingsById = clientSettingsService.read(dto.getId());
		
		//settings with that ID should exist cause its an update
		if (clientSettingsById == null){
			throw new SettingsNotFoundException(dto.getId());
		}
		
		//USER CHECK 2 
		if(!clientSettingsById.getUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//search settings by Name
		ClientSettings clientSettingsByName = clientSettingsService.findByUserIdAndName(user.getId(), dto.getName());
		
		//if clientSettingsByName null = OK; name got changed and doesn't exist yet
		if(clientSettingsByName != null){
			//check if the settings by name are the same settings, if not we can't update (name needs to be unique for user)
			if(!clientSettingsByName.equals(clientSettingsById)){
				throw new SettingsAlreadyExistException(dto.getName());
			}
		}
		
		//update existing settings from the dto
		clientSettingsById.updateFromDTO(dto);
		
		//update the settigns in the database
		ClientSettings updatedClientSettings = clientSettingsService.update(clientSettingsById);
		
		//RESPONSE
		wrapper = new ClientSettingsWrapper();
		wrapper.add("data",updatedClientSettings);
		wrapper.setError(error);
		
		return new ResponseEntity<ClientSettingsWrapper>(wrapper, HttpStatus.OK);
	}
	
	//infernalSettings
	//TODO check if the defaultsettings for accounts get taken from the first in the list or what
	@RequestMapping(path = "/user/{userid}/infernalsettings/{infernalsettingsid}", method = RequestMethod.GET)
	public ResponseEntity<InfernalSettingsWrapper> findInfernalSettingsById(@PathVariable Long userid, @PathVariable Long infernalsettingsid) {
		//VARS
		InfernalSettingsWrapper wrapper;
		String error = "";
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//PROCESSING
		InfernalSettings infernalSettings = infernalSettingsService.read(infernalsettingsid);
		
		//NULL CHECK
		if(infernalSettings == null){
			throw new SettingsNotFoundException(infernalsettingsid);
		}
		
		//USER CLIENTSETTINGS CHECK
		if(!infernalSettings.getUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//RESPONSE
		wrapper = new InfernalSettingsWrapper();
		wrapper.add("data",infernalSettings);
		wrapper.setError(error);
		
		//RETURN
		return new ResponseEntity<InfernalSettingsWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/user/{userid}/infernalsettings/{infernalsettingsid}", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody
	public ResponseEntity<InfernalSettingsWrapper> deleteInfernalSettingsById(@PathVariable Long userid, @PathVariable Long infernalsettingsid){
		//VARS
		InfernalSettingsWrapper wrapper;
		String error = "";
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//ID should not be null or 0 cause its a delete
		if(infernalsettingsid == null || infernalsettingsid == 0L){
			throw new SettingsNotFoundException(infernalsettingsid);
		}
		
		//search settings by ID
		InfernalSettings infernalSettingsById = infernalSettingsService.read(infernalsettingsid);
		
		//settings with that ID should exist cause its a delete
		if (infernalSettingsById == null){
			throw new SettingsNotFoundException(infernalsettingsid);
		}
		
		//USER CHECK 2 
		if(!infernalSettingsById.getUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//delete the settings
		try{
			infernalSettingsService.delete(infernalSettingsById);
		} catch (DataIntegrityViolationException e){
			throw new InfernalSettingsInUseException(infernalSettingsById.getSets());
		}
		
		//RESPONSE
		wrapper = new InfernalSettingsWrapper();
		wrapper.add("data",infernalSettingsById);
		wrapper.setError(error);
		
		return new ResponseEntity<InfernalSettingsWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/user/{userid}/infernalsettings", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResponseEntity<InfernalSettingsWrapper> newInfernalSettings(@PathVariable Long userid, @ModelAttribute("infernalsettings") @Valid InfernalSettingsDTO dto) throws BindException {
		//VARS
		InfernalSettingsWrapper wrapper;
		String error = "";
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//If for some reason the ID of the dto is not equal to 0, set it to 0 (this would mean people are trying to do funny business)
		if(dto.getId() != 0){
			dto.setId(0L);
		}
		
		//Check if name exists already for that user (name is sets in this entity)
		InfernalSettings alreadyExistingSettings = infernalSettingsService.findByUserIdAndSets(user.getId(), dto.getSets());
		if(alreadyExistingSettings != null){
			throw new SettingsAlreadyExistException(dto.getSets());
		}
		//build clientSettings from the dto
		InfernalSettings newInfernalSettings = new InfernalSettings(dto);
		
		//set user
		newInfernalSettings.setUser(user);
		
		//create the settings in the database
		InfernalSettings createdInfernalSettings = infernalSettingsService.create(newInfernalSettings);
		
		//RESPONSE
		wrapper = new InfernalSettingsWrapper();
		wrapper.add("data",createdInfernalSettings);
		wrapper.setError(error);
		
		return new ResponseEntity<InfernalSettingsWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/user/{userid}/infernalsettings", method = RequestMethod.PUT, produces = "application/json")
	@ResponseBody
	public ResponseEntity<InfernalSettingsWrapper> updateInfernalSettings(@PathVariable Long userid, @ModelAttribute("infernalsettings") @Valid InfernalSettingsDTO dto) throws BindException {
		//VARS
		InfernalSettingsWrapper wrapper;
		String error = "";
			
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//search settings by ID
		InfernalSettings infernalSettingsById = infernalSettingsService.read(dto.getId());
		
		//settings with that ID should exist cause its an update
		if (infernalSettingsById == null){
			throw new SettingsNotFoundException(dto.getId());
		}
		
		//USER CHECK 2 
		if(!infernalSettingsById.getUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//search settings by Name
		InfernalSettings infernalSettingsByName = infernalSettingsService.findByUserIdAndSets(user.getId(), dto.getSets());
		
		//if clientSettingsByName null = OK; name got changed and doesn't exist yet
		if(infernalSettingsByName != null){
			//check if the settings by name are the same settings, if not we can't update (name needs to be unique for user)
			if(!infernalSettingsByName.equals(infernalSettingsById)){
				throw new SettingsAlreadyExistException(dto.getSets());
			}
		}
		
		//update existing settings from the dto
		infernalSettingsById.updateFromDTO(dto);
		
		//update the settigns in the database
		InfernalSettings updatedInfernalSettings = infernalSettingsService.update(infernalSettingsById);
		
		//RESPONSE
		wrapper = new InfernalSettingsWrapper();
		wrapper.add("data",updatedInfernalSettings);
		wrapper.setError(error);
		
		return new ResponseEntity<InfernalSettingsWrapper>(wrapper, HttpStatus.OK);
	}
	
	//importSettings
	@RequestMapping(path = "/user/{userid}/importsettings/{importsettingsid}", method = RequestMethod.GET)
	public ResponseEntity<ImportSettingsWrapper> findImportSettingsById(@PathVariable Long userid, @PathVariable Long importsettingsid) {
		//VARS
		ImportSettingsWrapper wrapper;
		String error = "";
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//PROCESSING
		ImportSettings importSettings = importSettingsService.read(importsettingsid);
		
		//NULL CHECK
		if(importSettings == null){
			throw new SettingsNotFoundException(importsettingsid);
		}
		
		//USER CLIENTSETTINGS CHECK
		if(!importSettings.getUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//RESPONSE
		wrapper = new ImportSettingsWrapper();
		wrapper.add("data",importSettings);
		wrapper.setError(error);
		
		//RETURN
		return new ResponseEntity<ImportSettingsWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/user/{userid}/importsettings/{importsettingsid}", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody
	public ResponseEntity<ImportSettingsWrapper> deleteImportSettingsById(@PathVariable Long userid, @PathVariable Long importsettingsid){
		//VARS
		ImportSettingsWrapper wrapper;
		String error = "";
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//ID should not be null or 0 cause its a delete
		if(importsettingsid == null || importsettingsid == 0L){
			throw new SettingsNotFoundException(importsettingsid);
		}
		
		//search settings by ID
		ImportSettings importSettingsById = importSettingsService.read(importsettingsid);
		
		//settings with that ID should exist cause its a delete
		if (importSettingsById == null){
			throw new SettingsNotFoundException(importsettingsid);
		}
		
		//USER CHECK 2 
		if(!importSettingsById.getUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//ID should not be the currently active import settings
		if(importsettingsid == user.getUserSettings().getActiveImportSettings()){
			throw new ImportSettingsInUseException(importSettingsById.getName());
		}
		
		//delete the settings
		importSettingsService.delete(importSettingsById);
		
		//RESPONSE
		wrapper = new ImportSettingsWrapper();
		wrapper.add("data",importSettingsById);
		wrapper.setError(error);
		
		return new ResponseEntity<ImportSettingsWrapper>(wrapper, HttpStatus.OK);
	}

	@RequestMapping(value = "/user/{userid}/importsettings", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResponseEntity<ImportSettingsWrapper> newImportSettings(@PathVariable Long userid, @ModelAttribute("importsettings") @Valid ImportSettingsDTO dto) throws BindException {
		//VARS
		ImportSettingsWrapper wrapper;
		String error = "";
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//If for some reason the ID of the dto is not equal to 0, set it to 0 (this would mean people are trying to do funny business)
		if(dto.getId() != 0){
			dto.setId(0L);
		}
		
		//Check if name exists already for that user
		ImportSettings alreadyExistingSettings = importSettingsService.findByUserIdAndName(user.getId(), dto.getName());
		if(alreadyExistingSettings != null){
			throw new SettingsAlreadyExistException(dto.getName());
		}
		//build clientSettings from the dto
		ImportSettings newImportSettings = new ImportSettings(dto);
		
		//set user
		newImportSettings.setUser(user);
		
		//create the settings in the database
		ImportSettings createdImportSettings = importSettingsService.create(newImportSettings);
		
		//RESPONSE
		wrapper = new ImportSettingsWrapper();
		wrapper.add("data",createdImportSettings);
		wrapper.setError(error);
		
		return new ResponseEntity<ImportSettingsWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/user/{userid}/importsettings", method = RequestMethod.PUT, produces = "application/json")
	@ResponseBody
	public ResponseEntity<ImportSettingsWrapper> updateImportSettings(@PathVariable Long userid, @ModelAttribute("importsettings") @Valid ImportSettingsDTO dto) throws BindException {
		//VARS
		ImportSettingsWrapper wrapper;
		String error = "";
			
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//search settings by ID
		ImportSettings importSettingsById = importSettingsService.read(dto.getId());
		
		//settings with that ID should exist cause its an update
		if (importSettingsById == null){
			throw new SettingsNotFoundException(dto.getId());
		}
		
		//USER CHECK 2 
		if(!importSettingsById.getUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//search settings by Name
		ImportSettings importSettingsByName = importSettingsService.findByUserIdAndName(user.getId(), dto.getName());
		
		//if clientSettingsByName null = OK; name got changed and doesn't exist yet
		if(importSettingsByName != null){
			//check if the settings by name are the same settings, if not we can't update (name needs to be unique for user)
			if(!importSettingsByName.equals(importSettingsById)){
				throw new SettingsAlreadyExistException(dto.getName());
			}
		}
		
		//update existing settings from the dto
		importSettingsById.updateFromDTO(dto);
		
		//update the settigns in the database
		ImportSettings updatedImportSettings = importSettingsService.update(importSettingsById);
		
		//RESPONSE
		wrapper = new ImportSettingsWrapper();
		wrapper.add("data",updatedImportSettings);
		wrapper.setError(error);
		
		return new ResponseEntity<ImportSettingsWrapper>(wrapper, HttpStatus.OK);
	}
	
	//userSettings
	@RequestMapping(value = "/user/{userid}/usersettings", method = RequestMethod.PUT, produces = "application/json")
	@ResponseBody
	public ResponseEntity<UserSettingsWrapper> updateUserSettings(@PathVariable Long userid, @ModelAttribute("usersettings") @Valid UserSettingsDTO dto) throws BindException {
		//VARS
		UserSettingsWrapper wrapper;
		String error = "";
			
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//get the current users settings
		UserSettings userSettings = userSettingsService.getByUser(user);
		
		//update existing settings from the dto
		userSettings.updateFromDTO(dto);
		
		//update the settigns in the database
		UserSettings updatedUserSettings = userSettingsService.update(userSettings);
		
		//RESPONSE
		wrapper = new UserSettingsWrapper();
		wrapper.add("data",updatedUserSettings);
		wrapper.setError(error);
		
		return new ResponseEntity<UserSettingsWrapper>(wrapper, HttpStatus.OK);
	}
}
