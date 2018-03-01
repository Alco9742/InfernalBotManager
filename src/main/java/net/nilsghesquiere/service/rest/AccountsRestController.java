package net.nilsghesquiere.service.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.LolAccountService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.enums.Region;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.util.wrappers.LolAccountMap;
import net.nilsghesquiere.util.wrappers.LolAccountWrapper;
import net.nilsghesquiere.web.error.AccountNotFoundException;
import net.nilsghesquiere.web.error.UserNotFoundException;

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
	
	@Autowired
	private LolAccountService lolAccountService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationFacade authenticationFacade;	
	
	@RequestMapping(path = "/user/{userid}", method = RequestMethod.GET)
	public ResponseEntity<LolAccountWrapper> findAccountsByUserId(@PathVariable Long userid) {
		//VARS
		LolAccountWrapper wrapper = new LolAccountWrapper();
		String error = "";
		
		//PROCESSING
		List<LolAccount> lolAccounts = lolAccountService.findByUserId(userid);
		
		//RESPONSE
		wrapper.setError(error);
		wrapper.add("data",lolAccounts);
		
		
		//RETURN
		return new ResponseEntity<LolAccountWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}/usable/{region}/limit/{amount}", method = RequestMethod.GET)
	public ResponseEntity<LolAccountWrapper> findUsableAccounts(@PathVariable Long userid, @PathVariable Region region, @PathVariable Integer amount) {
		//VARS
		LolAccountWrapper wrapper = new LolAccountWrapper();
		String error = "";
		
		//PROCESSING
		List<LolAccount> lolAccounts = lolAccountService.findUsableAccounts(userid, region,amount);
		
		//RESPONSE
		wrapper.setError(error);
		wrapper.add("data",lolAccounts);
	
		//RETURN
		return new ResponseEntity<LolAccountWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}", method = RequestMethod.POST)
	public ResponseEntity<LolAccountWrapper> create(@PathVariable Long userid, @RequestBody LolAccountMap lolAccountMap) {
		//VARS
		LolAccountWrapper wrapper = new LolAccountWrapper();
		List<LolAccount> returnAccounts = new ArrayList<>();
		String error = "";
		
		//LOOP (Will always be 1 account for now)
		for(LolAccount lolAccount: lolAccountMap.getMap().values()){
			//CHECKS
			error = checkUser(userid);
			if (lolAccount == null && error.equals("")){
				error = "Account is empty";
			}
			//PROCESSING
			User user = userService.read(userid);
			LolAccount newAccount = new LolAccount(user,lolAccount.getAccount(),lolAccount.getPassword(),lolAccount.getRegion());	
			LolAccount createdAccount = lolAccountService.create(newAccount);
			returnAccounts.add(createdAccount);
		}
		
		//RESPONSE
		wrapper.add("data",returnAccounts);
		wrapper.setError(error);

		//RETURN
		return new ResponseEntity<LolAccountWrapper>(wrapper,HttpStatus.CREATED);
	}
	
	@RequestMapping(path = "/user/{userid}",method = RequestMethod.PUT)
	public ResponseEntity<LolAccountWrapper> update(@PathVariable Long userid,@RequestBody LolAccountMap lolAccountMap) {
		LolAccountWrapper wrapper = new LolAccountWrapper();
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
		return new ResponseEntity<LolAccountWrapper>(wrapper,HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}/delete",method = RequestMethod.POST)
	public ResponseEntity<LolAccountWrapper> delete(@PathVariable Long userid, @RequestBody LolAccountMap lolAccountMap) {
		LolAccountWrapper wrapper = new LolAccountWrapper();
		List<LolAccount> deletedAccounts = new ArrayList<>();
		for (LolAccount lolAccount : lolAccountMap.getMap().values()){
			validateAccountById(lolAccount.getId());
			validateUserByUserId(userid);
			lolAccountService.delete(lolAccount);
			deletedAccounts.add(lolAccount);
		}
		wrapper.add("data",deletedAccounts);
		return new ResponseEntity<LolAccountWrapper>(wrapper,HttpStatus.OK);
	}
	
	@RequestMapping(value="/user/{userid}/import", method=RequestMethod.POST)
	public ResponseEntity<LolAccountWrapper> processUpload(@PathVariable Long userid, @RequestParam MultipartFile file) throws IOException {
		//VARS
		boolean hasError = false;
		String error = "";
		LolAccountWrapper wrapper = new LolAccountWrapper();
		List<LolAccount> importedAccounts = new ArrayList<>();
		List<LolAccount> createdAccounts = new ArrayList<>();
		
		//CHECKS
		validateUserByUserId(userid);
		User user = userService.read(userid);
		
		//PROCESSING
		if (file.getContentType().equals("text/plain")){
			try(Stream<String> stream = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("UTF-8"))).lines()){
				importedAccounts = stream
									.map(line -> LolAccount.buildFromString(user,line))
									.collect(Collectors.toList());
			}
		} else {
			hasError = true;
			error = "uploaded file is not a plain text file!";
		}
		for (LolAccount importedAccount : importedAccounts){
			LolAccount createdAccount = lolAccountService.create(importedAccount);
			createdAccounts.add(createdAccount);
		}
		
		//RESPONSE
		if (hasError){
			wrapper = new LolAccountWrapper();
			wrapper.setError(error);
		} else {
			wrapper = new LolAccountWrapper();
		}
		wrapper.add("data",createdAccounts);
		
		//RETURN
		return new ResponseEntity<LolAccountWrapper>(wrapper,HttpStatus.OK);
	}
	private void validateUserByUserId(Long userId) {
		userService.findOptionalByUserId(userId).orElseThrow(
			() -> new UserNotFoundException(userId));
	}

	private void validateAccountById(Long accountId) {
		lolAccountService.findOptionalById(accountId).orElseThrow(
			() -> new AccountNotFoundException(accountId));
	}
	
	//TODO authentication
	private String checkUser(Long userid){
		String error = "";
		//CHECKS
		Optional<User> optionalUserFromId = userService.findOptionalByUserId(userid);
		if(!optionalUserFromId.isPresent()){
			error = "User with id " + userid + " does not exist";
		}/* else {
			User userFromId = optionalUserFromId.get();
			Optional<User> optionalAuthenticatedUser = authenticationFacade.getOptionalAuthenticatedUser();
			if(!optionalAuthenticatedUser.isPresent()){
				error = "You are currently not authenticated, log in to the site";	
			} else{
				User authenticatedUser = optionalAuthenticatedUser.get();
				if(!userFromId.equals(authenticatedUser)){
					error = "The requested accounts are not owned by the authenticated user";	
				}
			}
		}*/
		return error;
	}
}
