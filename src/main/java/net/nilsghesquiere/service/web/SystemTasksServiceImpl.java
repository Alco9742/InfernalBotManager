package net.nilsghesquiere.service.web;

import java.util.List;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.Client;
import net.nilsghesquiere.entities.GlobalVariable;
import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.entities.Privilege;
import net.nilsghesquiere.entities.Role;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.UserSettings;
import net.nilsghesquiere.persistence.dao.ClientDataRepository;
import net.nilsghesquiere.persistence.dao.ClientRepository;
import net.nilsghesquiere.persistence.dao.GlobalVariableRepository;
import net.nilsghesquiere.persistence.dao.LolAccountRepository;
import net.nilsghesquiere.persistence.dao.PrivilegeRepository;
import net.nilsghesquiere.persistence.dao.RoleRepository;
import net.nilsghesquiere.persistence.dao.UserRepository;
import net.nilsghesquiere.persistence.dao.UserSettingsRepository;
import net.nilsghesquiere.util.enums.AccountStatus;
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
	
	@Autowired
	private PrivilegeRepository privilegeRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private GlobalVariableRepository globalVariableRepository;
	
	@Autowired
	private LolAccountRepository lolAccountRepository;
	
	@Override
	public List<Client> findClientsByClientStatus(ClientStatus status) {
		return clientRepository.findByClientStatus(status);
	}

	@Override
	public void setClientsAsDisconnected(List<Client> clients) {
		for(Client client : clients){
			client.setError(true);
			client.setClientStatus(ClientStatus.DISCONNECTED);
			clientRepository.save(client);
		}
	}
	
	@Override
	public void setClientsDcMailSent(List<Client> clients) {
		for(Client client : clients){
			client.setDcMailSent(true);
			clientRepository.save(client);
		}
	}
	
	@Override
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
		return userRepository.getUserByClients(client);
	}

	@Override
	public void createPrivilege(Privilege privilege) {
		privilegeRepository.save(privilege);
	}

	@Override
	public Privilege findPrivilegeByName(String name) {
		return privilegeRepository.findByName(name);
	}

	@Override
	public Role findRoleByName(String name) {
		return roleRepository.findByName(name);
	}

	@Override
	public void createRole(Role role) {
		roleRepository.save(role);
	}

	@Override
	public GlobalVariable findGlobalVariableByName(String name) {
		return globalVariableRepository.findByName(name);
	}

	@Override
	public void createGlobalVariable(GlobalVariable var) {
		globalVariableRepository.save(var);
	}
	
	@Override
	public void setAllInUseAccountsToReadyForUse() {
		for (LolAccount lolAccount : lolAccountRepository.findAll()){
			if (lolAccount.getAccountStatus().equals(AccountStatus.IN_USE) || lolAccount.getAccountStatus().equals(AccountStatus.IN_BUFFER)){
				lolAccount.setAccountStatus(AccountStatus.READY_FOR_USE);
				lolAccount.setAssignedTo("");
				lolAccountRepository.save(lolAccount);
			}
		}
	}
	
	@Override 
	public void createUserSettingsIfNotExisting(){
		List<User> users = userRepository.findAll();
		for (User user : users){
			if(user.getUserSettings() == null){
				user.setUserSettings(new UserSettings());
				userRepository.save(user);
			}
		}
	}
}
