package net.nilsghesquiere.web.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

import org.springframework.http.HttpStatus;

@Data
public class ApiError {
	 
    private HttpStatus status;
    private String message;
    private List<String> errors;
 
    public ApiError() {
        super();
        this.status = HttpStatus.OK;
        this.message = "";
        this.errors = new ArrayList<String>();
    }
    
    public ApiError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
 
    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
}