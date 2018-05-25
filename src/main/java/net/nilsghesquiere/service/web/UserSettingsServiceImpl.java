package net.nilsghesquiere.service.web;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.UserSettings;
import net.nilsghesquiere.persistence.dao.UserSettingsRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserSettingsServiceImpl implements UserSettingsService{
	private final UserSettingsRepository userSettingsRepository;
	
	@Autowired
	public UserSettingsServiceImpl(UserSettingsRepository userSettingsRepository){
		this.userSettingsRepository = userSettingsRepository;
	}

	@Override
	public UserSettings read(Long id) {
		return userSettingsRepository.findOne(id);
	}

	@Override
	@ModifyingTransactionalServiceMethod
	public UserSettings create(UserSettings userSettings) {
		return userSettingsRepository.save(userSettings);
	}

	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public UserSettings update(UserSettings userSettings) {
		return userSettingsRepository.save(userSettings);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public void delete(UserSettings userSettings) {
		userSettingsRepository.delete(userSettings);
	}



	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public UserSettings getByUser(User user) {
		return userSettingsRepository.getByUser(user);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public UserSettings getByUserId(Long userId) {
		return userSettingsRepository.getByUserId(userId);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public void deleteById(Long id) {
		userSettingsRepository.deleteById(id);
	}
}
