package net.nilsghesquiere.service.web;

import java.util.List;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.ClientData;
import net.nilsghesquiere.persistence.dao.ClientDataRepository;
import net.nilsghesquiere.persistence.dao.QueuerLolAccountRepository;
import net.nilsghesquiere.persistence.dao.QueuerRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Transactional
@SuppressWarnings("unused")
public class ClientDataServiceImpl implements ClientDataService{
	private final ClientDataRepository clientDataRepository;
	private final QueuerRepository queuerRepository;
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
		/*
		// Setting the relationships
		for(Queuer queuer: clientData.getQueuers()){
			queuer.setClientData(clientData);
			for(QueuerLolAccount lolacc : queuer.getQueuerLolAccounts()){
				lolacc.setQueuer(queuer);
			}
		}
		// Saving the data
		 * 
		 */
		return clientDataRepository.save(clientData);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ClientData update(ClientData clientData) {
		// Currently have to do this customly
		// Deleting the current data --> delete the queuers containing the lolAccounts, keep the ClientData 
		//TODO rework 
		/*
	//	ClientData oldData = clientDataRepository.findByTagAndUserId(clientData.getTag(), clientData.getUser().getId());
	//	oldData.getQueuers().stream().forEach(queuer -> queuerRepository.delete(queuer));
		// Setting the relationships
		for(Queuer queuer: clientData.getQueuers()){
			queuer.setClientData(clientData);
			for(QueuerLolAccount lolacc : queuer.getQueuerLolAccounts()){
				lolacc.setQueuer(queuer);
			}
		}
		// Saving the data
		*/
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
	public List<ClientData> findByClientUserId(Long userid) {
		return clientDataRepository.findByClientUserId(userid);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ClientData findByClientId(Long clientId) {
		return clientDataRepository.findByClientId(clientId);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public long countActiveQueuers() {
		long count = 0L;
		List<ClientData> allClientDatas = clientDataRepository.findAll();
		for (ClientData data : allClientDatas){
			count += data.getQueuers().size();
		}
		return count;
	}
}
