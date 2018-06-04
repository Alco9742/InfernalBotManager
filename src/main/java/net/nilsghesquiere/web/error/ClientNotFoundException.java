package net.nilsghesquiere.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClientNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public ClientNotFoundException(Long clientId) {
		super("Sorry, we could not find an client with id'" + clientId + "'.");
	}
}
