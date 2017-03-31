package net.nilsghesquiere.datasource;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class DataSourceBean{
	
	@Bean
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource DataSource() {
		DataSource dataSource =  DataSourceBuilder.create().build();
		return dataSource;
		}
}

