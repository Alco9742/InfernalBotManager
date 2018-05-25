package net.nilsghesquiere.service.web;

import java.util.List;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.ImportSettings;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.persistence.dao.ImportSettingsRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ImportSettingsServiceImpl implements ImportSettingsService{
	private final ImportSettingsRepository importSettingsRepository;
	
	@Autowired
	public ImportSettingsServiceImpl(ImportSettingsRepository importSettingsRepository){
		this.importSettingsRepository = importSettingsRepository;
	}

	@Override
	public ImportSettings read(Long id) {
		return importSettingsRepository.findOne(id);
	}

	@Override
	@ModifyingTransactionalServiceMethod
	public ImportSettings create(ImportSettings importSettings) {
		return importSettingsRepository.save(importSettings);
	}

	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ImportSettings update(ImportSettings importSettings) {
		return importSettingsRepository.save(importSettings);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public void delete(ImportSettings importSettings) {
		importSettingsRepository.delete(importSettings);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public List<ImportSettings> findByUser(User user) {
		return importSettingsRepository.findByUser(user);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public List<ImportSettings> findByUserId(Long userId) {
		return importSettingsRepository.findByUserId(userId);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public void deleteById(Long id) {
		importSettingsRepository.deleteById(id);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ImportSettings findByUserIdAndName(Long userid, String name) {
		return importSettingsRepository.findByUserIdAndName(userid, name);
	}
}
