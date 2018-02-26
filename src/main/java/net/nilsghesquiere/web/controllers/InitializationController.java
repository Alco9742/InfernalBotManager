package net.nilsghesquiere.web.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.entities.Role;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.ILolAccountService;
import net.nilsghesquiere.service.web.IRoleService;
import net.nilsghesquiere.service.web.IUserService;
import net.nilsghesquiere.util.enums.Server;
import net.nilsghesquiere.util.enums.UserType;
import net.nilsghesquiere.web.dto.UserDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/init")
public class InitializationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationController.class);
	private static final String VIEW = "index";
	
	private final IRoleService roleService;
	private final IUserService userService;
	private final ILolAccountService lolAccountService;
	
	@Autowired
	public InitializationController(IRoleService roleService, IUserService userService, ILolAccountService lolAccountService) {
		this.roleService = roleService;
		this.userService = userService;
		this.lolAccountService = lolAccountService;
	}
	
	// Setup user for login
	@RequestMapping(path="main",method = RequestMethod.GET)
	public String init() {
		if (roleService.findAll().size() == 0 ){
			//create the roles and commit to DB
			Role role1 = new Role(UserType.ADMIN.getName());
			Role role2 = new Role(UserType.USER.getName());
			roleService.create(role1);
			roleService.create(role2);
			
			//create the user and commit to DB
			UserDTO userDTO = new UserDTO("ghesquiere.nils@gmail.com", "Syntra1234");
			userService.registerNewUserAccount(userDTO);
			List<Role> roles = new  ArrayList<>();
			roles.add(role1);
			User createdUser = userService.findUserByEmail("ghesquiere.nils@gmail.com");
			createdUser.setEnabled(true);
			createdUser.setRoles(roles);
			userService.update(createdUser);
			userService.createVerificationTokenForUser(createdUser, UUID.randomUUID().toString());
			LOGGER.info("Created initial roles and user");
		} else {
			LOGGER.warn("Initial roles and user already created");
		}
		return VIEW;
	}
	
	@RequestMapping(path="extended",method = RequestMethod.GET)
	public String extendedInit() {
		User user = userService.findUserByEmail("ghesquiere.nils@gmail.com");
		LolAccount lolAccount = new LolAccount("Pismerito","EdGOY4Xt",Server.EUROPE_WEST,30L, true);
		lolAccount.setUser(user);
		lolAccountService.create(lolAccount);
		return VIEW;
	}

	@RequestMapping(path="testuser",method = RequestMethod.GET)
	public String testUserInit() {
		UserDTO userDTO = new UserDTO("testuser@gmail.com","Test123");
		userService.registerNewUserAccount(userDTO);
		User createdUser = userService.findUserByEmail("testuser@gmail.com");
		createdUser.setEnabled(true);
		userService.update(createdUser);
		userService.createVerificationTokenForUser(createdUser, UUID.randomUUID().toString());
		LolAccount lolAccount = new LolAccount("Pismerito","EdGOY4Xt",Server.EUROPE_WEST,30L, true);
		lolAccount.setUser(createdUser);
		lolAccountService.create(lolAccount);
		return VIEW;
	}
}
