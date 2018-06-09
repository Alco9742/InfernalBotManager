package net.nilsghesquiere.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component("authenticationFailureHandler")
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		setDefaultFailureUrl("/login?error=true");
		super.onAuthenticationFailure(request, response, exception);
		
		String errorMessage = "Bad credentials";
 
		if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
			errorMessage = "User is disabled";
		} else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
			errorMessage = "User account has expired";
		} else if (exception.getMessage().equalsIgnoreCase("blocked")) {
			errorMessage = "User IP is currently blocked from performing login attempts, contact Alco";
		}
		request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
	}
	

}
