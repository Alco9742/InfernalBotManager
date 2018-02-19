package net.nilsghesquiere.restservices;

import java.util.ArrayList;
import java.util.List;

import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.exceptions.AccountAlreadyExistsException;
import net.nilsghesquiere.exceptions.AccountNotFoundException;
import net.nilsghesquiere.exceptions.UserNotFoundException;
import net.nilsghesquiere.services.LolAccountService;
import net.nilsghesquiere.services.UserService;
import net.nilsghesquiere.valueobjects.JSONWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<LolAccount> findAccountById(@PathVariable Long accountid) {
		validateAccountById(accountid);
		LolAccount lolAccount = lolAccountService.read(accountid);
		return new ResponseEntity<LolAccount>(lolAccount, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/{accountid}",method = RequestMethod.PUT)
	public ResponseEntity<LolAccount> update(@PathVariable Long accountid,@RequestBody LolAccount lolAccount) {
		LOGGER.info("Edit: " + lolAccount);
		Preconditions.checkNotNull(lolAccount);
		validateAccountById(accountid);
		if (accountid == lolAccount.getId()){
			lolAccount.setUser(userService.read(lolAccountService.read(accountid).getUser().getId()));
			LolAccount updatedLolAccount = lolAccountService.update(lolAccount);
			return new ResponseEntity<LolAccount>(updatedLolAccount,HttpStatus.OK);
		} else {
			return new ResponseEntity<LolAccount>(lolAccount, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(path = "/user/{userid}", method = RequestMethod.GET)
	public ResponseEntity<JSONWrapper> findAccountsByUserId(@PathVariable Long userid) {
		JSONWrapper wrapper = new JSONWrapper();
		validateUserByUserId(userid);
		List<LolAccount> lolAccount = lolAccountService.findByUserId(userid);
		wrapper.add("data",lolAccount);
		return new ResponseEntity<JSONWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}", method = RequestMethod.POST)
	public ResponseEntity<JSONWrapper> create(@PathVariable Long userid, @RequestBody LolAccount lolAccount) {
		JSONWrapper wrapper = new JSONWrapper();
		Preconditions.checkNotNull(lolAccount);
		validateUserByUserId(userid);
		//TODO werkende manier vinden om update tegen te houden
		//validateNewAccountById(lolAccount.getId());
		lolAccount.setUser(userService.read(userid));
		//has to be in an array for datatables refresh
		LolAccount[] newLolAccountArray = {lolAccountService.create(lolAccount)};
		wrapper.add("data",newLolAccountArray);
		return new ResponseEntity<JSONWrapper>(wrapper,HttpStatus.CREATED);
	}
	
	@RequestMapping(path = "/user/{userid}",method = RequestMethod.PUT)
	public ResponseEntity<JSONWrapper> update(@PathVariable Long userid,@RequestBody LolAccount[] lolAccounts) {
		JSONWrapper wrapper = new JSONWrapper();
		List<LolAccount> returnAccounts = new ArrayList<>();
		for (LolAccount lolAccount : lolAccounts){
			LOGGER.info("Edit: " + lolAccount);
			Preconditions.checkNotNull(lolAccount);
			validateAccountById(lolAccount.getId());
			validateUserByUserId(userid);
			lolAccount.setUser(userService.read(userid));
			LolAccount updatedLolAccount = lolAccountService.update(lolAccount);
			returnAccounts.add(updatedLolAccount);
		}
		LolAccount[] responseAccounts = returnAccounts.toArray(new LolAccount[returnAccounts.size()]);
		wrapper.add("data",responseAccounts);
		return new ResponseEntity<JSONWrapper>(wrapper,HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}/delete",method = RequestMethod.POST)
	public ResponseEntity<JSONWrapper> delete(@PathVariable Long userid, @RequestBody LolAccount[] lolAccounts) {
		JSONWrapper wrapper = new JSONWrapper();
		List<LolAccount> deletedAccounts = new ArrayList<>();
		for (LolAccount lolAccount : lolAccounts){
			LOGGER.info("Delete: " + lolAccount);
			validateAccountById(lolAccount.getId());
			validateUserByUserId(userid);
			lolAccountService.delete(lolAccount);
			deletedAccounts.add(lolAccount);
		}
		LolAccount[] responseAccounts = deletedAccounts.toArray(new LolAccount[deletedAccounts.size()]);
		wrapper.add("data",responseAccounts);
		return new ResponseEntity<JSONWrapper>(wrapper,HttpStatus.OK);
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
