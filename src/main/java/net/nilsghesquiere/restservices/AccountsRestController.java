package net.nilsghesquiere.restservices;

import java.util.List;

import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.preconditions.RestPreconditions;
import net.nilsghesquiere.services.LolAccountService;
import net.nilsghesquiere.services.UserService;
import net.nilsghesquiere.exceptions.UserNotFoundException;
import net.nilsghesquiere.exceptions.AccountNotFoundException;
import net.nilsghesquiere.exceptions.AccountAlreadyExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Preconditions;

@RestController
@RequestMapping("/api/accounts")
public class AccountsRestController {

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
	
	@RequestMapping(path = "/user/{userid}", method = RequestMethod.GET)
	public ResponseEntity<List<LolAccount>> findAccountsByUserId(@PathVariable Long userid) {
		validateUserByUserId(userid);
		List<LolAccount> lolAccount = lolAccountService.findByUserId(userid);
		return new ResponseEntity<List <LolAccount>>(lolAccount, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}", method = RequestMethod.PUT)
	public ResponseEntity<LolAccount> create(@PathVariable Long userid, @RequestBody LolAccount lolAccount) {
		Preconditions.checkNotNull(lolAccount);
		validateUserByUserId(userid);
		//TODO werkende manier vinden om update tegen te houden
		//validateNewAccountById(lolAccount.getId());
		lolAccount.setUser(userService.read(userid));
		LolAccount newLolAccount = lolAccountService.create(lolAccount);
		return new ResponseEntity<LolAccount>(newLolAccount,HttpStatus.CREATED);
	}
	
	@RequestMapping(path = "/user/{userid}",method = RequestMethod.POST)
	public ResponseEntity<LolAccount> update(@PathVariable Long userid,@RequestBody LolAccount lolAccount) {
		Preconditions.checkNotNull(lolAccount);
		validateAccountById(lolAccount.getId());
		validateUserByUserId(userid);
		lolAccount.setUser(userService.read(userid));
		LolAccount updatedLolAccount = lolAccountService.update(lolAccount);
		return new ResponseEntity<LolAccount>(updatedLolAccount,HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}",method = RequestMethod.DELETE)
	public void delete(@PathVariable Long userid, @RequestBody LolAccount lolAccount) {
		validateAccountById(lolAccount.getId());
		validateUserByUserId(userid);
		lolAccountService.delete(lolAccount);
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
