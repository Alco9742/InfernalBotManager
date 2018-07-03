package net.nilsghesquiere.service.web;

import java.util.List;

import net.nilsghesquiere.entities.Client;
import net.nilsghesquiere.entities.GlobalVariable;
import net.nilsghesquiere.entities.Privilege;
import net.nilsghesquiere.entities.Role;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.UserSettings;
import net.nilsghesquiere.util.enums.ClientStatus;

public interface SystemTasksService {

	List<Client> findClientsByClientStatus(ClientStatus status);

	void setClientsAsDisconnected(List<Client> clients);

	void setClientsDcMailSent(List<Client> clients);

	void setAllClientsAsOffline();
	
	void convertInfernalSettingsIfNeeded();
	
	User getUserByClient(Client client);

	UserSettings getUserSettingsByUser(User user);

	void createPrivilege(Privilege privilege);

	Privilege findPrivilegeByName(String name);

	Role findRoleByName(String name);

	void createRole(Role role);

	GlobalVariable findGlobalVariableByName(String name);

	void createGlobalVariable(GlobalVariable var);

	void setAllInUseAccountsToReadyForUse();

	void createUserSettingsIfNotExisting();
	
}
