package net.nilsghesquiere.services;

import java.util.List;
import java.util.Optional;

import net.nilsghesquiere.entities.AppUser;

public interface UserService {
	AppUser read(Long id);
	List<AppUser> findAll();
	void create(AppUser user);
	void update(AppUser user);
	void delete(AppUser user);
	Optional<AppUser> findByUsername(String username);
	Optional<AppUser> findByUserId(Long userId);
}
