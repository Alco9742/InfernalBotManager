package net.nilsghesquiere.security;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AuthenticationEventFailureListener 
implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationEventFailureListener.class);

  @Autowired
  private LoginAttemptService loginAttemptService;

  @Autowired
  private HttpServletRequest request;
  
	    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
	    	  final String xfHeader = request.getHeader("X-Forwarded-For");
	          if (xfHeader == null) {
	        	  LOGGER.info("Authentication failure for user with IP " + request.getRemoteAddr());
	              loginAttemptService.loginFailed(request.getRemoteAddr(), e.getAuthentication().getName());
	          } else {
	        	  LOGGER.info("Authentication failure for user with IP " + xfHeader.split(",")[0]);
	              loginAttemptService.loginFailed(xfHeader.split(",")[0], e.getAuthentication().getName());
	          }
	    }
}
