package net.nilsghesquiere.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public UserNotFoundException(String username) {
		super("Sorry, we could not find user '" + username + "'.");
	}
	public UserNotFoundException(Long userId) {
		super("Sorry, we could not find a user with ID'" + userId + "'.");
	}
}
