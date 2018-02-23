package net.nilsghesquiere.util.wrappers;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JSONResponseWithError extends JSONResponse{
	@JsonIgnore(false)
	private String error;
}
