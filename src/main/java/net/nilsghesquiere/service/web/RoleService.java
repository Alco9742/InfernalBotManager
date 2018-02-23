package net.nilsghesquiere.service.web;

import java.util.List;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.Role;
import net.nilsghesquiere.persistence.dao.RoleRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoleService implements IRoleService{
	private final RoleRepository roleRepository;
	
	@Autowired
	public RoleService(RoleRepository roleRepository){
		this.roleRepository = roleRepository;
	}
	
	public Role read(Long id){
		return roleRepository.findOne(id);
	}

	@Override
	public List<Role> findAll() {
		return (List<Role>) roleRepository.findAll();
	}

	@Override
	@ModifyingTransactionalServiceMethod
	public void create(Role role) {
		roleRepository.save(role);
	}

	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(name);
	}
}
