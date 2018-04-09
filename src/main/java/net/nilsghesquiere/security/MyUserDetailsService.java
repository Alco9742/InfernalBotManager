package net.nilsghesquiere.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import net.nilsghesquiere.entities.Role;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.RoleService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.web.error.IPBlockedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(MyUserDetailsService.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private LoginAttemptService loginAttemptService;
	
	@Autowired
	private HttpServletRequest request;
	
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException,IPBlockedException {
		String ip = getClientIP();
		if (loginAttemptService.isBlocked(ip)) {
			throw new IPBlockedException("blocked");
		}
		User user = userService.findUserByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("No user found with username: " + email);
		}
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(), user.getPassword(), user.isEnabled(), true, true,true, 
				getAuthorities(user.getRoles()));
	}

    private final Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
    	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    	for (Role role: roles) {
    		authorities.add(new SimpleGrantedAuthority(role.getName()));
    		authorities.addAll(role.getPrivileges()
    				.stream()
    				.map(p -> new SimpleGrantedAuthority(p.getName()))
    				.collect(Collectors.toList()));
    	}
        return authorities;
    }
	
	private String getClientIP() {
		String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null){
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
	}
}
