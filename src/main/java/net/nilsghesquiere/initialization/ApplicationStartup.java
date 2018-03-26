package net.nilsghesquiere.initialization;

import net.nilsghesquiere.service.web.StorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartup.class);
	
	@Autowired
	private StorageService storageService;
	
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		LOGGER.info("Performing ApplicationStartup Methods");
		storageService.init();
		return;
	}
}
