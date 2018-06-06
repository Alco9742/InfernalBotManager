package net.nilsghesquiere.service.web;

import java.util.List;

import net.nilsghesquiere.entities.Client;
import net.nilsghesquiere.util.enums.ClientStatus;

public interface ClientService {
	Client read(Long id);
	Client create(Client client);
	Client update(Client client);
	void deleteById(Long id);
	Client getByTag(String tag);
	List<Client> findAll();
	void delete(Client client);
	List<Client> findByUserId(Long userid);
	Client getByUserIdAndTag(Long userid, String tag);
	List<Client> findByClientStatus(ClientStatus status);
	void setClientsAsDisconnected(List<Client> clients);
	void setAllClientsAsOffline();
}
