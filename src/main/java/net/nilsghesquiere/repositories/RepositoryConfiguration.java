package net.nilsghesquiere.repositories;

import net.nilsghesquiere.configuration.HibernateAwareObjectMapper;
import net.nilsghesquiere.entities.AppUser;
import net.nilsghesquiere.entities.LolAccount;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"net.nilsghesquiere.entities"})
@EnableJpaRepositories(basePackages = {"net.nilsghesquiere.repositories"})
@EnableTransactionManagement
public class RepositoryConfiguration {
	
	@Bean
	public RepositoryRestConfigurer repositoryRestConfigurer() {
		return new RepositoryRestConfigurerAdapter() {
			@Override
			public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
				config.exposeIdsFor(AppUser.class,LolAccount.class);
			}
			
			@Override 
			public void configureJacksonObjectMapper(ObjectMapper objectMapper){
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
			}
			
//			@Override
//			public void configureExceptionHandlerExceptionResolver(ExceptionHandlerExceptionResolver exceptionResolver){
//				exceptionResolver.
//			}
		};
	}

}

