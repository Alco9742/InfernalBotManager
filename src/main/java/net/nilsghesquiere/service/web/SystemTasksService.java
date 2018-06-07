package net.nilsghesquiere.service.web;

import java.util.List;

import net.nilsghesquiere.entities.Client;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.UserSettings;
import net.nilsghesquiere.util.enums.ClientStatus;

public interface SystemTasksService {

	List<Client> findClientsByClientStatus(ClientStatus status);

	void setClientsAsDisconnected(List<Client> clients);

	void setClientsDcMailSent(List<Client> clients);

	void setAllClientsAsOffline();
	
	User getUserByClient(Client client);

	UserSettings getUserSettingsByUser(User user);
	
}
