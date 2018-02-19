package net.nilsghesquiere.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class JacksonConfiguration {
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new HibernateAwareObjectMapper();
		return mapper;
	}
	
}
