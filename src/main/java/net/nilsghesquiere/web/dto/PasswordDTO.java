package net.nilsghesquiere.web.dto;

import java.io.Serializable;

import lombok.Data;
import net.nilsghesquiere.web.validation.ValidPassword;

@Data
public class PasswordDTO implements Serializable{
	static final long serialVersionUID = 1L;
	private String oldPassword;
	@ValidPassword
	private String newPassword;
	
}
