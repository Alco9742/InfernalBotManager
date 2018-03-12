package net.nilsghesquiere.service.rest;

import java.util.ArrayList;
import java.util.List;

import net.nilsghesquiere.entities.GlobalVariable;
import net.nilsghesquiere.service.web.GlobalVariableService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.util.wrappers.GlobalVariableMap;
import net.nilsghesquiere.util.wrappers.GlobalVariableSingleWrapper;
import net.nilsghesquiere.util.wrappers.GlobalVariableWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class GlobalVariablesRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalVariablesRestController.class);
	
	@Autowired 
	private GlobalVariableService globalVariableService;
	
	@RequestMapping(path = "/globalvars", method = RequestMethod.GET)
	public ResponseEntity<GlobalVariableWrapper> findAll() {
		//VARS
		GlobalVariableWrapper wrapper = new GlobalVariableWrapper();
		String error = "";
		
		//PROCESSING
		List<GlobalVariable> globalVariables = globalVariableService.findAll();
		
		//RESPONSE
		wrapper.setError(error);
		wrapper.add("data",globalVariables);
		
		//RETURN
		return new ResponseEntity<GlobalVariableWrapper>(wrapper, HttpStatus.OK);
	}
	
	
	@RequestMapping(path = "/globalvars/name/{name}", method = RequestMethod.GET)
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
	
	@RequestMapping(path = "/globalvars", method = RequestMethod.POST)
	public ResponseEntity<GlobalVariableWrapper> create(@RequestBody GlobalVariableMap globalVariableMap) {
		//VARS
		GlobalVariableWrapper wrapper = new GlobalVariableWrapper();
		List<GlobalVariable> returnVariables = new ArrayList<>();
		String error = "";
		
		//LOOP (Will always be 1 variable for now)
		for(GlobalVariable globalVariable: globalVariableMap.getMap().values()){
			//CHECKS
			if (globalVariable == null){
				error = "Variable is empty";
			}
			//PROCESSING
			GlobalVariable newVariable = globalVariableService.create(globalVariable);
			if(newVariable != null){
				returnVariables.add(newVariable);
			}
		}
		//RESPONSE
		wrapper.add("data",returnVariables);
		wrapper.setError(error);

		//RETURN
		return new ResponseEntity<GlobalVariableWrapper>(wrapper,HttpStatus.CREATED);
	}
	
	
	@RequestMapping(path = "/globalvars",method = RequestMethod.PUT)
	public ResponseEntity<GlobalVariableWrapper> update(@RequestBody GlobalVariableMap globalVariableMap) {
		String error = "";
		GlobalVariableWrapper wrapper = new GlobalVariableWrapper();
		List<GlobalVariable> returnVariables = new ArrayList<>();
		for (GlobalVariable globalVariable : globalVariableMap.getMap().values()){
			GlobalVariable updatedVariable = globalVariableService.update(globalVariable);
			if (updatedVariable != null){
				returnVariables.add(updatedVariable);
			} else {
				if (error.equals("")){
					error = "The following keys are already present on the server: " + globalVariable.getName() ;
				} else {
					error = error + ", " + globalVariable.getName();
				}
			}
		}
		wrapper.add("data",returnVariables);
		wrapper.setError(error);
		return new ResponseEntity<GlobalVariableWrapper>(wrapper,HttpStatus.OK);
	}
	
	@RequestMapping(path = "/globalvars/delete",method = RequestMethod.POST)
	public ResponseEntity<GlobalVariableWrapper> delete(@RequestBody GlobalVariableMap globalVariableMap) {
		GlobalVariableWrapper wrapper = new GlobalVariableWrapper();
		List<GlobalVariable> deletedVariables = new ArrayList<>();
		for (GlobalVariable globalVariable : globalVariableMap.getMap().values()){
			globalVariableService.delete(globalVariable);
			deletedVariables.add(globalVariable);
		}
		wrapper.add("data",deletedVariables);
		return new ResponseEntity<GlobalVariableWrapper>(wrapper,HttpStatus.OK);
	}
	
}
