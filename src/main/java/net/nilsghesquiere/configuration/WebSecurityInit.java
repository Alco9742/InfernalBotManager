package net.nilsghesquiere.configuration;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.SessionTrackingMode;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class WebSecurityInit extends AbstractSecurityWebApplicationInitializer {

	@Override
	protected Set<SessionTrackingMode> getSessionTrackingModes() {
		return EnumSet.of(SessionTrackingMode.COOKIE);
	}
}