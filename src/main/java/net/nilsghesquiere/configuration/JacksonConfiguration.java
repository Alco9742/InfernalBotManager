package net.nilsghesquiere.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JacksonConfiguration {
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new HibernateAwareObjectMapper();
		return mapper;
	}
}
