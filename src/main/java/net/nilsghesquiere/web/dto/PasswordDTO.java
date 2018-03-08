package net.nilsghesquiere.web.dto;

import net.nilsghesquiere.validation.ValidPassword;

import java.io.Serializable;

import lombok.Data;

@Data
public class PasswordDTO implements Serializable{
	static final long serialVersionUID = 1L;
	private String oldPassword;
	@ValidPassword
	private String newPassword;
	
}
