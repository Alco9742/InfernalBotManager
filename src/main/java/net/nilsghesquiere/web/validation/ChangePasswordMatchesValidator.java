package net.nilsghesquiere.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import net.nilsghesquiere.web.dto.UserChangePasswordDTO;

public class ChangePasswordMatchesValidator implements ConstraintValidator<ChangePasswordMatches, Object> { 

	@Override
	public void initialize(ChangePasswordMatches constraintAnnotation){}

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context){
		UserChangePasswordDTO user = (UserChangePasswordDTO) obj;
		return user.getNewPassword().equals(user.getMatchingPassword());
	}
}

