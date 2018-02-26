package net.nilsghesquiere.service.web;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.persistence.dao.LolAccountRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LolAccountService implements ILolAccountService{
	private final LolAccountRepository lolAccountRepository;
	
	@Autowired
	public LolAccountService(LolAccountRepository lolAccountRepository){
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
	public List<LolAccount> findByUser(User user) {
		return lolAccountRepository.findByUser(user);
	}
	
	@Override
	public List<LolAccount> findByUserId(Long userId) {
		return lolAccountRepository.findByUserId(userId);
	}

	@Override
	public List<LolAccount> findByUserEmail(String email) {
		return lolAccountRepository.findByUserEmail(email);
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
