package net.nilsghesquiere.util.wrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JSONResponseWithError extends JSONResponse{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonIgnore(false)
	private String error;
}
