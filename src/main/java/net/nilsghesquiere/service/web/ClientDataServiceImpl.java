package net.nilsghesquiere.service.web;

import java.util.List;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.ClientData;
import net.nilsghesquiere.entities.Queuer;
import net.nilsghesquiere.entities.QueuerLolAccount;
import net.nilsghesquiere.persistence.dao.ClientDataRepository;
import net.nilsghesquiere.persistence.dao.QueuerLolAccountRepository;
import net.nilsghesquiere.persistence.dao.QueuerRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ClientDataServiceImpl implements ClientDataService{
	private final ClientDataRepository clientDataRepository;
	@SuppressWarnings("unused")
	private final QueuerRepository queuerRepository;
	@SuppressWarnings("unused")
	private final QueuerLolAccountRepository queuerLolAccountRepository;
	
	@Autowired
	public ClientDataServiceImpl(ClientDataRepository clientDataRepository,QueuerRepository queuerRepository, QueuerLolAccountRepository queuerLolAccountRepository){
		this.clientDataRepository = clientDataRepository;
		this.queuerRepository = queuerRepository;
		this.queuerLolAccountRepository = queuerLolAccountRepository;
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ClientData read(Long id){
		return clientDataRepository.findOne(id);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ClientData create(ClientData clientData) {
		// Setting the relationships
		for(Queuer queuer: clientData.getQueuers()){
			queuer.setClient(clientData);
			for(QueuerLolAccount lolacc : queuer.getQueuerLolAccounts()){
				lolacc.setQueuer(queuer);
			}
		}
		// Saving the data
		return clientDataRepository.save(clientData);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ClientData update(ClientData clientData) {
		// Deleting the current data
		ClientData dataToDelete = clientDataRepository.findByTagAndUserId(clientData.getTag(), clientData.getUser().getId());
		clientDataRepository.delete(dataToDelete);
		// Setting the relationships
		for(Queuer queuer: clientData.getQueuers()){
			queuer.setClient(clientData);
			for(QueuerLolAccount lolacc : queuer.getQueuerLolAccounts()){
				lolacc.setQueuer(queuer);
			}
		}
		
		// Saving the data
		return clientDataRepository.save(clientData);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public void deleteById(Long id) {
		clientDataRepository.deleteById(id);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public void delete(ClientData clientData) {
		clientDataRepository.delete(clientData);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public List<ClientData> findByUserId(Long userid) {
		return clientDataRepository.findByUserId(userid);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ClientData findByTagAndUserId(String tag, Long userid) {
		return clientDataRepository.findByTagAndUserId(tag,userid);
	}
}
