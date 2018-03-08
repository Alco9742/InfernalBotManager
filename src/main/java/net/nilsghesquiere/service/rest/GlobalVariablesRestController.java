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
import net.nilsghesquiere.util.enums.AccountStatus;
import net.nilsghesquiere.util.enums.Region;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.util.wrappers.LolAccountMap;
import net.nilsghesquiere.util.wrappers.LolAccountWrapper;
import net.nilsghesquiere.util.wrappers.LolMixedAccountMap;
import net.nilsghesquiere.util.wrappers.StringResponseMap;
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
public class GlobalVariablesRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalVariablesRestController.class);
	
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
	
	@RequestMapping(path = "/user/{userid}/region/{region}/limit/{amount}", method = RequestMethod.GET)
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
	
	@RequestMapping(path = "/user/{userid}/region/{region}/limit/{amount}/buffer/", method = RequestMethod.GET)
	public ResponseEntity<LolAccountWrapper> findBufferAccounts(@PathVariable Long userid, @PathVariable Region region, @PathVariable Integer amount) {
		//VARS
		LolAccountWrapper wrapper = new LolAccountWrapper();
		String error = "";
		
		//PROCESSING
		List<LolAccount> lolAccounts = lolAccountService.findBufferAccounts(userid, region,amount);
		
		//RESPONSE
		wrapper.setError(error);
		wrapper.add("data",lolAccounts);
	
		//RETURN
		return new ResponseEntity<LolAccountWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}/region/{region}/account/{account}", method = RequestMethod.GET)
	public ResponseEntity<LolAccount> findAccountId(@PathVariable Long userid, @PathVariable Region region, @PathVariable String account) {
		//PROCESSING
		LOGGER.info("userid=" + userid +", account=" + account);
		LolAccount lolAccount = lolAccountService.findByUserIdAndRegionAndAccount(userid, region, account);
		//RESPONSE
		
		//RETURN
		return new ResponseEntity<LolAccount>(lolAccount, HttpStatus.OK);
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
			if(createdAccount != null){
				returnAccounts.add(createdAccount);
			} else {
				error ="This account/region combination already exists on the server!";
			}
		}
		
		//RESPONSE
		wrapper.add("data",returnAccounts);
		wrapper.setError(error);

		//RETURN
		return new ResponseEntity<LolAccountWrapper>(wrapper,HttpStatus.CREATED);
	}
	
	@RequestMapping(path = "/user/{userid}",method = RequestMethod.PUT)
	public ResponseEntity<LolAccountWrapper> update(@PathVariable Long userid,@RequestBody LolAccountMap lolAccountMap) {
		String error = "";
		LolAccountWrapper wrapper = new LolAccountWrapper();
		List<LolAccount> returnAccounts = new ArrayList<>();
		for (LolAccount lolAccount : lolAccountMap.getMap().values()){
			Preconditions.checkNotNull(lolAccount);
			validateAccountById(lolAccount.getId());
			validateUserByUserId(userid);
			lolAccount.setUser(userService.read(userid));
			LolAccount updatedLolAccount = lolAccountService.update(lolAccount);
			//returns null if the account changed server but there is already an account with that name on the new server
			if (updatedLolAccount != null){
				returnAccounts.add(updatedLolAccount);
			} else {
				if (error.equals("")){
					error = "The following combinations are already present on the server: " + lolAccount.getAccount() + "/" + lolAccount.getRegion() ;
				} else {
					error = error + ", " + lolAccount.getAccount() + "/" + lolAccount.getRegion();
				}
			}
		}
		wrapper.add("data",returnAccounts);
		wrapper.setError(error);
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
			error = "Uploaded file is not a plain text file!";
		}
		for (LolAccount importedAccount : importedAccounts){
			LolAccount createdAccount = lolAccountService.create(importedAccount);
			if (createdAccount != null){
				createdAccounts.add(createdAccount);
			} else {
				if (!error.equals("Uploaded file is not a plain text file!")){
					if (error.equals("")){
						error= "The following account/region combinations already exist in the database: " + importedAccount.getAccount() + "/" + importedAccount.getRegion();
					} else {
						error = error + ", " +  importedAccount.getAccount() + "/" + importedAccount.getRegion();
					}
				}
			}
		}
		
		//RESPONSE
		wrapper = new LolAccountWrapper();
		wrapper.setError(error);
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
	
	@RequestMapping(path = "/user/{userid}/infernalImport",method = RequestMethod.PUT)
	public ResponseEntity<StringResponseMap> infernalImport(@PathVariable Long userid,@RequestBody LolMixedAccountMap lolMixedAccountMap) {
		//TODO put better error handling in this
		//VARS
		StringResponseMap responseMap = new StringResponseMap();
		String response = "OK";
		//update existing accounts
		for (LolAccount lolAccount : lolMixedAccountMap.getMap().values()){
			Preconditions.checkNotNull(lolAccount);
			validateAccountById(lolAccount.getId());
			validateUserByUserId(userid);
			lolAccount.setUser(userService.read(userid));
			LolAccount updatedLolAccount = lolAccountService.update(lolAccount);
			//returns null if the account changed server but there is already an account with that name on the new server
			if (updatedLolAccount == null){
				responseMap.add(lolAccount.getAccount(), "Combination (" + lolAccount.getAccount() + "/" +lolAccount.getRegion() + ") already exists");
			}
		}
		//create new accounts
		for(LolAccount lolAccount: lolMixedAccountMap.getNewAccs()){
			//CHECKS
			String error = checkUser(userid);
			if (lolAccount == null && !error.equals("")){
				error = "Account is empty";
			}
			//PROCESSING
			if(error.equals("")){
				lolAccount.setUser(userService.read(userid));
				LolAccount returnAccount = lolAccountService.create(lolAccount);
				if (returnAccount == null){
					error = "Combination (" + lolAccount.getAccount() + "/" +lolAccount.getRegion() + ") already exists";
				}
			} 
			
			if(!error.equals("")){
				response = error;
			}
			
			responseMap.add(lolAccount.getAccount(), response);
		}
		return new ResponseEntity<StringResponseMap>(responseMap,HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}/resetStatus",method = RequestMethod.PUT)
	public ResponseEntity<LolAccountWrapper> resetStatus(@PathVariable Long userid,@RequestBody Long[] ids) {
		LolAccountWrapper wrapper = new LolAccountWrapper();
		List<LolAccount> returnAccounts = new ArrayList<>();
		for (Long id : ids){
			LolAccount lolAccount = lolAccountService.read(id);
			Preconditions.checkNotNull(lolAccount);
			validateAccountById(lolAccount.getId());
			validateUserByUserId(userid);
			lolAccount.setAccountStatus(AccountStatus.READY_FOR_USE);
			lolAccount.setAssignedTo("");
			LolAccount updatedLolAccount = lolAccountService.update(lolAccount);
			returnAccounts.add(updatedLolAccount);
		}
		wrapper.add("data",returnAccounts);
		return new ResponseEntity<LolAccountWrapper>(wrapper,HttpStatus.OK);
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
