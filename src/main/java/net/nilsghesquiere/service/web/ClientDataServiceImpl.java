package net.nilsghesquiere.service.web;

import java.util.List;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.ClientData;
import net.nilsghesquiere.entities.Queuer;
import net.nilsghesquiere.entities.QueuerLolAccount;
import net.nilsghesquiere.persistence.dao.ClientDataRepository;
import net.nilsghesquiere.persistence.dao.ClientStatusRepository;
import net.nilsghesquiere.persistence.dao.LolAccountRepository;
import net.nilsghesquiere.persistence.dao.QueuerLolAccountRepository;
import net.nilsghesquiere.persistence.dao.QueuerRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ClientDataServiceImpl implements ClientDataService{
	private final ClientDataRepository clientDataRepository;
	private final QueuerRepository queuerRepository;
	private final QueuerLolAccountRepository queuerLolAccountRepository;
	private final ClientStatusRepository clientStatusRepository;
	
	@Autowired
	public ClientDataServiceImpl(ClientDataRepository clientDataRepository,QueuerRepository queuerRepository, QueuerLolAccountRepository queuerLolAccountRepository,ClientStatusRepository clientStatusRepository){
		this.clientDataRepository = clientDataRepository;
		this.queuerRepository = queuerRepository;
		this.queuerLolAccountRepository = queuerLolAccountRepository;
		this.clientStatusRepository = clientStatusRepository;
	}
	
	public ClientData read(Long id){
		return clientDataRepository.findOne(id);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	public ClientData create(ClientData clientData) {
		ClientData newData = clientDataRepository.save(clientData);
		for (Queuer queuer : newData.getQueuers()){
			queuer.setClient(newData);
			Queuer newQueuer = queuerRepository.save(queuer);
			for(QueuerLolAccount lolAccount : newQueuer.getQueuerAccounts()){
				lolAccount.setQueuer(newQueuer);
				queuerLolAccountRepository.save(lolAccount);
			}
		}
		return clientDataRepository.findOne(newData.getId());
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	public ClientData update(ClientData clientData) {
		return clientDataRepository.save(clientData);
	}

	@Override
	public void deleteById(Long id) {
		clientDataRepository.deleteById(id);
	}

	@Override
	public void delete(ClientData clientData) {
		clientDataRepository.delete(clientData);
	}

	@Override
	public List<ClientData> findByUserId(Long userid) {
		return clientDataRepository.findByUserId(userid);
	}

	@Override
	public ClientData findByTagAndUserId(String tag, Long userid) {
		return clientDataRepository.findByTagAndUserId(tag,userid);
	}
}
