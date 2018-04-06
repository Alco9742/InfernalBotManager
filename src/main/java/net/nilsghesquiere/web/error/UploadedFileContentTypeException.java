package net.nilsghesquiere.web.error;

public class UploadedFileContentTypeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UploadedFileContentTypeException() {
		super();
	}

	public UploadedFileContentTypeException(final String message, final Throwable cause) {
		super(message,cause);
	}

	public UploadedFileContentTypeException(final String message) {
		super(message);
	}

	public UploadedFileContentTypeException(final Throwable cause) {
		super(cause);
	}

}