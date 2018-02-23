package net.nilsghesquiere.service.web;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.VerificationToken;
import net.nilsghesquiere.persistence.dao.RoleRepository;
import net.nilsghesquiere.persistence.dao.UserRepository;
import net.nilsghesquiere.persistence.dao.VerificationTokenRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;
import net.nilsghesquiere.util.enums.UserType;
import net.nilsghesquiere.web.dto.UserDTO;
import net.nilsghesquiere.web.error.EmailExistsException;
import net.nilsghesquiere.web.error.UsernameExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService implements IUserService{
	public static final String TOKEN_INVALID = "invalidToken";
	public static final String TOKEN_EXPIRED = "expired";
	public static final String TOKEN_VALID = "valid";

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private VerificationTokenRepository tokenRepository;
	
	
	
	@Override
	public User read(Long id){
		return userRepository.findOne(id);
	}

	@Override
	public List<User> findAll() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	@ModifyingTransactionalServiceMethod
	public void create(User user) {
		userRepository.save(user);
	}

	@Override
	@ModifyingTransactionalServiceMethod
	public void update(User user) {
		userRepository.save(user);
	}

	@Override
	@ModifyingTransactionalServiceMethod
	public void delete(User user) {
		userRepository.delete(user);
	}

	//TODO optional use bekijken
	@Override
	public Optional<User> findByUsername(String username) {
		return Optional.of(userRepository.findByUsernameIgnoreCase(username));
	}

	@Override
	public Optional<User> findByUserId(Long userId) {
		return Optional.of(userRepository.findById(userId));
	}
	
	public boolean emailExist(String email) {
		User user = userRepository.findByEmailIgnoreCase(email);
		if (user != null) {
			return true;
		}
		return false;
	}
	public boolean usernameExist(String email) {
		User user = userRepository.findByUsernameIgnoreCase(email);
		if (user != null) {
			return true;
		}
		return false;
	}

	@ModifyingTransactionalServiceMethod
	@Override 
	public User registerNewUserAccount(UserDTO userDTO)throws EmailExistsException,UsernameExistsException {
		
		if (emailExist(userDTO.getEmail())) {
			throw new EmailExistsException("There is already an account with that email adress: "+ userDTO.getEmail());
		}
		if (usernameExist(userDTO.getUsername())) {
			throw new EmailExistsException("There is already an account with that username: "+ userDTO.getUsername());
		}
		User user = new User();
		user.setUsername(userDTO.getUsername());
		user.setPassword(userDTO.getPassword());
		user.setEmail(userDTO.getEmail());
		user.setRoles(Arrays.asList(roleRepository.findByName(UserType.USER.getName())));
		return userRepository.save(user);
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
}
