package net.nilsghesquiere.service.web;

import java.util.List;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.Privilege;
import net.nilsghesquiere.persistence.dao.PrivilegeRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PrivilegeServiceImpl implements PrivilegeService{
	private final PrivilegeRepository privilegeRepository;
	
	@Autowired
	public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository){
		this.privilegeRepository = privilegeRepository;
	}
	
	public Privilege read(Long id){
		return privilegeRepository.findOne(id);
	}

	@Override
	public List<Privilege> findAll() {
		return (List<Privilege>) privilegeRepository.findAll();
	}

	@Override
	@ModifyingTransactionalServiceMethod
	public void create(Privilege privilege) {
		privilegeRepository.save(privilege);
	}

	@Override
	public Privilege findByName(String name) {
		return privilegeRepository.findByName(name);
	}
}
