package net.nilsghesquiere.service.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.nilsghesquiere.entities.GlobalVariable;
import net.nilsghesquiere.entities.Metric;
import net.nilsghesquiere.entities.Role;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.UserSettings;
import net.nilsghesquiere.security.LoginAttemptService;
import net.nilsghesquiere.service.web.ClientDataService;
import net.nilsghesquiere.service.web.GlobalVariableService;
import net.nilsghesquiere.service.web.LolAccountService;
import net.nilsghesquiere.service.web.MetricService;
import net.nilsghesquiere.service.web.RoleService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.wrappers.GlobalVariableMap;
import net.nilsghesquiere.util.wrappers.GlobalVariableWrapper;
import net.nilsghesquiere.util.wrappers.LoginAttemptDTOMap;
import net.nilsghesquiere.util.wrappers.LoginAttemptDTOWrapper;
import net.nilsghesquiere.util.wrappers.MetricWrapper;
import net.nilsghesquiere.util.wrappers.UserMap;
import net.nilsghesquiere.util.wrappers.UserWrapper;
import net.nilsghesquiere.web.dto.LoginAttemptDTO;
import net.nilsghesquiere.web.dto.UserAdminDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.cache.LoadingCache;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/admin")
public class AdminRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminRestController.class);
	
	@Autowired
	private MetricService metricService;
	
	@Autowired 
	private GlobalVariableService globalVariableService;
	
	@Autowired 
	private UserService userService;
	
	@Autowired 
	private RoleService roleService;
	
	@Autowired
	private LolAccountService accountService;
	
	@Autowired
	private ClientDataService clientDataService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired 
	private LoginAttemptService loginAttemptService;
	
//METRICS

	@RequestMapping(path = "/metrics", method = RequestMethod.GET)
	public ResponseEntity<MetricWrapper> findAllMetrics() {
		//VARS
		MetricWrapper wrapper = new MetricWrapper();
		String error = "";
		
		//PROCESSING
		
		//Generate all metrics
		Metric totalUserMetric = new Metric ("totalUsers",Long.toString(userService.countAll()));
		Metric totalAccountsMetric = new Metric("totalAccounts", Long.toString(accountService.countAll()));
		Metric runningQueuersMetric = new Metric("runningQueuers", Long.toString(clientDataService.countActiveQueuers()));
		createOrUpdateMetric(totalUserMetric);
		createOrUpdateMetric(totalAccountsMetric);
		createOrUpdateMetric(runningQueuersMetric);
		
		
		//Get all metrics
		List<Metric> metrics = metricService.findAll();
		
		//RESPONSE
		wrapper.setError(error);
		wrapper.add("data",metrics);
		
		//RETURN
		return new ResponseEntity<MetricWrapper>(wrapper, HttpStatus.OK);
	}
	
	private void createOrUpdateMetric(Metric metric) {
		Metric metricFromDB = metricService.findByName(metric.getName());
		if (metricFromDB == null){
			metricService.create(metric);
		} else {
			metricFromDB.setValue(metric.getValue());
			metricService.update(metricFromDB);
		}
	}

//GLOBAL VARIABLES
	
	@RequestMapping(path = "/globalvars", method = RequestMethod.GET)
	public ResponseEntity<GlobalVariableWrapper> findAllVars() {
		//VARS
		GlobalVariableWrapper wrapper = new GlobalVariableWrapper();
		String error = "";
		
		//PROCESSING
		List<GlobalVariable> globalVariables = globalVariableService.findAll();
		
		//RESPONSE
		wrapper.setError(error);
		wrapper.add("data",globalVariables);
		
		//RETURN
		return new ResponseEntity<GlobalVariableWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/globalvars", method = RequestMethod.POST)
	public ResponseEntity<GlobalVariableWrapper> createVar(@RequestBody GlobalVariableMap globalVariableMap) {
		//VARS
		GlobalVariableWrapper wrapper = new GlobalVariableWrapper();
		List<GlobalVariable> returnVariables = new ArrayList<>();
		String error = "";
		
		//LOOP (Will always be 1 variable for now)
		for(GlobalVariable globalVariable: globalVariableMap.getMap().values()){
			//CHECKS
			if (globalVariable == null){
				error = "Variable is empty";
			}
			//PROCESSING
			GlobalVariable newVariable = globalVariableService.create(globalVariable);
			if(newVariable != null){
				returnVariables.add(newVariable);
			}
		}
		//RESPONSE
		wrapper.add("data",returnVariables);
		wrapper.setError(error);

		//RETURN
		return new ResponseEntity<GlobalVariableWrapper>(wrapper,HttpStatus.CREATED);
	}
	
	
	@RequestMapping(path = "/globalvars",method = RequestMethod.PUT)
	public ResponseEntity<GlobalVariableWrapper> updateVar(@RequestBody GlobalVariableMap globalVariableMap) {
		String error = "";
		GlobalVariableWrapper wrapper = new GlobalVariableWrapper();
		List<GlobalVariable> returnVariables = new ArrayList<>();
		for (GlobalVariable globalVariable : globalVariableMap.getMap().values()){
			GlobalVariable updatedVariable = globalVariableService.update(globalVariable);
			if (updatedVariable != null){
				returnVariables.add(updatedVariable);
			} else {
				if (error.equals("")){
					error = "The following keys are already present on the server: " + globalVariable.getName() ;
				} else {
					error = error + ", " + globalVariable.getName();
				}
			}
		}
		wrapper.add("data",returnVariables);
		wrapper.setError(error);
		return new ResponseEntity<GlobalVariableWrapper>(wrapper,HttpStatus.OK);
	}
	
	@RequestMapping(path = "/globalvars/delete",method = RequestMethod.POST)
	public ResponseEntity<GlobalVariableWrapper> deleteVar(@RequestBody GlobalVariableMap globalVariableMap) {
		GlobalVariableWrapper wrapper = new GlobalVariableWrapper();
		List<GlobalVariable> deletedVariables = new ArrayList<>();
		for (GlobalVariable globalVariable : globalVariableMap.getMap().values()){
			globalVariableService.delete(globalVariable);
			deletedVariables.add(globalVariable);
		}
		wrapper.add("data",deletedVariables);
		return new ResponseEntity<GlobalVariableWrapper>(wrapper,HttpStatus.OK);
	}
	
//USERS

	@RequestMapping(path = "/users", method = RequestMethod.GET)
	public ResponseEntity<UserWrapper> findAllUsers() {
		//VARS
		List<UserAdminDTO> userAdminDTOList = new ArrayList<UserAdminDTO>();
		UserWrapper wrapper = new UserWrapper();
		String error = "";
		
		//PROCESSING
		List<User> users = userService.findAll();
		for (User user : users){
			//NOT PASSING PASSWORDS
			Integer activeQueuers = clientDataService.countActiveQueuersByUserId(user.getId());
			UserAdminDTO dto = new UserAdminDTO(user.getId(),user.getEmail(), activeQueuers, user.getUserSettings().getMaxQueuers(),user.isEnabled());
			userAdminDTOList.add(dto);
		}
		
		//RESPONSE
		wrapper.setError(error);
		wrapper.add("data",userAdminDTOList);
		
		//RETURN
		return new ResponseEntity<UserWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/users", method = RequestMethod.POST)
	public ResponseEntity<UserWrapper> createUser(@RequestBody UserMap userMap) {
		//VARS
		UserWrapper wrapper = new UserWrapper();
		List<UserAdminDTO> returnUsers = new ArrayList<>();
		String error = "";
		
		//LOOP (Will always be 1 variable for now)
		for(UserAdminDTO dto: userMap.getMap().values()){
			//CHECKS
			if (dto == null){
				error = "User is empty";
			}
			if(dto.getPassword() == null || dto.getPassword().isEmpty()){
				error = "Password is empty";
			}
			
			if (error.isEmpty()){
				returnUsers.add(createOrUpdateUserFromDTO(dto));
			}

		}
		//RESPONSE
		wrapper.add("data",returnUsers);
		wrapper.setError(error);

		//RETURN
		return new ResponseEntity<UserWrapper>(wrapper,HttpStatus.CREATED);
	}
	
	
	@RequestMapping(path = "/users",method = RequestMethod.PUT)
	public ResponseEntity<UserWrapper> update(@RequestBody UserMap userMap) {
		String error = "";
		UserWrapper wrapper = new UserWrapper();
		List<UserAdminDTO> returnUsers = new ArrayList<>();
		for (UserAdminDTO dto: userMap.getMap().values()){
			returnUsers.add(createOrUpdateUserFromDTO(dto));
		}
		wrapper.add("data",returnUsers);
		wrapper.setError(error);
		return new ResponseEntity<UserWrapper>(wrapper,HttpStatus.OK);
	}
	
	@RequestMapping(path = "/users/delete",method = RequestMethod.POST)
	public ResponseEntity<UserWrapper> delete(@RequestBody UserMap userMap) {
		UserWrapper wrapper = new UserWrapper();
		List<UserAdminDTO> deletedUsers = new ArrayList<>();
		for (UserAdminDTO dto : userMap.getMap().values()){
			UserAdminDTO newDto = deleteUserFromDTO(dto);
			deletedUsers.add(newDto);
		}
		wrapper.add("data",deletedUsers);
		return new ResponseEntity<UserWrapper>(wrapper,HttpStatus.OK);
	}

	@RequestMapping(path = "/loginattempts", method = RequestMethod.GET)
	public ResponseEntity<LoginAttemptDTOWrapper> findAllFailedLoginAttempts() {
		LoginAttemptDTOWrapper wrapper = new LoginAttemptDTOWrapper();
		List<LoginAttemptDTO> loginAttempts = new ArrayList<>();
		LoadingCache<String, Integer> attemptsCache = loginAttemptService.findAllAttempts();
		LoadingCache<String, Set<String>> usernamesCache = loginAttemptService.findAllUsernames();
		//RETURN
		for(Entry<String,Integer> entry : attemptsCache.asMap().entrySet()){
			LoginAttemptDTO dto = new LoginAttemptDTO();
			Set<String> usernames = usernamesCache.asMap().get(entry.getKey());
			dto.setIp(entry.getKey());
			dto.setAttempts(entry.getValue());
			dto.setUsernames(usernames);
			loginAttempts.add(dto);
		}

		wrapper.add("data",loginAttempts);
		return new ResponseEntity<LoginAttemptDTOWrapper>(wrapper, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/loginattempts/delete",method = RequestMethod.POST)
	public ResponseEntity<LoginAttemptDTOWrapper> deleteLoginAttempts(@RequestBody LoginAttemptDTOMap loginAttemptMap) {
		LoginAttemptDTOWrapper wrapper = new LoginAttemptDTOWrapper();
		List<LoginAttemptDTO> deletedLoginAttempts = new ArrayList<>();
		for (LoginAttemptDTO dto : loginAttemptMap.getMap().values()){
			loginAttemptService.loginSucceeded(dto.getIp());
			deletedLoginAttempts.add(dto);
		}
		wrapper.add("data",deletedLoginAttempts);
		return new ResponseEntity<LoginAttemptDTOWrapper>(wrapper,HttpStatus.OK);
	}
	
	
	
	private UserAdminDTO createOrUpdateUserFromDTO(UserAdminDTO dto) {
		User user = userService.findUserByUserId(dto.getId());
		if (user == null) {
			user = new User();
			UserSettings userSettings = new UserSettings();
			//set userdata
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
			user.setEmail(dto.getEmail());
			user.setEnabled(dto.getEnabled());
			//set user settings
			userSettings.setMaxQueuers(dto.getMaxQueuers());
			user.setUserSettings(userSettings);
			//Set User Role
			Role userRole = roleService.findByName("ROLE_USER");
			Collection<Role> roles = new ArrayList<Role>(Arrays.asList(userRole));
			user.setRoles(roles);
			userService.create(user);
			User createdUser = userService.findUserByEmail(dto.getEmail());
			user = createdUser;
		} else {
			user.setEmail(dto.getEmail());
			if (dto.getPassword() != null || !dto.getPassword().isEmpty()){
				user.setPassword(passwordEncoder.encode(dto.getPassword()));
			}
			user.setEnabled(dto.getEnabled());
			//set user settings
			user.getUserSettings().setMaxQueuers(dto.getMaxQueuers());
			User updatedUser = userService.update(user);
			user=updatedUser;
		}
		Integer activeQueuers = clientDataService.countActiveQueuersByUserId(user.getId());
		return new UserAdminDTO(user.getId(),user.getEmail(),activeQueuers,user.getUserSettings().getMaxQueuers(),user.isEnabled());
	}
	
	private UserAdminDTO deleteUserFromDTO(UserAdminDTO dto) {
		User user = userService.findUserByEmail(dto.getEmail());
		userService.delete(user);
		return new UserAdminDTO(user.getId(),user.getEmail(),0, user.getUserSettings().getMaxQueuers(),user.isEnabled());
	}
}
