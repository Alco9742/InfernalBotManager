package net.nilsghesquiere.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClientSettingsInUseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public ClientSettingsInUseException(String settingsname) {
		super("Could not delete settings '" + settingsname + "', the settings are still bound to a client");
	}
}
