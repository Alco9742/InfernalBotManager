package net.nilsghesquiere.security;

import java.util.Arrays;
import java.util.Calendar;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.PasswordResetToken;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.persistence.dao.PasswordResetTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserSecurityService implements IUserSecurityService {

	@Autowired
	private PasswordResetTokenRepository passwordTokenRepository;

	// API

	@Override
	public User validatePasswordResetToken(long id, String token) {
		final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
		if ((passToken == null) || (passToken.getUser().getId() != id)) {
			return null;
		}

		final Calendar cal = Calendar.getInstance();
		if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
		    return null;
		}
		
		final User user = passToken.getUser();
		return user;
	}

}
