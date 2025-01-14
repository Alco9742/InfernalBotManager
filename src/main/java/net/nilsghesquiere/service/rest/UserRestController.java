package net.nilsghesquiere.service.rest;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.util.wrappers.UserSingleWrapper;
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
@RequestMapping("/api/users")
public class UserRestController {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);
	
	@Autowired 
	private UserService userService;
	
	@Autowired 
	private AuthenticationFacade authenticationFacade;
	
	@RequestMapping(path = "/username/{username:.+}/id", method = RequestMethod.GET)
	public ResponseEntity<Long> findIdByUserName(@PathVariable String username) {
		//PROCESSING
		User user = userService.findUserByEmail(username);
		//Authenticated user check
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		//RETURN
		return new ResponseEntity<Long>(user.getId(), HttpStatus.OK);
	}
	
	@RequestMapping(path = "/username/{username:.+}", method = RequestMethod.GET)
	public ResponseEntity<UserSingleWrapper> findByUserName(@PathVariable String username) {
		//VARS
		UserSingleWrapper wrapper = new UserSingleWrapper();
		//PROCESSING
		User user = userService.findUserByEmail(username);
		//Authenticated user check
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		//RESPONSE
		wrapper.add("data",user);
		//RETURN
		return new ResponseEntity<UserSingleWrapper>(wrapper, HttpStatus.OK);
	}
	
}
