package net.nilsghesquiere.service.web;

import java.util.List;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.persistence.dao.InfernalSettingsRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public InfernalSettings update(InfernalSettings infernalSettings) {
		return infernalSettingsRepository.save(infernalSettings);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public void deleteById(Long id) {
		infernalSettingsRepository.deleteById(id);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public InfernalSettings getByUserId(Long userid) {
		return infernalSettingsRepository.getByUserId(userid);
	}
	
	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public List<InfernalSettings> findByUser(User user) {
		return infernalSettingsRepository.findByUser(user);
	}
	
	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public InfernalSettings findByUserIdAndSets(Long userid, String sets) {
		return infernalSettingsRepository.findByUserIdAndSets(userid, sets);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public void delete(InfernalSettings infernalSettings) {
		infernalSettingsRepository.delete(infernalSettings);
	}
}
