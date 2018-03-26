package net.nilsghesquiere.service.rest;

import net.nilsghesquiere.entities.GlobalVariable;
import net.nilsghesquiere.service.web.GlobalVariableService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.wrappers.GlobalVariableSingleWrapper;

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
@RequestMapping("/api/vars")
public class GlobalVariableRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalVariableRestController.class);
	
	@Autowired 
	private GlobalVariableService globalVariableService;
	
	@Autowired 
	private UserService userService;
	
	@RequestMapping(path = "/{name}", method = RequestMethod.GET)
	public ResponseEntity<GlobalVariableSingleWrapper> findByName(@PathVariable String name) {
		//VARS
		GlobalVariableSingleWrapper wrapper = new GlobalVariableSingleWrapper();
		String error = "";
		
		//PROCESSING
		GlobalVariable globalVariable = globalVariableService.findByName(name);
		
		//RESPONSE
		wrapper.setError(error);
		wrapper.add("data",globalVariable);
		
		//RETURN
		return new ResponseEntity<GlobalVariableSingleWrapper>(wrapper, HttpStatus.OK);
	}
}
