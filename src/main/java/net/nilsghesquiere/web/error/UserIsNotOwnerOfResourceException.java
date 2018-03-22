package net.nilsghesquiere.web.error;

public class UserIsNotOwnerOfResourceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserIsNotOwnerOfResourceException() {
		super();
	}

	public UserIsNotOwnerOfResourceException(final String message, final Throwable cause) {
		super(message,cause);
	}

	public UserIsNotOwnerOfResourceException(final String message) {
		super(message);
	}

	public UserIsNotOwnerOfResourceException(final Throwable cause) {
		super(cause);
	}

}