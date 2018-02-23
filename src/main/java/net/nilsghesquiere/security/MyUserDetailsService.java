package net.nilsghesquiere.security;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.Role;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.persistence.dao.UserRepository;

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
	private static final Logger LOGGER = LoggerFactory.getLogger(MyUserDetailsService.class);
	private final UserRepository userRepository;
	
	@Autowired 
	public MyUserDetailsService(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsernameIgnoreCase(username);
		try{
			if (user == null) {
				throw new UsernameNotFoundException("No user found with username: "+ username);
			}
			boolean accountNonExpired = true;
			boolean credentialsNonExpired = true;
			boolean accountNonLocked = true;
			return new org.springframework.security.core.userdetails.User (
					user.getUsername(), 
					user.getPassword(), 
				user.isEnabled(),
				accountNonExpired, 
				credentialsNonExpired, 
				accountNonLocked, 
				getAuthorities(user.getRoles()));
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	private static List<GrantedAuthority> getAuthorities (List<Role> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

}
