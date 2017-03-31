package net.nilsghesquiere.services;

import java.util.List;

import net.nilsghesquiere.entities.AppUser;
import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.repositories.LolAccountRepository;

import org.springframework.beans.factory.annotation.Autowired;

@ReadOnlyTransactionalService
public class LolAccountServiceImpl implements LolAccountService{
	private final LolAccountRepository lolAccountRepository;
	
	@Autowired
	public LolAccountServiceImpl(LolAccountRepository lolAccountRepository){
		this.lolAccountRepository = lolAccountRepository;
	}
	
	public LolAccount read(Long id){
		return lolAccountRepository.findOne(id);
	}

	@Override
	public List<LolAccount> findAll() {
		return (List<LolAccount>) lolAccountRepository.findAll();
	}

	@Override
	public List<LolAccount> findByUser(AppUser user) {
		return lolAccountRepository.findByUser(user);
	}

	@Override
	@ModifyingTransactionalServiceMethod
	public void create(LolAccount lolAccount) {
		lolAccountRepository.save(lolAccount);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	public void update(LolAccount lolAccount) {
		lolAccountRepository.save(lolAccount);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	public void delete(LolAccount lolAccount) {
		lolAccountRepository.delete(lolAccount);
	}
}
