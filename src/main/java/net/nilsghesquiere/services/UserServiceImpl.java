package net.nilsghesquiere.services;

import java.util.List;

import net.nilsghesquiere.entities.AppUser;
import net.nilsghesquiere.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ReadOnlyTransactionalService
public class UserServiceImpl implements UserService{
	private final UserRepository userRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	public AppUser read(Long id){
		return userRepository.findOne(id);
	}

	@Override
	public List<AppUser> findAll() {
		return (List<AppUser>) userRepository.findAll();
	}

	@Override
	@ModifyingTransactionalServiceMethod
	public void create(AppUser user) {
		userRepository.save(user);
	}

	@Override
	@ModifyingTransactionalServiceMethod
	public void update(AppUser user) {
		userRepository.save(user);
	}

	@Override
	@ModifyingTransactionalServiceMethod
	public void delete(AppUser user) {
		userRepository.delete(user);
	}
	
	@Override
	public AppUser findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
