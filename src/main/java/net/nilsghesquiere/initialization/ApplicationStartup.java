package net.nilsghesquiere.initialization;

import net.nilsghesquiere.service.web.LolAccountService;
import net.nilsghesquiere.service.web.StorageService;
import net.nilsghesquiere.service.web.SystemTasksService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartup.class);
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired 
	private SystemTasksService systemTasksService;
	
	@Autowired 
	private LolAccountService accountService;
	
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		LOGGER.info("Performing ApplicationStartup Methods");
		//Start the storigare service
		storageService.init();
		//Set all clients as offline and delete all clientdata : we just booted, clients can't be connected or we will run into issues with the scheduled tasks
		setAllClientsAsOffline();
		//If user has no usersettings yet, create them (needed for update)
		systemTasksService.createUserSettingsIfNotExisting();
		return;
	}
	
	private void setAllClientsAsOffline(){
		// Set all clients on offline and delete their clientdata
		systemTasksService.setAllClientsAsOffline();
		//disabled this for now until the account fetching system gets reworked
		//systemTasksService.setAllInUseAccountsToReadyForUse();
	}
}
