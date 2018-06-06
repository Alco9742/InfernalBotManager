package net.nilsghesquiere;

import net.nilsghesquiere.configuration.properties.StorageProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.scheduling.annotation.EnableScheduling;

@EntityScan(
		basePackageClasses = {Application.class, Jsr310JpaConverters.class}
)
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(StorageProperties.class)
public class Application extends SpringBootServletInitializer {
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}