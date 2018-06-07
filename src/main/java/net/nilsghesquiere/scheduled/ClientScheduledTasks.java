package net.nilsghesquiere.scheduled;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import net.nilsghesquiere.entities.Client;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.UserSettings;
import net.nilsghesquiere.service.web.SystemTasksService;
import net.nilsghesquiere.util.enums.ClientStatus;
import net.nilsghesquiere.util.mailing.MailBuilders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClientScheduledTasks {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientScheduledTasks.class);
	
	@Autowired
	private SystemTasksService systemTasksService;
	
	@Autowired 
	private JavaMailSender mailSender;
	
	@Scheduled(fixedRate = 30000,initialDelay = 30000) //Excecute every 30 seconds
	public void checkConnectedClients(){
		List<Client> clientsToSetDisconnected = new ArrayList<Client>();
		List<Client> connectedClients = systemTasksService.findClientsByClientStatus(ClientStatus.CONNECTED);
		for (Client client : connectedClients){
			long secondsSinceLastPing = Duration.between(client.getLastPing(), LocalDateTime.now()).toMillis() / 1000;
			if(secondsSinceLastPing >= 35){
				clientsToSetDisconnected.add(client);
			}
		}
		if (!clientsToSetDisconnected.isEmpty()){
			systemTasksService.setClientsAsDisconnected(clientsToSetDisconnected);
		}
	}

	//got to de everything in a very roundabout way right now because no session = lazy loading issues
	//TODO test at home, doesn't work here because of firewall
	@Scheduled(fixedRate = 120000, initialDelay = 60000) //Excecute every two minutes
	public void checkDisconnectedClients(){
		List<Client> clientsToSendMail = new ArrayList<Client>();
		List<Client> disconnectedClients = systemTasksService.findClientsByClientStatus(ClientStatus.DISCONNECTED);
		for (Client client : disconnectedClients){
			if(!client.getDcMailSent()){
				User user = systemTasksService.getUserByClient(client);
				UserSettings userSettings = systemTasksService.getUserSettingsByUser(user);
				if(userSettings.getMailOnDisconnect()){
					long secondsSinceLastPing = Duration.between(client.getLastPing(), LocalDateTime.now()).toMillis() / 1000;
					if(secondsSinceLastPing >= 300){
						clientsToSendMail.add(client);
						SimpleMailMessage email= MailBuilders.buildClientDisconnectedMail(client.getTag(),user.getEmail(),secondsSinceLastPing);
						mailSender.send(email);
					}
				}
			}
		}
		if (!clientsToSendMail.isEmpty()){
			systemTasksService.setClientsDcMailSent(clientsToSendMail);
		}
	}
}
