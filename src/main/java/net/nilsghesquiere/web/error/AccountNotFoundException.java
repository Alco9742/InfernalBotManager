package net.nilsghesquiere.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public AccountNotFoundException(Long accountId) {
		super("Sorry, we could not find an account with id'" + accountId + "'.");
	}
}
