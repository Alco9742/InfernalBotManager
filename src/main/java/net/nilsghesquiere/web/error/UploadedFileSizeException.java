package net.nilsghesquiere.web.error;

public class UploadedFileSizeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UploadedFileSizeException() {
		super();
	}

	public UploadedFileSizeException(final String message, final Throwable cause) {
		super(message,cause);
	}

	public UploadedFileSizeException(final String message) {
		super(message);
	}

	public UploadedFileSizeException(final Throwable cause) {
		super(cause);
	}

}