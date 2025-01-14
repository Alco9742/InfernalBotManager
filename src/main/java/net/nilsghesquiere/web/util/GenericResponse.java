package net.nilsghesquiere.web.util;

import java.util.List;

import lombok.Data;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Data
public class GenericResponse {
	private String message;
	private String error;
 
	public GenericResponse() {
		super();
		this.message = "";
		this.error = "";
	}
	
	public GenericResponse(String message) {
		super();
		this.message = message;
	}
 
	public GenericResponse(String message, String error) {
		super();
		this.message = message;
		this.error = error;
	}
	
	public GenericResponse(List<FieldError> fieldErrors, List<ObjectError> globalErrors) {
		super();
		ObjectMapper mapper = new ObjectMapper();
		try {
			this.message = mapper.writeValueAsString(fieldErrors);
			this.error = mapper.writeValueAsString(globalErrors);
		} catch (JsonProcessingException e) {
			this.message = "";
			this.error = "";
		}
	}
}