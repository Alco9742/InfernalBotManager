package net.nilsghesquiere.service.web;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.persistence.dao.InfernalSettingsRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class InfernalSettingsServiceImpl implements InfernalSettingsService{
	private final InfernalSettingsRepository infernalSettingsRepository;
	
	@Autowired
	public InfernalSettingsServiceImpl(InfernalSettingsRepository infernalSettingsRepository){
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

	@Override
	public InfernalSettings getByUserId(Long userid) {
		return infernalSettingsRepository.getByUserId(userid);
	}
}
