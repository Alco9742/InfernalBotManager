package net.nilsghesquiere.services;

import java.util.List;
import java.util.Optional;

import net.nilsghesquiere.dto.UserDTO;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.exceptions.EmailExistsException;
import net.nilsghesquiere.exceptions.UsernameExistsException;

public interface IUserService {
	User read(Long id);
	List<User> findAll();
	void create(User user);
	void update(User user);
	void delete(User user);
	Optional<User> findByUsername(String username);
	Optional<User> findByUserId(Long userId);
	User registerNewUserAccount(UserDTO userDTO) throws EmailExistsException, UsernameExistsException;
	boolean emailExist(String email);
	boolean usernameExist(String email);
}
