package net.nilsghesquiere.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SettingsNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public SettingsNotFoundException(Long settingsid) {
		super("Sorry, we could not find settings with id '" + settingsid + "'.");
	}
}
