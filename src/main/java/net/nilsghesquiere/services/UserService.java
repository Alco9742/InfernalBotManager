package net.nilsghesquiere.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.nilsghesquiere.annotations.ModifyingTransactionalServiceMethod;
import net.nilsghesquiere.annotations.ReadOnlyTransactionalService;
import net.nilsghesquiere.dto.UserDTO;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.enums.UserType;
import net.nilsghesquiere.exceptions.EmailExistsException;
import net.nilsghesquiere.exceptions.UsernameExistsException;
import net.nilsghesquiere.repositories.RoleRepository;
import net.nilsghesquiere.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ReadOnlyTransactionalService
public class UserService implements IUserService{
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	
	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository){
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	
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
		return Optional.of(userRepository.findByUsername(username));
	}

	@Override
	public Optional<User> findByUserId(Long userId) {
		return Optional.of(userRepository.findById(userId));
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
	
	public boolean emailExist(String email) {
		User user = userRepository.findByEmail(email);
		if (user != null) {
			return true;
		}
		return false;
	}
	public boolean usernameExist(String email) {
		User user = userRepository.findByUsername(email);
		if (user != null) {
			return true;
		}
		return false;
	}
}
