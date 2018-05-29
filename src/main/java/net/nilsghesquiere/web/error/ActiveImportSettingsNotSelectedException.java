package net.nilsghesquiere.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ActiveImportSettingsNotSelectedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public ActiveImportSettingsNotSelectedException() {
		super("No active import settings selected, edit your user settings on the settings page.");
	}
}
