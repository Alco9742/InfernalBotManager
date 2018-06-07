package net.nilsghesquiere.service.web;

import java.util.List;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.Client;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.UserSettings;
import net.nilsghesquiere.persistence.dao.ClientDataRepository;
import net.nilsghesquiere.persistence.dao.ClientRepository;
import net.nilsghesquiere.persistence.dao.UserRepository;
import net.nilsghesquiere.persistence.dao.UserSettingsRepository;
import net.nilsghesquiere.util.enums.ClientStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SystemTasksServiceImpl implements SystemTasksService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private ClientDataRepository clientDataRepository;
	
	@Autowired
	private UserSettingsRepository userSettingsRepository;
	
	@Override
	//@PreAuthorize("hasRole('ROLE_SYSTEM')")
	public List<Client> findClientsByClientStatus(ClientStatus status) {
		return clientRepository.findByClientStatus(status);
	}

	@Override
	//@PreAuthorize("hasRole('ROLE_SYSTEM')")
	public void setClientsAsDisconnected(List<Client> clients) {
		for(Client client : clients){
			client.setError(true);
			client.setClientStatus(ClientStatus.DISCONNECTED);
			clientRepository.save(client);
		}
	}
	
	@Override
	//@PreAuthorize("hasRole('ROLE_SYSTEM')")
	public void setClientsDcMailSent(List<Client> clients) {
		for(Client client : clients){
			client.setDcMailSent(true);
			clientRepository.save(client);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ROLE_SYSTEM')")
	public void setAllClientsAsOffline() {
		List<Client> clients = clientRepository.findAll();
		for (Client client : clients){
			if (client.getClientData() != null){
				clientDataRepository.deleteById(client.getClientData().getId());
			}
			client.setClientData(null);
			client.setLastPing(null);
			client.setClientStatus(ClientStatus.OFFLINE);
			clientRepository.save(client);
		}
	}

	@Override
	public UserSettings getUserSettingsByUser(User user) {
		return userSettingsRepository.getByUser(user);
	}

	@Override
	public User getUserByClient(Client client) {
		// TODO Auto-generated method stub
		return userRepository.getUserByClients(client);
	}
}
