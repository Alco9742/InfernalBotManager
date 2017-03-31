package net.nilsghesquiere.services;

import java.util.List;

import net.nilsghesquiere.entities.AppUser;

public interface UserService {
	AppUser read(Long id);
	List<AppUser> findAll();
	void create(AppUser user);
	void update(AppUser user);
	void delete(AppUser user);
	AppUser findByUsername(String username);
}
