package net.nilsghesquiere.web.controllers;

import net.nilsghesquiere.web.error.EmailExistsException;
import net.nilsghesquiere.web.util.GenericResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomResponseEntityExceptionHandler.class);
	
	@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "This should be application specific";
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
	
	@ExceptionHandler({ MailAuthenticationException.class })
	public ResponseEntity<Object> handleMail(RuntimeException ex, WebRequest request) {
		LOGGER.error("500 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Problem sending mail", "MailError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({ EmailExistsException.class })
	public ResponseEntity<Object> handleEmailExists(RuntimeException ex, WebRequest request) {
		LOGGER.error("409 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("A user with that email already exists", "EmailAlreadyExists");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleInternal(RuntimeException ex, WebRequest request) {
		LOGGER.error("500 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Internal Server Error", "InternalError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleBindException (BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.error("400 Status Code", ex);
		BindingResult result = ex.getBindingResult();
		GenericResponse bodyOfResponse = new GenericResponse(result.getFieldErrors(), result.getGlobalErrors());
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
}