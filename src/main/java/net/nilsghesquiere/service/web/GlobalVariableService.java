package net.nilsghesquiere.service.web;

import net.nilsghesquiere.entities.GlobalVariable;

public interface GlobalVariableService {
	GlobalVariable read(Long id);
	GlobalVariable create(GlobalVariable globalVariable);
	GlobalVariable update(GlobalVariable globalVariable);
	void deleteById(Long id);
	GlobalVariable getByName(String name);
}
