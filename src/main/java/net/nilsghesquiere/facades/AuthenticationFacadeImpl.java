package net.nilsghesquiere.facades;

import java.util.Optional;

import net.nilsghesquiere.entities.AppUser;
import net.nilsghesquiere.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {
	private final UserService userService;

	@Autowired
	public AuthenticationFacadeImpl(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public UserDetails getAuthenticatedUserDetails() {
		return (UserDetails) getAuthentication().getPrincipal();
	}

	@Override
	public Optional<AppUser> getOptionalAuthenticatedUser() {
		String username = getAuthentication().getName();
		if (username != "anonymousUser"){
			AppUser user = userService.findByUsername(username);
			if (user != null){
				return Optional.of(user);
			}
		} 
		return Optional.empty();
	}
	
	@Override
	public AppUser getAuthenticatedUser() {
		return getOptionalAuthenticatedUser().get();
	}
	
	@Override
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
}
