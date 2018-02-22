package net.nilsghesquiere.facades;

import java.util.Optional;

import net.nilsghesquiere.entities.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationFacade {
	UserDetails getAuthenticatedUserDetails();
	Optional<User> getOptionalAuthenticatedUser();
	User getAuthenticatedUser();
	Authentication getAuthentication();
}
