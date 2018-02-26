package net.nilsghesquiere.web.dto;

import net.nilsghesquiere.validation.ValidPassword;
import lombok.Data;

@Data
public class PasswordDTO {
	private String oldPassword;
	@ValidPassword
	private String newPassword;
	
}
