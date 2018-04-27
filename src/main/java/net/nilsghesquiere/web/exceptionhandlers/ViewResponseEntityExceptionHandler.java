package net.nilsghesquiere.web.exceptionhandlers;

import java.util.ArrayList;
import java.util.List;

import net.nilsghesquiere.web.annotations.ViewController;
import net.nilsghesquiere.web.error.EmailExistsException;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(annotations=ViewController.class)
@Order(2)
public class ViewResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ViewResponseEntityExceptionHandler.class);
	
	@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "Unspecified error";
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
	
	@ExceptionHandler({ MailAuthenticationException.class })
	public ResponseEntity<Object> handleMail(RuntimeException ex, WebRequest request) {
		LOGGER.error("500 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Failure: Problem sending mail", "MailError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({ EmailExistsException.class })
	public ResponseEntity<Object> handleEmailExists(RuntimeException ex, WebRequest request) {
		LOGGER.error("409 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Failure: A user with that email already exists", "EmailAlreadyExists");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleInternal(RuntimeException ex, WebRequest request) {
		LOGGER.error("500 Status Code", ex);
		GenericResponse bodyOfResponse = new GenericResponse("Failure: Internal Server Error", "InternalError");
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleBindException (BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.error("400 Status Code", ex);
		BindingResult result = ex.getBindingResult();
		result.reject("GlobalError","Failure: please correct field errors");
		//The following is a hackisch way to get rid of the duplicate sequence errors from password error
		for(FieldError error: result.getFieldErrors()){
			String defaultMessage = error.getDefaultMessage();
			if (defaultMessage.contains("<br/>")){
				List<String> errorList = new ArrayList<>();
				for(String msg : defaultMessage.split("<br/>")){
					if(!msg.isEmpty()){
						if(!errorList.contains(msg)){
							errorList.add(msg);
						}
					}
				}
				StringBuilder newDefaultMessage = new StringBuilder();
				for (String errorMsg : errorList){
					newDefaultMessage.append(errorMsg);
					if (errorList.indexOf(errorMsg) != errorList.size()-1){
						newDefaultMessage.append("<br/>");
					}
				}
				result.rejectValue(error.getField(), error.getCode(), newDefaultMessage.toString());
			}
		}
		GenericResponse bodyOfResponse = new GenericResponse(result.getFieldErrors(), result.getGlobalErrors());
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
}