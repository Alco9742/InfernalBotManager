package net.nilsghesquiere.service.web;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.persistence.dao.InfernalSettingsRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;

@Service
@Transactional
public class InfernalSettingsService implements IInfernalSettingsService{
	private final InfernalSettingsRepository infernalSettingsRepository;
	
	@Autowired
	public InfernalSettingsService(InfernalSettingsRepository infernalSettingsRepository){
		this.infernalSettingsRepository = infernalSettingsRepository;
	}
	
	public InfernalSettings read(Long id){
		return infernalSettingsRepository.findOne(id);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	public InfernalSettings create(InfernalSettings infernalSettings) {
		return infernalSettingsRepository.save(infernalSettings);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	public InfernalSettings update(InfernalSettings infernalSettings) {
		return infernalSettingsRepository.save(infernalSettings);
	}

	@Override
	public void deleteById(Long id) {
		infernalSettingsRepository.deleteById(id);
	}
}
