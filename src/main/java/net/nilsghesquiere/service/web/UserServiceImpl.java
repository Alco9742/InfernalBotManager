package net.nilsghesquiere.service.web;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.entities.PasswordResetToken;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.VerificationToken;
import net.nilsghesquiere.persistence.dao.PasswordResetTokenRepository;
import net.nilsghesquiere.persistence.dao.RoleRepository;
import net.nilsghesquiere.persistence.dao.UserRepository;
import net.nilsghesquiere.persistence.dao.VerificationTokenRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;
import net.nilsghesquiere.web.dto.UserDTO;
import net.nilsghesquiere.web.error.EmailExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	public static final String TOKEN_INVALID = "invalidToken";
	public static final String TOKEN_EXPIRED = "expired";
	public static final String TOKEN_VALID = "valid";

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private VerificationTokenRepository tokenRepository;
	
	@Autowired
	private PasswordResetTokenRepository passwordTokenRepository;
	
	@Autowired
	private InfernalSettingsService infernalSettingsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User read(Long id){
		return userRepository.findOne(id);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<User> findAll() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	@ModifyingTransactionalServiceMethod
	public User create(User user) {
		return userRepository.save(user);
	}

	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public User update(User user) {
		return userRepository.save(user);
	}

	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void delete(User user) {
		userRepository.delete(user);
	}

	@Override
	public User findUserByEmail(String username) {
		return userRepository.findByEmailIgnoreCase(username);
	}

	@Override
	public User findUserByUserId(Long userId) {
		return userRepository.findById(userId);
	}
	
	public boolean emailExist(String email) {
		User user = userRepository.findByEmailIgnoreCase(email);
		if (user != null) {
			return true;
		}
		return false;
	}
	public boolean usernameExist(String email) {
		User user = userRepository.findByEmailIgnoreCase(email);
		if (user != null) {
			return true;
		}
		return false;
	}

	@ModifyingTransactionalServiceMethod
	@Override 
	public User registerNewUserAccount(UserDTO userDTO)throws EmailExistsException {
		if (emailExist(userDTO.getEmail())) {
			throw new EmailExistsException("There is already an account with that email adress: "+ userDTO.getEmail());
		}
		User user = new User();
		
		user.setEmail(userDTO.getEmail());
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
		User returnUser = userRepository.save(user);
		
		InfernalSettings inferalSettings = infernalSettingsService.create(new InfernalSettings(returnUser));
		user.setInfernalSettings(inferalSettings);
		return returnUser;
	}
	
	@Override
	public User getUser(final String verificationToken) {
		final VerificationToken token = tokenRepository.findByToken(verificationToken);
		if (token != null) {
			return token.getUser();
		}
		return null;
	}

	@Override
	public void createVerificationTokenForUser(final User user, final String token) {
		final VerificationToken myToken = new VerificationToken(token, user);
		tokenRepository.save(myToken);
	}

	@Override
	public VerificationToken getVerificationToken(String VerificationToken) {
		return tokenRepository.findByToken(VerificationToken);
	}

	@Override
	public void saveRegisteredUser(User user) {
		userRepository.save(user);
	}

	@Override
	public VerificationToken generateNewVerificationToken(String existingVerificationToken) {
		VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
		vToken.updateToken(UUID.randomUUID().toString());
		vToken = tokenRepository.save(vToken);
		return vToken;
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public Optional<User> findOptionalByEmail(String email) {
		return Optional.of(userRepository.findByEmailIgnoreCase(email));
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public Optional<User> findOptionalByUserId(Long userId) {
		return Optional.of(userRepository.findById(userId));
	}

	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
		PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordTokenRepository.save(myToken);
	}

	@Override
	public void changeUserPassword(User user, String newPassword) {
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);

	}

	@Override
	public long countAll() {
		return userRepository.count();
	}
}
