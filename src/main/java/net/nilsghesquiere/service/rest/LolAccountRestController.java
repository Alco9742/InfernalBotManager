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

import net.nilsghesquiere.entities.ImportSettings;
import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.ImportSettingsService;
import net.nilsghesquiere.service.web.LolAccountService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.enums.AccountStatus;
import net.nilsghesquiere.util.enums.Region;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.util.wrappers.LolAccountMap;
import net.nilsghesquiere.util.wrappers.LolAccountWrapper;
import net.nilsghesquiere.util.wrappers.StringResponseMap;
import net.nilsghesquiere.web.error.ActiveImportSettingsNotSelectedException;
import net.nilsghesquiere.web.error.SettingsNotFoundException;
import net.nilsghesquiere.web.error.UploadedFileContentTypeException;
import net.nilsghesquiere.web.error.UploadedFileEmptyException;
import net.nilsghesquiere.web.error.UploadedFileMalformedException;
import net.nilsghesquiere.web.error.UploadedFileSizeException;
import net.nilsghesquiere.web.error.UserIsNotOwnerOfResourceException;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/accounts")
public class LolAccountRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LolAccountRestController.class);
	
	@Autowired
	private LolAccountService lolAccountService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ImportSettingsService importSettingsService;
	
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
		LolAccount lolAccount = lolAccountService.findByUserIdAndRegionAndAccount(userid, region, account);
		//RESPONSE
		
		//RETURN
		return new ResponseEntity<LolAccount>(lolAccount, HttpStatus.OK);
	}
	
	//TODO add check for importSettings
	
	@RequestMapping(path = "/user/{userid}", method = RequestMethod.POST)
	public ResponseEntity<LolAccountWrapper> create(@PathVariable Long userid, @RequestBody LolAccountMap lolAccountMap) {
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//IMPORTSETTINGS
		Long activeUserSettingsId = user.getUserSettings().getActiveImportSettings();
		if(activeUserSettingsId == 0){
			throw new ActiveImportSettingsNotSelectedException();
		}
		
		ImportSettings importSettings = importSettingsService.read(activeUserSettingsId);
		
		if (importSettings == null){
			//should never get here
			throw new SettingsNotFoundException(activeUserSettingsId);
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
			LolAccount newAccount = new LolAccount(user,lolAccount.getAccount(),lolAccount.getPassword(),lolAccount.getRegion(), importSettings);	
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
			LolAccount lolAccountFromDB = lolAccountService.read(lolAccount.getId());
			if (lolAccountFromDB != null){
				if(!lolAccountFromDB.getUser().equals(user)){
					throw new UserIsNotOwnerOfResourceException();
				}
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
			LolAccount lolAccountFromDB = lolAccountService.read(lolAccount.getId());
			if (lolAccountFromDB != null){
				if(!lolAccountFromDB.getUser().equals(user)){
					throw new UserIsNotOwnerOfResourceException();
				}
				lolAccountService.delete(lolAccount);
				deletedAccounts.add(lolAccount);
			}
		}
		wrapper.add("data",deletedAccounts);
		return new ResponseEntity<LolAccountWrapper>(wrapper,HttpStatus.OK);
	}
	
	//TODO add checks for importsettings
	
	//import with region in file
	//TODO krijg hier een 404
	@RequestMapping(value="/user/{userid}/import/{importsettingsid}", method=RequestMethod.POST)
	public ResponseEntity<LolAccountWrapper> processUpload(@PathVariable Long userid, @PathVariable Long importsettingsid, @RequestParam MultipartFile file) throws IOException {
		LOGGER.info("test");
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
		
		//IMPORTSETTING
		
		if(importsettingsid == 0){
			throw new ActiveImportSettingsNotSelectedException();
		}
		
		ImportSettings importSettings = importSettingsService.read(importsettingsid);
		
		if (importSettings == null){
			throw new SettingsNotFoundException(importsettingsid);
		}
		
		//MAP THE INPUT TO LOLACCOUNTS & FILE FORM CHECK
		try(Stream<String> stream = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("UTF-8"))).lines()){
			importedAccounts = stream
								.map(line -> LolAccount.buildFromString(user,line,importSettings))
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
	@RequestMapping(value="/user/{userid}/import/{importsettingsid}/region/{region}", method=RequestMethod.POST)
	public ResponseEntity<LolAccountWrapper> processUploadRegion(@PathVariable Long userid,@PathVariable Long importsettingsid, @PathVariable Region region, @RequestParam MultipartFile file) throws IOException {
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
		
		if(importsettingsid == 0){
			throw new ActiveImportSettingsNotSelectedException();
		}
		
		//IMPORTSETTING
		ImportSettings importSettings = importSettingsService.read(importsettingsid);
		
		if (importSettings == null){
			throw new SettingsNotFoundException(importsettingsid);
		}
		
		//MAP THE INPUT TO LOLACCOUNTS & FILE FORM CHECK
		try(Stream<String> stream = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("UTF-8"))).lines()){
			importedAccounts = stream
								.map(line -> LolAccount.buildFromStringWithRegion(user,line,region,importSettings))
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
	public ResponseEntity<StringResponseMap> infernalImport(@PathVariable Long userid,@RequestBody LolAccountMap lolAccountMap) {
		//VARS
		StringResponseMap responseMap = new StringResponseMap();
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//update existing accounts
		for (LolAccount lolAccount : lolAccountMap.getMap().values()){
			if(lolAccount != null){
				LolAccount lolAccountFromDB = lolAccountService.read(lolAccount.getId());
				if (lolAccountFromDB != null){
					if(!lolAccountFromDB.getUser().equals(user)){
						throw new UserIsNotOwnerOfResourceException();
					}
					LolAccount updatedLolAccount = lolAccountService.update(lolAccount);
					//returns null if the account changed server but there is already an account with that name on the new server
					if (updatedLolAccount == null){
						responseMap.add(lolAccount.getAccount(), "Combination (" + lolAccount.getAccount() + "/" +lolAccount.getRegion() + ") already exists");
					}
				}
			}
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
			if(lolAccount != null){
				LolAccount lolAccountFromDB = lolAccountService.read(lolAccount.getId());
				if (lolAccountFromDB != null){
					if(!lolAccountFromDB.getUser().equals(user)){
						throw new UserIsNotOwnerOfResourceException();
					}
					lolAccount.setAccountStatus(AccountStatus.READY_FOR_USE);
					lolAccount.setAssignedTo("");
					LolAccount updatedLolAccount = lolAccountService.update(lolAccount);
					returnAccounts.add(updatedLolAccount);
				}
			}
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
			if (!lolAccount.getAccountStatus().equals(AccountStatus.BANNED) && !lolAccount.getAccountStatus().equals(AccountStatus.DONE)){
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
	
	@RequestMapping(path = "/user/{userid}/export/selected/ir/{includeRegion}/il/{includeLevel}/",method = RequestMethod.PUT)
	public @ResponseBody byte[] exportSimpleSelected(@PathVariable Long userid,@PathVariable Boolean includeRegion,@PathVariable Boolean includeLevel, @RequestBody Long[] ids) {
		//VARS
		StringBuilder builder = new StringBuilder("");
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		for (Long id : ids){
			LolAccount lolAccount = lolAccountService.read(id);
			if(lolAccount != null){
				if(!lolAccount.getUser().equals(user)){
					throw new UserIsNotOwnerOfResourceException();
				}
				builder.append(lolAccount.getAccount());
				builder.append(":");
				builder.append(lolAccount.getPassword());
				if(includeRegion){
					builder.append(":");
					builder.append(lolAccount.getRegion().toString());
				}
				if(includeLevel){
					builder.append(":");
					builder.append(lolAccount.getLevel());
				}
				builder.append(System.lineSeparator());
			}
		}
		
		//remove last empty line
		
		int last = builder.lastIndexOf("\n");
		if (last >= 0) {
			builder.delete(last, builder.length());
		}
		
		return builder.toString().getBytes();
	}
	@RequestMapping(value = {"/user/{userid}/export/custom/r/{region}/s/{status}/ir/{includeRegion}/il/{includeLevel}/",
							"/user/{userid}/export/custom/r/{region}/ir/{includeRegion}/il/{includeLevel}/",
							"/user/{userid}/export/custom/s/{status}/ir/{includeRegion}/il/{includeLevel}/",
							"/user/{userid}/export/custom/ir/{includeRegion}/il/{includeLevel}/"},
					method = RequestMethod.PUT)
	public @ResponseBody byte[] exportSimpleCustom(@PathVariable Long userid, @PathVariable Optional<Region> region, @PathVariable Optional<AccountStatus> status, @PathVariable Boolean includeRegion,@PathVariable Boolean includeLevel) {
		//VARS
		StringBuilder builder = new StringBuilder("");
		boolean allRegions = false;
		boolean allStatus = false;
		
		//USER CHECK
		User user = userService.findUserByUserId(userid);
		if(!authenticationFacade.getAuthenticatedUser().equals(user)){
			throw new UserIsNotOwnerOfResourceException();
		}
		
		//fill the booleans according to the input
		if(!region.isPresent()){
			allRegions = true;
		}
		if(!status.isPresent()){
			allStatus = true;
		}
		
		
		//build the list of accounts needed for export
		List<LolAccount> lolAccounts = new ArrayList<>();
		
		if(allRegions){
			if (allStatus){
				//All regions and all status = find by user
				lolAccounts = lolAccountService.findByUser(user);
			} else {
				//All regions but selected status
				lolAccounts = lolAccountService.findByUserAndAccountStatus(user,status.get());
			}
		} else {
			if (allStatus){
				//All status but selected region
				lolAccounts = lolAccountService.findByUserAndRegion(user, region.get());
			} else {
				//Selected region and selected status
				lolAccounts = lolAccountService.findByUserAndRegionAndAccountStatus(user, region.get(), status.get());
			}
		}
		
		//export (can stay same as regular)
		for (LolAccount lolAccount : lolAccounts){
			if(lolAccount != null){
				if(!lolAccount.getUser().equals(user)){
					throw new UserIsNotOwnerOfResourceException();
				}
				builder.append(lolAccount.getAccount());
				builder.append(":");
				builder.append(lolAccount.getPassword());
				if(includeRegion){
					builder.append(":");
					builder.append(lolAccount.getRegion().toString());
				}
				if(includeLevel){
					builder.append(":");
					builder.append(lolAccount.getLevel());
				}
				builder.append(System.lineSeparator());
			}
		}
		return builder.toString().getBytes();
	}
}
