package net.nilsghesquiere.service.rest;

import java.util.ArrayList;
import java.util.List;

import net.nilsghesquiere.entities.Client;
import net.nilsghesquiere.entities.ClientSettings;
import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.ClientService;
import net.nilsghesquiere.service.web.ClientSettingsService;
import net.nilsghesquiere.service.web.InfernalSettingsService;
import net.nilsghesquiere.service.web.UserService;
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
public class ClientsRestController {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientsRestController.class);
	
	@Autowired 
	private ClientService clientService;
	
	@Autowired 
	private InfernalSettingsService infernalSettingsService;
	
	@Autowired 
	private ClientSettingsService clientSettingsService;
	
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
			ClientDTO dto = new ClientDTO(client.getId(),client.getTag(),client.getHWID(),client.getClientSettings().getId(),client.getInfernalSettings().getId(),client.getClientStatus());
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
				ClientDTO returnDTO = new ClientDTO(createdClient.getId(),createdClient.getTag(),"",createdClient.getClientSettings().getId(),createdClient.getInfernalSettings().getId(),createdClient.getClientStatus());
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
				ClientDTO returnDTO = new ClientDTO(updatedClient.getId(),updatedClient.getTag(),updatedClient.getHWID(), updatedClient.getClientSettings().getId(), updatedClient.getInfernalSettings().getId(),updatedClient.getClientStatus()); 
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
				ClientDTO deletedDTO = new ClientDTO(client.getId(),client.getTag(), client.getHWID(),client.getClientSettings().getId(),client.getInfernalSettings().getId(),client.getClientStatus());
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
				Client updatedClient = clientService.update(client);
				ClientDTO returnDto = new ClientDTO(updatedClient.getId(), updatedClient.getTag(),updatedClient.getHWID(),updatedClient.getClientSettings().getId(),updatedClient.getInfernalSettings().getId(),updatedClient.getClientStatus());
				returnDtos.add(returnDto);
			}
		}
		wrapper.add("data",returnDtos);
		return new ResponseEntity<ClientDTOWrapper>(wrapper,HttpStatus.OK);
	}
	
	
	//Methods for the InfernalBotManagerClient
	@RequestMapping(path = "/user/{userid}/tag/{tag}", method = RequestMethod.GET)
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
		wrapper.add(client.getId().toString(), client);
		
		//RETURN
		return new ResponseEntity<ClientSingleWrapper>(wrapper, HttpStatus.OK);
	}
}
