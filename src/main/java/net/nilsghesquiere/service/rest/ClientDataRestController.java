package net.nilsghesquiere.service.rest;

import java.util.ArrayList;
import java.util.List;

import net.nilsghesquiere.entities.ClientData;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.ClientDataService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.wrappers.ClientDataMap;
import net.nilsghesquiere.util.wrappers.ClientDataWrapper;

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
@RequestMapping("/api/clients")
public class ClientDataRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientDataRestController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ClientDataService clientDataService;
	
	
	@RequestMapping(path = "/user/{userid}", method = RequestMethod.GET)
	public ResponseEntity<ClientDataWrapper> findClientsByUserId(@PathVariable Long userid) {
		//VARS
		ClientDataWrapper wrapper = new ClientDataWrapper();
		String error = "";
		
		//PROCESSING
		List<ClientData> clientDatas = clientDataService.findByUserId(userid);
		
		//RESPONSE
		wrapper.setError(error);
		wrapper.add("data",clientDatas);
		
		
		//RETURN
		return new ResponseEntity<ClientDataWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}", method = RequestMethod.POST)
	public ResponseEntity<ClientDataWrapper> createOrUpdate(@PathVariable Long userid, @RequestBody ClientDataMap clientDataMap) {
		//VARS
		ClientDataWrapper wrapper = new ClientDataWrapper();
		List<ClientData> returnClientDatas = new ArrayList<>();
		String error = "";
		
		//LOOP (Will always be 1 clientData for now)
		for(ClientData clientData: clientDataMap.getMap().values()){
			//CHECKS
			if (clientData == null && error.equals("")){
				error = "Client data is empty";
			}
			//PROCESSING
			System.out.println(clientData);
			User user = userService.read(userid);
			clientData.setUser(user);
			ClientData clientDataFromDB = clientDataService.findByTagAndUserId(clientData.getTag(), userid);
			ClientData newClientData = null;
			if (clientDataFromDB != null){
				clientData.setId(clientDataFromDB.getId());
				newClientData = clientDataService.update(clientData);
			} else {
				newClientData = clientDataService.create(clientData);
			}

			if(newClientData != null){
				returnClientDatas.add(newClientData);
			} else {
				error ="Unspecified failure";
			}
		}
		
		//RESPONSE
		wrapper.add("data",returnClientDatas);
		wrapper.setError(error);

		//RETURN
		return new ResponseEntity<ClientDataWrapper>(wrapper,HttpStatus.CREATED);
	}
	
	@RequestMapping(path = "/user/{userid}/delete",method = RequestMethod.POST)
	public ResponseEntity<ClientDataWrapper> delete(@PathVariable Long userid, @RequestBody ClientDataMap clientDataMap) {
		ClientDataWrapper wrapper = new ClientDataWrapper();
		List<ClientData> deletedClientDatas = new ArrayList<>();
		for (ClientData clientData : clientDataMap.getMap().values()){
			//TODO checks
			//validateAccountById(lolAccount.getId());
			//validateUserByUserId(userid);
			clientDataService.delete(clientData);
			deletedClientDatas.add(clientData);
		}
		wrapper.add("data",deletedClientDatas);
		return new ResponseEntity<ClientDataWrapper>(wrapper,HttpStatus.OK);
	}	
}
