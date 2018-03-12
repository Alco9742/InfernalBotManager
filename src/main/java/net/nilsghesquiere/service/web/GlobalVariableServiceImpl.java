package net.nilsghesquiere.service.web;

import java.util.List;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.GlobalVariable;
import net.nilsghesquiere.persistence.dao.GlobalVariableRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;

import org.springframework.beans.factory.annotation.Autowired;
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
	public GlobalVariable create(GlobalVariable globalVariable) {
		return globalVariableRepository.save(globalVariable);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	public GlobalVariable update(GlobalVariable globalVariable) {
		return globalVariableRepository.save(globalVariable);
	}

	@Override
	public void deleteById(Long id) {
		globalVariableRepository.deleteById(id);
	}

	@Override
	public GlobalVariable findByName(String name) {
		return globalVariableRepository.findByName(name);
	}

	@Override
	public List<GlobalVariable> findAll() {
		return globalVariableRepository.findAll();
	}

	@Override
	public void delete(GlobalVariable globalVariable) {
		globalVariableRepository.delete(globalVariable);
	}
}
