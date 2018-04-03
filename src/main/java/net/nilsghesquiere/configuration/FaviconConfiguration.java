package net.nilsghesquiere.configuration;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Configuration
public class FaviconConfiguration {

	@Autowired
	ApplicationContext applicationContext;

	@Bean
	public SimpleUrlHandlerMapping faviconHandlerMapping() {
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setOrder(Integer.MIN_VALUE);
		mapping.setUrlMap(Collections.singletonMap("favicon.ico",faviconRequestHandler()));
		return mapping;
	}
	
	@Bean
	protected ResourceHttpRequestHandler faviconRequestHandler() {
		ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
		requestHandler.setLocations(Arrays.<Resource> asList(applicationContext.getResource("/")));
		requestHandler.setCacheSeconds(0);
		return requestHandler;
	}
}