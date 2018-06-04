package net.nilsghesquiere.web.exceptionhandlers;

import net.nilsghesquiere.web.error.ActiveImportSettingsNotSelectedException;
import net.nilsghesquiere.web.error.ClientNotFoundException;
import net.nilsghesquiere.web.error.ClientSettingsInUseException;
import net.nilsghesquiere.web.error.ImportSettingsInUseException;
import net.nilsghesquiere.web.error.InfernalSettingsInUseException;
import net.nilsghesquiere.web.error.SettingsAlreadyExistException;
import net.nilsghesquiere.web.error.SettingsNotFoundException;
import net.nilsghesquiere.web.error.UploadedFileContentTypeException;
import net.nilsghesquiere.web.error.UploadedFileEmptyException;
import net.nilsghesquiere.web.error.UploadedFileMalformedException;
import net.nilsghesquiere.web.error.UploadedFileNullException;
import net.nilsghesquiere.web.error.UploadedFileSizeException;
import net.nilsghesquiere.web.error.UserIsNotOwnerOfResourceException;
import net.nilsghesquiere.web.error.UserNotFoundException;
import net.nilsghesquiere.web.util.GenericResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(annotations = RestController.class)
@Order(1)
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
	
	@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "This should be application specific";
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
	
	@ExceptionHandler({ UserNotFoundException.class })
	public ResponseEntity<Object> handleUserNotFound(RuntimeException ex, WebRequest request) {
		LOGGER.error("404 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Could not find the requested user", "UserNotFound");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({ UserIsNotOwnerOfResourceException.class })
	public ResponseEntity<Object> handleUserIsNotOwner(RuntimeException ex, WebRequest request) {
		LOGGER.error("500 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("User is not the owner of the requested resource", "AccessError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
	}

	@ExceptionHandler({ ClientNotFoundException.class })
	public ResponseEntity<Object> handleClientNotFound(RuntimeException ex, WebRequest request) {
		LOGGER.error("404 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Could not find the requested client", "ClientNotFoundError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({ SettingsNotFoundException.class })
	public ResponseEntity<Object> handleSettingsNotFound(RuntimeException ex, WebRequest request) {
		LOGGER.error("404 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Could not find the requested settings", "SettingsNotFoundError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({ SettingsAlreadyExistException.class })
	public ResponseEntity<Object> handleSettingsAlreadyExist(RuntimeException ex, WebRequest request) {
		LOGGER.error("404 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Settings with that name already exist, choose a unique name", "SettingsAlreadyExistError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
	
	@ExceptionHandler({ ActiveImportSettingsNotSelectedException.class })
	public ResponseEntity<Object> handleActiveImportSettingsNotSelected(RuntimeException ex, WebRequest request) {
		LOGGER.error("404 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("No active import settings selected, edit your user settings on the settings page.", "ActiveImportSettingsNotSelectedError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({ ImportSettingsInUseException.class })
	public ResponseEntity<Object> handleImportSettingsInUse(RuntimeException ex, WebRequest request) {
		LOGGER.error("404 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), "SettingsInUseError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({ InfernalSettingsInUseException.class })
	public ResponseEntity<Object> handleInfernalSettingsInUse(RuntimeException ex, WebRequest request) {
		LOGGER.error("404 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), "SettingsInUseError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({ ClientSettingsInUseException.class })
	public ResponseEntity<Object> handleClientSettingsInUse(RuntimeException ex, WebRequest request) {
		LOGGER.error("404 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), "SettingsInUseError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({ MailAuthenticationException.class })
	public ResponseEntity<Object> handleMail(RuntimeException ex, WebRequest request) {
		LOGGER.error("500 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Problem sending mail", "MailError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
	
	@ExceptionHandler({ UploadedFileSizeException.class })
	public ResponseEntity<Object> handleFileSizeException(RuntimeException ex, WebRequest request) {
		LOGGER.error("500 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Uploaded file size > 1MB!", "UploadSizeError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({ UploadedFileContentTypeException.class })
	public ResponseEntity<Object> handleFileContentTypeException(RuntimeException ex, WebRequest request) {
		LOGGER.error("500 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Uploaded file is not a plain text file!", "ContentTypeError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({ UploadedFileNullException.class })
	public ResponseEntity<Object> handleFileNullException(RuntimeException ex, WebRequest request) {
		LOGGER.error("500 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Uploaded file is null!", "NullFileError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({ UploadedFileMalformedException.class })
	public ResponseEntity<Object> handleFileMallformedException(RuntimeException ex, WebRequest request) {
		LOGGER.error("500 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Uploaded file is malformed, check the syntax", "MalformedFileError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({ UploadedFileEmptyException.class })
	public ResponseEntity<Object> handleFileEmptyException(RuntimeException ex, WebRequest request) {
		LOGGER.error("500 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Uploaded file is empty!", "EmptyFileError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleInternal(RuntimeException ex, WebRequest request) {
		LOGGER.error("500 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Internal Server Error", "InternalError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleBindException (BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.error("400 Status Code", ex);
		BindingResult result = ex.getBindingResult();
		result.reject("GlobalError","Failure: please correct field errors");
		GenericResponse bodyOfResponse = new GenericResponse(result.getFieldErrors(), result.getGlobalErrors());
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
}