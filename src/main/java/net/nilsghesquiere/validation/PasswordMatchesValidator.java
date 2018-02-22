package net.nilsghesquiere.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import net.nilsghesquiere.annotations.PasswordMatches;
import net.nilsghesquiere.annotations.ValidEmail;
import net.nilsghesquiere.dto.UserDTO;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> { 

	@Override
	public void initialize(PasswordMatches constraintAnnotation){}

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context){
		UserDTO user = (UserDTO) obj;
		return user.getPassword().equals(user.getMatchingPassword());
	}
}

