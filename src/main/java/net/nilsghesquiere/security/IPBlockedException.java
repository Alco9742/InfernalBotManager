package net.nilsghesquiere.security;

import org.springframework.security.core.AuthenticationException;

public class IPBlockedException extends AuthenticationException{
	private static final long serialVersionUID = 1L;

	public IPBlockedException(String msg) {
		super(msg);
	}
	
}
