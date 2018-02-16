package net.nilsghesquiere.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
	public Optional<LolAccount> findById(Long id) {
		return lolAccountRepository.findById(id);
	}

	@Override
	public List<LolAccount> findAll() {
		return lolAccountRepository.findAll();
	}

	@Override
	public List<LolAccount> findByUser(AppUser user) {
		return lolAccountRepository.findByUser(user);
	}
	
	@Override
	public List<LolAccount> findByUserId(Long userId) {
		return lolAccountRepository.findByUserId(userId);
	}

	@Override
	public List<LolAccount> findByUserUsername(String username) {
		return lolAccountRepository.findByUserUsername(username);
	}

	@Override
	@ModifyingTransactionalServiceMethod
	public LolAccount create(LolAccount lolAccount) {
		return lolAccountRepository.save(lolAccount);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	public LolAccount update(LolAccount lolAccount) {
		return lolAccountRepository.save(lolAccount);
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	public void delete(LolAccount lolAccount) {
		lolAccountRepository.delete(lolAccount);
	}

	@Override
	public void deleteById(Long id) {
		lolAccountRepository.deleteById(id);
	}
}
