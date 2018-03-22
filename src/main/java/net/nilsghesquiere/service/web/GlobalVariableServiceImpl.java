package net.nilsghesquiere.service.web;

import java.util.List;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.GlobalVariable;
import net.nilsghesquiere.persistence.dao.GlobalVariableRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class GlobalVariableServiceImpl implements GlobalVariableService{
	private final GlobalVariableRepository globalVariableRepository;
	
	@Autowired
	public GlobalVariableServiceImpl(GlobalVariableRepository globalVariableRepository){
		this.globalVariableRepository = globalVariableRepository;
	}
	
	public GlobalVariable read(Long id){
		return globalVariableRepository.findOne(id);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public GlobalVariable create(GlobalVariable globalVariable) {
		return globalVariableRepository.save(globalVariable);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public GlobalVariable update(GlobalVariable globalVariable) {
		return globalVariableRepository.save(globalVariable);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteById(Long id) {
		globalVariableRepository.deleteById(id);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public GlobalVariable findByName(String name) {
		return globalVariableRepository.findByName(name);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<GlobalVariable> findAll() {
		return globalVariableRepository.findAll();
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void delete(GlobalVariable globalVariable) {
		globalVariableRepository.delete(globalVariable);
	}
}
