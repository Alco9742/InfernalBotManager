package net.nilsghesquiere.rest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.nilsghesquiere.entities.AppUser;
import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.exceptions.AccountAlreadyExistsException;
import net.nilsghesquiere.exceptions.AccountNotFoundException;
import net.nilsghesquiere.exceptions.UserNotFoundException;
import net.nilsghesquiere.services.LolAccountService;
import net.nilsghesquiere.services.UserService;
import net.nilsghesquiere.valueobjects.JSONWrapper;
import net.nilsghesquiere.valueobjects.LolAccountListWrapper;
import net.nilsghesquiere.valueobjects.LolAccountMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Preconditions;

@RestController
@RequestMapping("/api/accounts")
public class AccountsRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountsRestController.class);
	private final LolAccountService lolAccountService;
	private final UserService userService;
	
	@Autowired
	AccountsRestController(LolAccountService lolAccountService,UserService userService){
		this.lolAccountService = lolAccountService;
		this.userService = userService;
	}
	
	@RequestMapping(path = "/{accountid}", method = RequestMethod.GET)
	public ResponseEntity<LolAccountListWrapper> findAccountById(@PathVariable Long accountid) {
		LolAccountListWrapper wrapper = new LolAccountListWrapper();
		List<LolAccount> lolAccounts = new ArrayList<>();
		validateAccountById(accountid);
		LolAccount lolAccount = lolAccountService.read(accountid);
		lolAccounts.add(lolAccount);
		wrapper.add("data", lolAccounts);
		return new ResponseEntity<LolAccountListWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/{accountid}",method = RequestMethod.PUT)
	public ResponseEntity<LolAccountListWrapper> update(@PathVariable Long accountid,@RequestBody LolAccount lolAccount) {
		LolAccountListWrapper wrapper = new LolAccountListWrapper();
		List<LolAccount> updatedLolAccounts = new ArrayList<>();
		Preconditions.checkNotNull(lolAccount);
		validateAccountById(accountid);
		if (accountid == lolAccount.getId()){
			lolAccount.setUser(userService.read(lolAccountService.read(accountid).getUser().getId()));
			LolAccount updatedLolAccount = lolAccountService.update(lolAccount);
			updatedLolAccounts.add(updatedLolAccount);
		}
		wrapper.add("data", updatedLolAccounts);
		return new ResponseEntity<LolAccountListWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}", method = RequestMethod.GET)
	public ResponseEntity<LolAccountListWrapper> findAccountsByUserId(@PathVariable Long userid) {
		LolAccountListWrapper wrapper = new LolAccountListWrapper();
		validateUserByUserId(userid);
		List<LolAccount> lolAccounts = lolAccountService.findByUserId(userid);
		wrapper.add("data",lolAccounts);
		return new ResponseEntity<LolAccountListWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}", method = RequestMethod.POST)
	public ResponseEntity<LolAccountListWrapper> create(@PathVariable Long userid, @RequestBody LolAccountMap lolAccountMap) {
		LolAccountListWrapper wrapper = new LolAccountListWrapper();
		List<LolAccount> returnAccounts = new ArrayList<>();
		for(LolAccount lolAccount: lolAccountMap.getMap().values()){
			Preconditions.checkNotNull(lolAccount);
			validateUserByUserId(userid);
			AppUser user = userService.read(userid);
			LolAccount newAccount = new LolAccount(user,lolAccount.getUsername(),lolAccount.getPassword(),lolAccount.getMaxlevel(), lolAccount.isEnabled());	
			LolAccount createdAccount = lolAccountService.create(newAccount);
			returnAccounts.add(createdAccount);
		}
		wrapper.add("data",returnAccounts);
		return new ResponseEntity<LolAccountListWrapper>(wrapper,HttpStatus.CREATED);
	}
	
	@RequestMapping(path = "/user/{userid}",method = RequestMethod.PUT)
	public ResponseEntity<LolAccountListWrapper> update(@PathVariable Long userid,@RequestBody LolAccountMap lolAccountMap) {
		LolAccountListWrapper wrapper = new LolAccountListWrapper();
		List<LolAccount> returnAccounts = new ArrayList<>();
		for (LolAccount lolAccount : lolAccountMap.getMap().values()){
			Preconditions.checkNotNull(lolAccount);
			validateAccountById(lolAccount.getId());
			validateUserByUserId(userid);
			lolAccount.setUser(userService.read(userid));
			LolAccount updatedLolAccount = lolAccountService.update(lolAccount);
			returnAccounts.add(updatedLolAccount);
		}
		wrapper.add("data",returnAccounts);
		return new ResponseEntity<LolAccountListWrapper>(wrapper,HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}/delete",method = RequestMethod.POST)
	public ResponseEntity<LolAccountListWrapper> delete(@PathVariable Long userid, @RequestBody LolAccountMap lolAccountMap) {
		LolAccountListWrapper wrapper = new LolAccountListWrapper();
		List<LolAccount> deletedAccounts = new ArrayList<>();
		for (LolAccount lolAccount : lolAccountMap.getMap().values()){
			LOGGER.info("Delete: " + lolAccount);
			validateAccountById(lolAccount.getId());
			validateUserByUserId(userid);
			lolAccountService.delete(lolAccount);
			deletedAccounts.add(lolAccount);
		}
		wrapper.add("data",deletedAccounts);
		return new ResponseEntity<LolAccountListWrapper>(wrapper,HttpStatus.OK);
	}
	
	@RequestMapping(value="/user/{userid}/import", method=RequestMethod.POST)
	public void processUpload(@RequestParam MultipartFile file) throws IOException {
		LOGGER.info("test");
	}
	private void validateUserByUserId(Long userId) {
		userService.findByUserId(userId).orElseThrow(
			() -> new UserNotFoundException(userId));
	}

	private void validateNewAccountById(Long accountId) {
		if(lolAccountService.findById(accountId).isPresent()){
			throw new AccountAlreadyExistsException(accountId);
		}
	}

	private void validateAccountById(Long accountId) {
		lolAccountService.findById(accountId).orElseThrow(
			() -> new AccountNotFoundException(accountId));
	}
}
