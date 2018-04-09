package net.nilsghesquiere.service.web;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.persistence.dao.LolAccountRepository;
import net.nilsghesquiere.service.ModifyingTransactionalServiceMethod;
import net.nilsghesquiere.util.enums.AccountStatus;
import net.nilsghesquiere.util.enums.Region;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Transactional
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
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public Optional<LolAccount> findOptionalById(Long id) {
		return Optional.of(lolAccountRepository.findById(id));
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public List<LolAccount> findAll() {
		return lolAccountRepository.findAll();
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public List<LolAccount> findByUser(User user) {
		return lolAccountRepository.findByUser(user);
	}
	
	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public List<LolAccount> findByUserId(Long userId) {
		return lolAccountRepository.findByUserId(userId);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public List<LolAccount> findByUserEmail(String email) {
		return lolAccountRepository.findByUserEmail(email);
	}

	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public LolAccount create(LolAccount lolAccount) {
		LolAccount returnAccount = null;
		//check if account with that name/server combo exists
		LolAccount checkAccount = lolAccountRepository.findByAccountIgnoreCaseAndRegion(lolAccount.getAccount(), lolAccount.getRegion());
		if (checkAccount == null){
			returnAccount = lolAccountRepository.save(lolAccount);
		} 
		return returnAccount;
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public LolAccount update(LolAccount lolAccount) {
		Boolean accountAlreadyExistsOnRegion = false;
		// find the account with that ID in the database
		LolAccount currentDBAccount = lolAccountRepository.findById(lolAccount.getId());
		// if the account with that ID has a different region than the update one (region has been changed)
		//System.out.println("N: " +lolAccount.getRegion() +", DB: " + currentDBAccount.getRegion());
		if (lolAccount.getRegion() != currentDBAccount.getRegion()){
			// search all accounts with that accountname
			List <LolAccount> allAccountsFromDB = lolAccountRepository.findAllByAccountIgnoreCase(lolAccount.getAccount());
			for (LolAccount dbAccount : allAccountsFromDB){
				//System.out.println("N: " +lolAccount.getRegion() +", DB: " + dbAccount.getRegion());
				// if one of those accounts has the same server as the updated account: combination already exists, return null
				if (dbAccount.getRegion() == lolAccount.getRegion()){
					accountAlreadyExistsOnRegion = true;
				}
			}
		}
		if (!accountAlreadyExistsOnRegion){
			return lolAccountRepository.save(lolAccount);
		}
		return null;
	}
	
	@Override
	@ModifyingTransactionalServiceMethod
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public void delete(LolAccount lolAccount) {
		lolAccountRepository.delete(lolAccount);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public void deleteById(Long id) {
		lolAccountRepository.deleteById(id);
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public List<LolAccount> findUsableAccounts(Long userid, Region region, Integer amount) {
		// limit using a Pageable
		Pageable pageable = new PageRequest(0,amount);
		List<LolAccount> lolAccounts = lolAccountRepository.findUsableAccounts(userid, region, pageable);
		//set as IN_USE here so others don't grab the same accounts
		for (LolAccount lolAccount : lolAccounts){
			lolAccount.setAccountStatus(AccountStatus.IN_USE);
			lolAccountRepository.save(lolAccount);
		}
		return lolAccounts;
	}
	
	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public List<LolAccount> findBufferAccounts(Long userid, Region region, Integer amount) {
		// limit using a Pageable
		Pageable pageable = new PageRequest(0,amount);
		List<LolAccount> lolAccounts = lolAccountRepository.findBufferAccounts(userid, region, pageable);
		//set as IN_BUFFER here so others don't grab the same accounts
		for (LolAccount lolAccount : lolAccounts){
			lolAccount.setAccountStatus(AccountStatus.IN_BUFFER);
			lolAccountRepository.save(lolAccount);
		}
		return lolAccounts;
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public LolAccount findByUserIdAndRegionAndAccount(Long userid, Region region, String account) {
		return lolAccountRepository.findByAccountIgnoreCaseAndRegionAndUserId(account, region, userid);
	}

	@Override
	public long countAll() {
		return lolAccountRepository.count();
	}
}
