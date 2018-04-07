package net.nilsghesquiere.web.exceptionhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import net.nilsghesquiere.web.util.GenericResponse;

@ControllerAdvice
class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<Object> handleMultipart(RuntimeException ex, WebRequest request) {
		LOGGER.error("413 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Uploaded file size too big", "MultipartError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.PAYLOAD_TOO_LARGE, request);
	}
}