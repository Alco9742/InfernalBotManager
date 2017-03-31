package net.nilsghesquiere.services;

import java.util.List;

import net.nilsghesquiere.entities.Role;
import net.nilsghesquiere.repositories.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ReadOnlyTransactionalService
public class RoleServiceImpl implements RoleService{
	private final RoleRepository roleRepository;
	
	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository){
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
}
