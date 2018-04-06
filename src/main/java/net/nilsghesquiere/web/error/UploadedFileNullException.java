package net.nilsghesquiere.web.error;

public class UploadedFileNullException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UploadedFileNullException() {
		super();
	}

	public UploadedFileNullException(final String message, final Throwable cause) {
		super(message,cause);
	}

	public UploadedFileNullException(final String message) {
		super(message);
	}

	public UploadedFileNullException(final Throwable cause) {
		super(cause);
	}

}