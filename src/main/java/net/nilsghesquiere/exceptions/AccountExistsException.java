package net.nilsghesquiere.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public AccountExistsException(Long accountId) {
		super("Sorry, account with ID '" + accountId + "' already exists.");
	}
}
