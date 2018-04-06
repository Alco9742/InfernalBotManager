package net.nilsghesquiere.web.error;

public class UploadedFileMalformedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UploadedFileMalformedException() {
		super();
	}

	public UploadedFileMalformedException(final String message, final Throwable cause) {
		super(message,cause);
	}

	public UploadedFileMalformedException(final String message) {
		super(message);
	}

	public UploadedFileMalformedException(final Throwable cause) {
		super(cause);
	}

}