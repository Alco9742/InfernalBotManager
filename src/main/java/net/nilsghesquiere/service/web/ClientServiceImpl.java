package net.nilsghesquiere.service.web;

import java.util.List;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.Client;
import net.nilsghesquiere.persistence.dao.ClientRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ClientServiceImpl implements ClientService{
	private final ClientRepository clientRepository;
	
	@Autowired
	public ClientServiceImpl(ClientRepository clientRepository){
		this.clientRepository = clientRepository;
	}
	
	public Client read(Long id){
		return clientRepository.findOne(id);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public Client create(Client client) {
		return clientRepository.save(client);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public Client update(Client client) {
		return clientRepository.save(client);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public void deleteById(Long id) {
		clientRepository.deleteById(id);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public Client getByTag(String tag) {
		return clientRepository.getByTag(tag);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Client> findAll() {
		return clientRepository.findAll();
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public void delete(Client client) {
		clientRepository.delete(client);
	}
	
	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public List<Client> findByUserId(Long userId) {
		return clientRepository.findByUserId(userId);
	}

	@Override
	public Client getByUserIdAndTag(Long userid, String tag) {
		return clientRepository.getByUserIdAndTag(userid, tag);
	}
}
