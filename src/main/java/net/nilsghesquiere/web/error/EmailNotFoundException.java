package net.nilsghesquiere.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public EmailNotFoundException(String email) {
		super("Sorry, we could not find user '" + email + "'.");
	}
	public EmailNotFoundException(Long userId) {
		super("Sorry, we could not find a user with ID'" + userId + "'.");
	}
}
