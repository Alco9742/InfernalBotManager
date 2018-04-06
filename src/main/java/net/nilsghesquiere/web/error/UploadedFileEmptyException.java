package net.nilsghesquiere.web.error;

public class UploadedFileEmptyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UploadedFileEmptyException() {
		super();
	}

	public UploadedFileEmptyException(final String message, final Throwable cause) {
		super(message,cause);
	}

	public UploadedFileEmptyException(final String message) {
		super(message);
	}

	public UploadedFileEmptyException(final Throwable cause) {
		super(cause);
	}

}