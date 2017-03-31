package net.nilsghesquiere.facades;

import java.util.Optional;

import net.nilsghesquiere.entities.AppUser;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationFacade {
	UserDetails getAuthenticatedUserDetails();
	Optional<AppUser> getOptionalAuthenticatedUser();
	AppUser getAuthenticatedUser();
	Authentication getAuthentication();
}
