package net.nilsghesquiere.service.web;

import java.util.List;

import net.nilsghesquiere.entities.Role;

public interface IRoleService {
	Role read(Long id);
	List<Role> findAll();
	void create(Role role);
	Role findByName(String name);
}
