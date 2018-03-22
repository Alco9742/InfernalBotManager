package net.nilsghesquiere.service.rest;

import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.facades.AuthenticationFacade;

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
	private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);
	
	@Autowired 
	private UserService userService;
	
	@Autowired 
	private AuthenticationFacade authenticationFacade;
	
	@RequestMapping(path = "/username/{username:.+}", method = RequestMethod.GET)
	public ResponseEntity<Long> findByUserName(@PathVariable String username) {
		//PROCESSING
		Long userId = userService.findUserByEmail(username).getId();
		//test
		LOGGER.info(authenticationFacade.getAuthenticatedUser().toString());
		//RETURN
		return new ResponseEntity<Long>(userId, HttpStatus.OK);
	}
	
}
