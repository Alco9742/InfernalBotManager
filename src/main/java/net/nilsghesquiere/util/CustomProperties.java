package net.nilsghesquiere.util;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author pavan.solapure
 *
 */
public class CustomProperties extends Properties {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomProperties.class);

	private static final long serialVersionUID = 1L;

	public CustomProperties(DataSource dataSource) {
		super();
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> configs = jdbcTemplate
				.queryForList("select config_key, config_value from config_params");
		
		LOGGER.info("Loading properties from Database");
		for (Map<String, Object> config : configs) {
			setProperty((config.get("config_key")).toString(), (config.get("config_value")).toString());
		}
	}
}