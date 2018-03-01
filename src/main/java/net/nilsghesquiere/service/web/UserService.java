package net.nilsghesquiere.service.web;

import java.util.List;
import java.util.Optional;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.VerificationToken;
import net.nilsghesquiere.web.dto.UserDTO;
import net.nilsghesquiere.web.error.EmailExistsException;

public interface UserService {
	User read(Long id);
	List<User> findAll();
	void create(User user);
	void update(User user);
	void delete(User user);
	Optional<User> findOptionalByEmail(String email);
	Optional<User> findOptionalByUserId(Long userId);
	User findUserByEmail(String email);
	User findUserByUserId(Long userId);
	//new methods using baeldung
	boolean emailExist(String email);
	boolean usernameExist(String email);
	User registerNewUserAccount(UserDTO userDTO) throws EmailExistsException;
	User getUser(String verificationToken);
	void saveRegisteredUser(User user);
	void createVerificationTokenForUser(User user, String token);
	VerificationToken getVerificationToken(String VerificationToken);
	VerificationToken generateNewVerificationToken(String token);
	void createPasswordResetTokenForUser(User user, String token);
	void changeUserPassword(User user, String newPassword);
}
