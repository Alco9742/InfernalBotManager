package net.nilsghesquiere.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.exceptions.AccountExistsException;
import net.nilsghesquiere.exceptions.AccountNotFoundException;
import net.nilsghesquiere.exceptions.UserNotFoundException;
import net.nilsghesquiere.facades.AuthenticationFacade;
import net.nilsghesquiere.services.ILolAccountService;
import net.nilsghesquiere.services.IUserService;
import net.nilsghesquiere.valueobjects.JSONResponse;
import net.nilsghesquiere.valueobjects.JSONResponseWithoutError;
import net.nilsghesquiere.valueobjects.JSONWrapper;
import net.nilsghesquiere.valueobjects.LolAccountListWrapper;
import net.nilsghesquiere.valueobjects.JSONResponseWithError;
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
	private final ILolAccountService lolAccountService;
	private final IUserService userService;
	private final AuthenticationFacade authenticationFacade;	
	
	@Autowired
	AccountsRestController(ILolAccountService lolAccountService,IUserService userService, AuthenticationFacade authenticationFacade){
		this.lolAccountService = lolAccountService;
		this.userService = userService;
		this.authenticationFacade = authenticationFacade;
	}
	
	@RequestMapping(path = "/{accountid}", method = RequestMethod.GET)
	public ResponseEntity<LolAccountListWrapper> findAccountById(@PathVariable Long accountid) {
		//VARS
		LolAccountListWrapper wrapper = new LolAccountListWrapper();
		List<LolAccount> lolAccounts = new ArrayList<>();
		
		//CHECKS
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
	public ResponseEntity<JSONResponse> findAccountsByUserId(@PathVariable Long userid) {
		//VARS
		JSONResponse wrapper;
		boolean hasError = false;
		String error = "";
		
		//CHECKS
		//error = checkUser(userid);
		if(!error.equals("")){
			hasError = true;
		}
		
		//PROCESSING
		List<LolAccount> lolAccounts = lolAccountService.findByUserId(userid);
		
		//RESPONSE
		if (hasError){
			wrapper = new JSONResponseWithError();
			wrapper.add("data",lolAccounts);
			wrapper.setError(error);
		} else {
			wrapper = new JSONResponseWithoutError();
			wrapper.add("data",lolAccounts);
		}
		
		//RETURN
		return new ResponseEntity<JSONResponse>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}", method = RequestMethod.POST)
	public ResponseEntity<JSONResponse> create(@PathVariable Long userid, @RequestBody LolAccountMap lolAccountMap) {
		//VARS
		JSONResponse wrapper;
		List<LolAccount> returnAccounts = new ArrayList<>();
		boolean hasError = false;
		String error = "";
		
		//LOOP (Will always be 1 account for now)
		for(LolAccount lolAccount: lolAccountMap.getMap().values()){
			//CHECKS
			error = checkUser(userid);
			if (lolAccount == null && error.equals("")){
				error = "Account is empty";
			}
			LOGGER.info("error: " + error);
			if(!error.equals("")){
				hasError = true;
			} else {
				//PROCESSING
				User user = userService.read(userid);
				LolAccount newAccount = new LolAccount(user,lolAccount.getUsername(),lolAccount.getPassword(),lolAccount.getMaxlevel(), lolAccount.isEnabled());	
				LolAccount createdAccount = lolAccountService.create(newAccount);
				returnAccounts.add(createdAccount);
			}
		}
		
		//RESPONSE
		if (hasError){
			wrapper = new JSONResponseWithError();
			wrapper.add("data",returnAccounts);
			wrapper.setError(error);
		} else {
			wrapper = new JSONResponseWithoutError();
			wrapper.add("data",returnAccounts);
		}
		
		//RETURN
		return new ResponseEntity<JSONResponse>(wrapper,HttpStatus.CREATED);
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
			validateAccountById(lolAccount.getId());
			validateUserByUserId(userid);
			lolAccountService.delete(lolAccount);
			deletedAccounts.add(lolAccount);
		}
		wrapper.add("data",deletedAccounts);
		return new ResponseEntity<LolAccountListWrapper>(wrapper,HttpStatus.OK);
	}
	
	@RequestMapping(value="/user/{userid}/import", method=RequestMethod.POST)
	public ResponseEntity<JSONResponse> processUpload(@PathVariable Long userid, @RequestParam MultipartFile file) throws IOException {
		//VARS
		boolean hasError = false;
		String error = "";
		JSONResponse wrapper;
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
			wrapper = new JSONResponseWithError();
			wrapper.setError(error);
		} else {
			wrapper = new JSONResponseWithoutError();
		}
		wrapper.add("data",createdAccounts);
		
		//RETURN
		return new ResponseEntity<JSONResponse>(wrapper,HttpStatus.OK);
	}
	private void validateUserByUserId(Long userId) {
		userService.findByUserId(userId).orElseThrow(
			() -> new UserNotFoundException(userId));
	}

	private void validateAccountById(Long accountId) {
		lolAccountService.findById(accountId).orElseThrow(
			() -> new AccountNotFoundException(accountId));
	}
	
	private String checkUser(Long userid){
		String error = "";
		//CHECKS
		Optional<User> optionalUserFromId = userService.findByUserId(userid);
		if(!optionalUserFromId.isPresent()){
			error = "User with id " + userid + " does not exist";
		} else {
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
		}
		return error;
	}
}
