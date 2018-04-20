package net.nilsghesquiere.service.web;

import java.util.List;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.ClientSettings;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.persistence.dao.ClientSettingsRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ClientSettingsServiceImpl implements ClientSettingsService{
	private final ClientSettingsRepository clientSettingsRepository;
	
	@Autowired
	public ClientSettingsServiceImpl(ClientSettingsRepository clientSettingsRepository){
		this.clientSettingsRepository = clientSettingsRepository;
	}

	@Override
	public ClientSettings read(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@ModifyingTransactionalServiceMethod
	public ClientSettings create(ClientSettings clientSettings) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public void delete(ClientSettings clientSettings) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ClientSettings update(ClientSettings clientSettings) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public List<ClientSettings> findByUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public List<ClientSettings> findByUserId(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}
}
