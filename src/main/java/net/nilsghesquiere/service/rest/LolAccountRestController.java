package net.nilsghesquiere.service.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
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
import net.nilsghesquiere.web.error.UploadedFileContentTypeException;
import net.nilsghesquiere.web.error.UploadedFileEmptyException;
import net.nilsghesquiere.web.error.UploadedFileMalformedException;
import net.nilsghesquiere.web.error.UploadedFileSizeException;
import net.nilsghesquiere.web.error.UserIsNotOwnerOfResourceException;
import net.nilsghesquiere.web.error.UserNotFoundException;
import net.nilsghesquiere.web.util.GenericResponse;

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
public class LolAccountRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LolAccountRestController.class);
	
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
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
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
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
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
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
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
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//PROCESSING
		LOGGER.info("userid=" + userid +", account=" + account);
		LolAccount lolAccount = lolAccountService.findByUserIdAndRegionAndAccount(userid, region, account);
		//RESPONSE
		
		//RETURN
		return new ResponseEntity<LolAccount>(lolAccount, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}", method = RequestMethod.POST)
	public ResponseEntity<LolAccountWrapper> create(@PathVariable Long userid, @RequestBody LolAccountMap lolAccountMap) {
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//VARS
		LolAccountWrapper wrapper = new LolAccountWrapper();
		List<LolAccount> returnAccounts = new ArrayList<>();
		String error = "";
		
		//LOOP (Will always be 1 account for now)
		for(LolAccount lolAccount: lolAccountMap.getMap().values()){
			//CHECKS
			error = "";
			if (lolAccount == null){
				error = "Account is empty";
			}
			//PROCESSING
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
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
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
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		for (LolAccount lolAccount : lolAccountMap.getMap().values()){
			validateAccountById(lolAccount.getId());
			validateUserByUserId(userid);
			lolAccountService.delete(lolAccount);
			deletedAccounts.add(lolAccount);
		}
		wrapper.add("data",deletedAccounts);
		return new ResponseEntity<LolAccountWrapper>(wrapper,HttpStatus.OK);
	}
	
	//import with region in file
	@RequestMapping(value="/user/{userid}/import", method=RequestMethod.POST)
	public ResponseEntity<LolAccountWrapper> processUpload(@PathVariable Long userid, @RequestParam MultipartFile file) throws IOException {
		//VARS
		String error = "";
		StringBuilder errorBuilder = new StringBuilder("");
		LolAccountWrapper wrapper = new LolAccountWrapper();
		List<LolAccount> importedAccounts = new ArrayList<>();
		List<LolAccount> createdAccounts = new ArrayList<>();
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//SIZE CHECK: geen bestanden groter dan 1 MB
		if(file.getSize() > 1048576){
			LOGGER.info("User " + userid + " attempted to upload a file of " + file.getSize() + " bytes");
			throw new UploadedFileSizeException();
		}
		
		//TYPE CHECK
		if(!file.getContentType().equals("text/plain")){
			LOGGER.info("User " + userid + " attempted to upload a file of type " + file.getContentType());
			throw new UploadedFileContentTypeException();
		}
		
		//MAP THE INPUT TO LOLACCOUNTS & FILE FORM CHECK
		try(Stream<String> stream = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("UTF-8"))).lines()){
			importedAccounts = stream
								.map(line -> LolAccount.buildFromString(user,line))
								.collect(Collectors.toList());
		} catch (IllegalArgumentException | IllegalStateException | ArrayIndexOutOfBoundsException | UploadedFileMalformedException e) {
			throw new UploadedFileMalformedException();
		}
		
		//EMPTY ACCOUNTLISTCHECK
		if(importedAccounts.isEmpty()){
			throw new UploadedFileEmptyException();
		}
		
		for (LolAccount importedAccount : importedAccounts){
			LolAccount createdAccount = lolAccountService.create(importedAccount);
			if (createdAccount != null){
				createdAccounts.add(createdAccount);
			} else {
				if (errorBuilder.toString().equals("")){
					errorBuilder.append("The following combinations already exist: \n" + importedAccount.getAccount() + "/" + importedAccount.getRegion());
				} else {
					errorBuilder.append("\n" + importedAccount.getAccount() + "/" + importedAccount.getRegion());
				}
			}
		}
		error = errorBuilder.toString();
		
		//RESPONSE
		wrapper = new LolAccountWrapper();
		wrapper.setError(error);
		wrapper.add("data",createdAccounts);
		
		//RETURN
		return new ResponseEntity<LolAccountWrapper>(wrapper,HttpStatus.OK);
	}

	//import with selected region
	@RequestMapping(value="/user/{userid}/import/{region}", method=RequestMethod.POST)
	public ResponseEntity<LolAccountWrapper> processUploadRegion(@PathVariable Long userid,@PathVariable Region region, @RequestParam MultipartFile file) throws IOException {
		//VARS
		String error = "";
		StringBuilder errorBuilder = new StringBuilder("");
		LolAccountWrapper wrapper = new LolAccountWrapper();
		List<LolAccount> importedAccounts = new ArrayList<>();
		List<LolAccount> createdAccounts = new ArrayList<>();
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//SIZE CHECK: geen bestanden groter dan 1 MB
		if(file.getSize() > 1048576){
			LOGGER.info("User " + userid + " attempted to upload a file of " + file.getSize() + " bytes");
			throw new UploadedFileSizeException();
		}
		
		//TYPE CHECK
		if(!file.getContentType().equals("text/plain")){
			LOGGER.info("User " + userid + " attempted to upload a file of type " + file.getContentType());
			throw new UploadedFileContentTypeException();
		}
		
		//MAP THE INPUT TO LOLACCOUNTS & FILE FORM CHECK
		try(Stream<String> stream = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("UTF-8"))).lines()){
			importedAccounts = stream
								.map(line -> LolAccount.buildFromStringWithRegion(user,line,region))
								.collect(Collectors.toList());
		} catch (IllegalArgumentException | IllegalStateException | ArrayIndexOutOfBoundsException | UploadedFileMalformedException e) {
			throw new UploadedFileMalformedException();
		}
		
		//EMPTY ACCOUNTLISTCHECK
		if(importedAccounts.isEmpty()){
			throw new UploadedFileEmptyException();
		}
		
		for (LolAccount importedAccount : importedAccounts){
			LolAccount createdAccount = lolAccountService.create(importedAccount);
			if (createdAccount != null){
				createdAccounts.add(createdAccount);
			} else {
				if (errorBuilder.toString().equals("")){
					errorBuilder.append("The following combinations already exist: \n" + importedAccount.getAccount() + "/" + importedAccount.getRegion());
				} else {
					errorBuilder.append("\n" + importedAccount.getAccount() + "/" + importedAccount.getRegion());
				}
			}
		}
		error = errorBuilder.toString();
		
		//RESPONSE
		wrapper = new LolAccountWrapper();
		wrapper.setError(error);
		wrapper.add("data",createdAccounts);
		
		//RETURN
		return new ResponseEntity<LolAccountWrapper>(wrapper,HttpStatus.OK);
	}
	
	@RequestMapping(path = "/user/{userid}/infernalImport",method = RequestMethod.PUT)
	public ResponseEntity<StringResponseMap> infernalImport(@PathVariable Long userid,@RequestBody LolMixedAccountMap lolMixedAccountMap) {
		//VARS
		StringResponseMap responseMap = new StringResponseMap();
		String response = "OK";
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
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
			String error = "";
			if (lolAccount == null){
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
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
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
	
	@RequestMapping(path = "/user/{userid}/resetAllStatus",method = RequestMethod.POST)
	public GenericResponse resetAllStatus(@PathVariable Long userid) {
		//VARS
		int aantalAccounts = 0;
	
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		
		//PROCESSING
		for (LolAccount lolAccount : lolAccountService.findByUser(user)){
			if (!lolAccount.getAccountStatus().equals(AccountStatus.BANNED)){
				lolAccount.setAccountStatus(AccountStatus.READY_FOR_USE);
				lolAccount.setAssignedTo("");
				lolAccountService.update(lolAccount);
				aantalAccounts += 1;
			}
		}
		
		//RESPONSE
		return new GenericResponse("Succesfully reset the status of " + aantalAccounts + " accounts.");
	}
	
	@RequestMapping(path = "/user/{userid}/deleteAllBanned",method = RequestMethod.POST)
	public GenericResponse deleteAllBanned(@PathVariable Long userid) {
		//VARS
		int aantalAccounts = 0;
	
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		
		//PROCESSING
		for (LolAccount lolAccount : lolAccountService.findByUser(user)){
			if (lolAccount.getAccountStatus().equals(AccountStatus.BANNED)){
				lolAccountService.delete(lolAccount);
				aantalAccounts += 1;
			}
		}
		
		//RESPONSE
		return new GenericResponse("Succesfully deleted " + aantalAccounts + " banned accounts.");
	}
	//todo nut van deze bekijken
	private void validateUserByUserId(Long userId) {
		userService.findOptionalByUserId(userId).orElseThrow(
			() -> new UserNotFoundException(userId));
	}

	private void validateAccountById(Long accountId) {
		lolAccountService.findOptionalById(accountId).orElseThrow(
			() -> new AccountNotFoundException(accountId));
	}
	
}
