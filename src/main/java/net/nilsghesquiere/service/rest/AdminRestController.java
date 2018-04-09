package net.nilsghesquiere.service.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.nilsghesquiere.entities.GlobalVariable;
import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.entities.Metric;
import net.nilsghesquiere.entities.Role;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.GlobalVariableService;
import net.nilsghesquiere.service.web.InfernalSettingsService;
import net.nilsghesquiere.service.web.MetricService;
import net.nilsghesquiere.service.web.RoleService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.wrappers.GlobalVariableMap;
import net.nilsghesquiere.util.wrappers.GlobalVariableWrapper;
import net.nilsghesquiere.util.wrappers.MetricWrapper;
import net.nilsghesquiere.util.wrappers.UserMap;
import net.nilsghesquiere.util.wrappers.UserWrapper;
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

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/admin")
public class AdminRestController {
	@SuppressWarnings("unused")
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
	private InfernalSettingsService infernalSettingsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
//METRICS

	@RequestMapping(path = "/metrics", method = RequestMethod.GET)
	public ResponseEntity<MetricWrapper> findAllMetrics() {
		//VARS
		MetricWrapper wrapper = new MetricWrapper();
		String error = "";
		
		//PROCESSING
		
		//Generate all metrics
		Metric totalUserMetric = new Metric ("totalUsers",Long.toString(userService.countAll()));
		
		//Get all metrics
		List<Metric> metrics = metricService.findAll();
		
		//RESPONSE
		wrapper.setError(error);
		wrapper.add("data",metrics);
		
		//RETURN
		return new ResponseEntity<MetricWrapper>(wrapper, HttpStatus.OK);
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
			UserAdminDTO dto = new UserAdminDTO(user.getId(),user.getEmail(),user.isEnabled());
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
				returnUsers.add(creatOrUpdateUserFromDTO(dto));
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
			returnUsers.add(creatOrUpdateUserFromDTO(dto));
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

	private UserAdminDTO creatOrUpdateUserFromDTO(UserAdminDTO dto) {
		User user = userService.findUserByEmail(dto.getEmail());
		if (user == null) {
			user = new User();
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
			user.setEmail(dto.getEmail());
			user.setEnabled(dto.getEnabled());
			//Set User Role
			Role userRole = roleService.findByName("ROLE_USER");
			Collection<Role> roles = new ArrayList<Role>(Arrays.asList(userRole));
			user.setRoles(roles);
			userService.create(user);
			User createdUser = userService.findUserByEmail(dto.getEmail());
			InfernalSettings inferalSettings = infernalSettingsService.create(new InfernalSettings(createdUser));
			user.setInfernalSettings(inferalSettings);
			user = createdUser;
		} else {
			user.setEmail(dto.getEmail());
			if (dto.getPassword() != null || !dto.getPassword().isEmpty()){
				user.setPassword(passwordEncoder.encode(dto.getPassword()));
			}
			user.setEnabled(dto.getEnabled());
			User updatedUser = userService.update(user);
			user=updatedUser;
		}
		return new UserAdminDTO(user.getId(),user.getEmail(),user.isEnabled());
	}
	
	private UserAdminDTO deleteUserFromDTO(UserAdminDTO dto) {
		User user = userService.findUserByEmail(dto.getEmail());
		userService.delete(user);
		return new UserAdminDTO(user.getId(),user.getEmail(),user.isEnabled());
	}
}
