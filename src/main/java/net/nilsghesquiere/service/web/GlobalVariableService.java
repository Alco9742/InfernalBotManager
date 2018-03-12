package net.nilsghesquiere.service.web;

import java.util.List;

import net.nilsghesquiere.entities.GlobalVariable;

public interface GlobalVariableService {
	GlobalVariable read(Long id);
	GlobalVariable create(GlobalVariable globalVariable);
	GlobalVariable update(GlobalVariable globalVariable);
	void deleteById(Long id);
	GlobalVariable findByName(String name);
	List<GlobalVariable> findAll();
	void delete(GlobalVariable globalVariable);
}
