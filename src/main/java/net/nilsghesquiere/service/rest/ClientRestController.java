package net.nilsghesquiere.service.rest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import net.nilsghesquiere.entities.Client;
import net.nilsghesquiere.entities.ClientSettings;
import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.ClientDataService;
import net.nilsghesquiere.service.web.ClientService;
import net.nilsghesquiere.service.web.ClientSettingsService;
import net.nilsghesquiere.service.web.InfernalSettingsService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.enums.ClientAction;
import net.nilsghesquiere.util.enums.ClientStatus;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.util.wrappers.ClientDTOMap;
import net.nilsghesquiere.util.wrappers.ClientDTOWrapper;
import net.nilsghesquiere.util.wrappers.ClientSingleWrapper;
import net.nilsghesquiere.web.dto.ClientDTO;
import net.nilsghesquiere.web.error.UserIsNotOwnerOfResourceException;

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
public class ClientRestController {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientRestController.class);
	
	@Autowired 
	private ClientService clientService;
	
	@Autowired 
	private InfernalSettingsService infernalSettingsService;
	
	@Autowired 
	private ClientSettingsService clientSettingsService;
	
	@Autowired
	private ClientDataService clientDataService;
	
	@Autowired 
	private UserService userService;
	
	@Autowired
	private AuthenticationFacade authenticationFacade;	
	

	@RequestMapping(path = "/user/{userid}", method = RequestMethod.GET)
	public ResponseEntity<ClientDTOWrapper> findClientsByUserId(@PathVariable Long userid) {
		//VARS
		ClientDTOWrapper wrapper = new ClientDTOWrapper();
		List<ClientDTO> clientDTOList = new ArrayList<>();
		String error = "";
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		//PROCESSING
		List<Client> clients = clientService.findByUserId(userid);
		for (Client client: clients){
			ClientDTO dto = new ClientDTO(client);
			clientDTOList.add(dto);
		}
		//RESPONSE
		wrapper.setError(error);
		wrapper.add("data",clientDTOList);
		
		//RETURN
		return new ResponseEntity<ClientDTOWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}", method = RequestMethod.POST)
	public ResponseEntity<ClientDTOWrapper> create(@PathVariable Long userid, @RequestBody ClientDTOMap clientMap) {
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//VARS
		ClientDTOWrapper wrapper = new ClientDTOWrapper();
		List<ClientDTO> returnClientDTOList = new ArrayList<>();
		String error = "";
		
		//LOOP (Will always be 1 dto for now)
		for(ClientDTO clientDTO : clientMap.getMap().values()){
			//CHECKS
			error = "";
			if (clientDTO == null){
				error = "Client is empty";
			}
			//check if the name is not empty 
			if(error.isEmpty() && clientDTO.getTag().trim().isEmpty()){
				error ="Tag can't be empty";
			}
			
			//check if one of the settings is null
			if(error.isEmpty() && (clientDTO.getClientSettings() == null || clientDTO.getInfernalSettings() == null)){
				error ="Settings can't be null";
			}
			
			//Check if the client tag already exists for the user or not
			Client existingClient = clientService.getByUserIdAndTag(userid, clientDTO.getTag());
			if (error.isEmpty() && existingClient != null){
				error ="Tag already exists on the server for this user";
			}
			
			if (error.isEmpty()){
				//PROCESSING
				ClientSettings clientSettings = clientSettingsService.read(clientDTO.getClientSettings());
				InfernalSettings infernalSettings = infernalSettingsService.read(clientDTO.getInfernalSettings());
				Client newClient = new Client(clientDTO.getTag(),user,infernalSettings,clientSettings);	
				Client createdClient = clientService.create(newClient);
				ClientDTO returnDTO = new ClientDTO(createdClient);
				returnClientDTOList.add(returnDTO);
			}
		}
		
		//RESPONSE
		wrapper.add("data",returnClientDTOList);
		wrapper.setError(error);

		//RETURN
		return new ResponseEntity<ClientDTOWrapper>(wrapper,HttpStatus.CREATED);
	}
	
	@RequestMapping(path = "/user/{userid}",method = RequestMethod.PUT)
	public ResponseEntity<ClientDTOWrapper> update(@PathVariable Long userid,@RequestBody ClientDTOMap clientMap) {
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		String error = "";
		ClientDTOWrapper wrapper = new ClientDTOWrapper();
		List<ClientDTO> returnClientDTOList = new ArrayList<>();
		
		//LOOP (Will always be 1 dto for now)
		for (ClientDTO dto : clientMap.getMap().values()){
			Client oldClient = clientService.read(dto.getId());
			//USER CHECK 2
			if (!oldClient.getUser().equals(user)) {
				throw new UserIsNotOwnerOfResourceException();
			}
			
			//check if the name is not empty 
			if(error.isEmpty() && dto.getTag().trim().isEmpty()){
				error ="Tag can't be empty";
			}
			
			//check if one of the settings is null
			if(error.isEmpty() && (dto.getClientSettings() == null || dto.getInfernalSettings() == null)){
				error ="Settings can't be null";
			}
			
			if (error.isEmpty()){
				//Check if the client tag already exists for the user or not
				Client existingClient = clientService.getByUserIdAndTag(userid, dto.getTag());
				if (existingClient != null && !existingClient.equals(oldClient)){
					error ="Tag already exists on the server for this user";
				}
			}
			
			if(error.isEmpty()){
				ClientSettings clientSettings = clientSettingsService.read(dto.getClientSettings());
				InfernalSettings infernalSettings = infernalSettingsService.read(dto.getInfernalSettings());
				oldClient.setTag(dto.getTag());
				oldClient.setClientSettings(clientSettings);
				oldClient.setInfernalSettings(infernalSettings);
				Client updatedClient = clientService.update(oldClient);
				ClientDTO returnDTO = new ClientDTO(updatedClient); 
				returnClientDTOList.add(returnDTO);
			}
		}
		
		wrapper.add("data",returnClientDTOList);
		wrapper.setError(error);
		return new ResponseEntity<ClientDTOWrapper>(wrapper,HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}/delete",method = RequestMethod.POST)
	public ResponseEntity<ClientDTOWrapper> delete(@PathVariable Long userid, @RequestBody ClientDTOMap clientMap) {
		ClientDTOWrapper wrapper = new ClientDTOWrapper();
		List<ClientDTO> deletedClientDTOs = new ArrayList<>();
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		for (ClientDTO dto : clientMap.getMap().values()){
			Client client = clientService.read(dto.getId());
			//do nothing if client is null for some reason
			//do nothing if users don't match, should not happen unless someone is trying something funky
			if(client != null && client.getUser().equals(user)){
				clientService.delete(client);
				ClientDTO deletedDTO = new ClientDTO(client);
				deletedClientDTOs.add(deletedDTO);
			}
		}
		wrapper.add("data",deletedClientDTOs);
		return new ResponseEntity<ClientDTOWrapper>(wrapper,HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}/resetHWID",method = RequestMethod.PUT)
	public ResponseEntity<ClientDTOWrapper> resetHWID(@PathVariable Long userid,@RequestBody Long[] ids) {
		ClientDTOWrapper wrapper = new ClientDTOWrapper();
		List<ClientDTO> returnDtos = new ArrayList<>();
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		for (Long id : ids){
			Client client = clientService.read(id);
			if (client != null && client.getUser().equals(user)){
				client.setHWID("");
				client.setClientStatus(ClientStatus.UNASSIGNED);
				client.setClientAction(ClientAction.NONE);
				client.setError(false);
				client.setDcMailSent(false);
				client.setLastPing(null);
				Client updatedClient = clientService.update(client);
				ClientDTO returnDto = new ClientDTO(updatedClient);
				returnDtos.add(returnDto);
			}
		}
		wrapper.add("data",returnDtos);
		return new ResponseEntity<ClientDTOWrapper>(wrapper,HttpStatus.OK);
	}
	
	
	//Methods for the InfernalBotManagerClient
	@RequestMapping(path = "/user/{userid}/client/tag/{tag}", method = RequestMethod.GET)
	public ResponseEntity<ClientSingleWrapper> findByUserIdAndTag(@PathVariable Long userid, @PathVariable String tag) {
		ClientSingleWrapper wrapper = new ClientSingleWrapper();
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//PROCESSING
		Client client = clientService.getByUserIdAndTag(userid, tag);
		
		//RESPONSE
		wrapper.add("data", client);
		
		//RETURN
		return new ResponseEntity<ClientSingleWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}/client/{clientid}/register", method = RequestMethod.PUT)
	public ResponseEntity<ClientSingleWrapper> registerHWID(@PathVariable Long userid, @PathVariable Long clientid, @RequestBody String hwid) {
		ClientSingleWrapper wrapper = new ClientSingleWrapper();
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//PROCESSING
		Client client = clientService.read(clientid);
		
		//USER CHECK 2;
		if(!client.getUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//Check for the 00:00:00:00:00:00:00:e0 HWID
		if(client.getHWID().trim().isEmpty() || client.getHWID().trim().equals("00:00:00:00:00:00:00:e0")){
			client.setHWID(hwid);
			client.setClientStatus(ClientStatus.CONNECTED);
			client.setClientAction(ClientAction.RUN);
			client.setLastPing(LocalDateTime.now());
			client.setError(false);
			client.setDcMailSent(false);
			Client updatedClient = clientService.update(client);
			wrapper.add("data", updatedClient);
		} else {
			wrapper.add("data", client);
		}
		
		//RETURN
		return new ResponseEntity<ClientSingleWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}/actions/{action}",method = RequestMethod.PUT)
	public ResponseEntity<ClientDTOWrapper> actions(@PathVariable Long userid, @PathVariable ClientAction action, @RequestBody Long[] ids) {
		ClientDTOWrapper wrapper = new ClientDTOWrapper();
		List<ClientDTO> returnDtos = new ArrayList<>();
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		for (Long id : ids){
			Client client = clientService.read(id);
			if (client != null && client.getUser().equals(user)){
				//nullcheck for old clients with no action
				if(client.getClientAction() != null && !client.getClientAction().equals(ClientAction.NONE)){
					client.setClientAction(action);
					Client updatedClient = clientService.update(client);
					ClientDTO returnDto = new ClientDTO(updatedClient);
					returnDtos.add(returnDto);
				}
			}
		}
		wrapper.add("data",returnDtos);
		return new ResponseEntity<ClientDTOWrapper>(wrapper,HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}/actions/{action}/all",method = RequestMethod.PUT)
	public ResponseEntity<ClientDTOWrapper> actionsAll(@PathVariable Long userid, @PathVariable ClientAction action) {
		ClientDTOWrapper wrapper = new ClientDTOWrapper();
		List<ClientDTO> returnDtos = new ArrayList<>();
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		List<Client> clients = clientService.findByUserId(userid);
		
		for (Client client : clients){
			if (client != null && client.getUser().equals(user)){
				//nullcheck for old clients with no action
				if(client.getClientAction() != null && !client.getClientAction().equals(ClientAction.NONE)){
					client.setClientAction(action);
					Client updatedClient = clientService.update(client);
					ClientDTO returnDto = new ClientDTO(updatedClient);
					returnDtos.add(returnDto);
				}
			}
		}
		wrapper.add("data",returnDtos);
		return new ResponseEntity<ClientDTOWrapper>(wrapper,HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}/client/{clientid}/ping", method = RequestMethod.PUT)
	public ResponseEntity<ClientAction> ping(@PathVariable Long userid, @PathVariable Long clientid, @RequestBody ClientStatus status) {
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//PROCESSING
		Client client = clientService.read(clientid);
		
		//USER CHECK 2;
		if(!client.getUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}

		client.setClientStatus(status);
		
		//if status = offline && it has clientData remove the clientData
		if (status == ClientStatus.OFFLINE && client.getClientData() != null){
			clientDataService.deleteById(client.getClientData().getId());
			client.setClientAction(ClientAction.NONE);
			client.setClientData(null);
			client.setLastPing(null);
		} else {
			client.setLastPing(LocalDateTime.now());
		}
		
		client.setDcMailSent(false);
		client.setError(false);
		
		//Check client action and respond
		ClientAction action = client.getClientAction();
		
		//TODO figure out how we do this
		switch (action){
			case NONE:
				if(status == ClientStatus.CONNECTED){
					client.setClientAction(ClientAction.RUN);
				}
				break;
			default:
				break;
		}
		
		clientService.update(client);
		
		//RETURN
		return new ResponseEntity<ClientAction>(action, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}/client/{clientid}/action", method = RequestMethod.PUT)
	public ResponseEntity<ClientAction> setAction(@PathVariable Long userid, @PathVariable Long clientid, @RequestBody ClientAction action) {
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//PROCESSING
		Client client = clientService.read(clientid);
		
		//USER CHECK 2;
		if(!client.getUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}

		client.setClientAction(action);
		
		clientService.update(client);
		
		//RETURN
		return new ResponseEntity<ClientAction>(action, HttpStatus.OK);
	}
}
