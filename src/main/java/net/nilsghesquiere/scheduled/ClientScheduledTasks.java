package net.nilsghesquiere.scheduled;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import net.nilsghesquiere.entities.Client;
import net.nilsghesquiere.service.web.ClientService;
import net.nilsghesquiere.util.enums.ClientStatus;
import net.nilsghesquiere.util.facades.AuthenticationFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ClientScheduledTasks {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientScheduledTasks.class);
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@Autowired private AuthenticationManager authManager;
	
	@Scheduled(fixedRate = 60000,initialDelay = 60000) //Excecute every minute
	public void checkConnectedClients(){
		List<Client> clientsToSetDisconnected = new ArrayList<Client>();
		List<Client> connectedClients = clientService.findByClientStatus(ClientStatus.CONNECTED);
		for (Client client : connectedClients){
			long secondsSinceLastPing = Duration.between(client.getLastPing(), LocalDateTime.now()).toMillis() / 1000;
			if(secondsSinceLastPing >= 45){
				clientsToSetDisconnected.add(client);
			}
		}
		if (!clientsToSetDisconnected.isEmpty()){
			UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("system", "NfAq5cKKRRgc2LvkfMPN");
			Authentication auth = authManager.authenticate(authReq);
			SecurityContext sc = SecurityContextHolder.getContext();
			sc.setAuthentication(auth);
			clientService.setClientsAsDisconnected(clientsToSetDisconnected);
		}
	}

	@Scheduled(fixedRate = 300000, initialDelay = 60000) //Excecute every five minutes
	public void checkDisconnectedClients(){
		List<Client> disconnectedClients = clientService.findByClientStatus(ClientStatus.DISCONNECTED);
		for (Client client : disconnectedClients){
			long secondsSinceLastPing = Duration.between(client.getLastPing(), LocalDateTime.now()).toMillis() / 1000;
			if(secondsSinceLastPing >= 300){
				//Send mail to user
				LOGGER.info("Time to send a mail");
			}
		}
	}
}
