package net.nilsghesquiere.security;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener 
  implements ApplicationListener<AuthenticationSuccessEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessEventListener.class);

    @Autowired
    private LoginAttemptService loginAttemptService;
    
    @Autowired
    private HttpServletRequest request;

    public void onApplicationEvent(AuthenticationSuccessEvent e) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            loginAttemptService.loginSucceeded(request.getRemoteAddr());
        } else {
            loginAttemptService.loginSucceeded(xfHeader.split(",")[0]);
        }
    }
}
