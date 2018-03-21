package net.nilsghesquiere.service.web;

import java.util.List;

import net.nilsghesquiere.entities.Privilege;
import net.nilsghesquiere.entities.Role;

public interface PrivilegeService {
	Privilege read(Long id);
	List<Privilege> findAll();
	void create(Privilege role);
	Privilege findByName(String name);
}
