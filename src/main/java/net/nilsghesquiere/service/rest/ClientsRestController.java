package net.nilsghesquiere.service.rest;

import java.util.List;

import net.nilsghesquiere.entities.Client;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.ClientService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.util.wrappers.ClientWrapper;
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
@RequestMapping("/api/clients")
public class ClientsRestController {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientsRestController.class);
	
	@Autowired 
	private ClientService clientService;
	
	@Autowired 
	private UserService userService;
	
	@Autowired
	private AuthenticationFacade authenticationFacade;	
	

	@RequestMapping(path = "/user/{userid}", method = RequestMethod.GET)
	public ResponseEntity<ClientWrapper> findClientsByUserId(@PathVariable Long userid) {
		//VARS
		ClientWrapper wrapper = new ClientWrapper();
		String error = "";
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		//PROCESSING
		List<Client> clients = clientService.findByUserId(userid);
		
		//RESPONSE
		wrapper.setError(error);
		wrapper.add("data",clients);
		
		
		//RETURN
		return new ResponseEntity<ClientWrapper>(wrapper, HttpStatus.OK);
	}
	
}
