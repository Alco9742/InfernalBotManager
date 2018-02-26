package net.nilsghesquiere.web.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.entities.Role;
import net.nilsghesquiere.service.web.ILolAccountService;
import net.nilsghesquiere.service.web.IRoleService;
import net.nilsghesquiere.service.web.IUserService;
import net.nilsghesquiere.util.enums.Server;
import net.nilsghesquiere.util.enums.UserType;
import net.nilsghesquiere.util.facades.AuthenticationFacade;

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
		//enkel uitvoeren indien er nog geen roles aangemaakt zijn
		if (roleService.findAll().size() == 0 ){
			//create the roles and commit to DB
			Role role1 = new Role(UserType.ADMIN.getName());
			Role role2 = new Role(UserType.USER.getName());
			roleService.create(role1);
			roleService.create(role2);
			
			//create the user and commit to DB
			List<Role> roles = new  ArrayList<>();
			roles.add(role1);
			User user = new User("Ghesquiere.nils@gmail.com", "Syntra1234",roles, true);
			userService.create(user);
			LOGGER.info("Created initial roles and user");
		} else {
			LOGGER.warn("Initial roles and user already created");
		}
		return VIEW;
	}
	
	@RequestMapping(path="extended",method = RequestMethod.GET)
	public String extendedInit() {
		User user = userService.findByEmail("Ghesquiere.nils@gmail.com").get();
		LolAccount lolAccount = new LolAccount("Pismerito","EdGOY4Xt",Server.EUROPE_WEST,30L, true);
		lolAccount.setUser(user);
		lolAccountService.create(lolAccount);
		return VIEW;
	}

	@RequestMapping(path="testuser",method = RequestMethod.GET)
	public String testUserInit() {
		Role role1 = roleService.findByName(UserType.USER.getName());
		List<Role> roles = new  ArrayList<>();
		roles.add(role1);
		User user = new User("testuser@gmail.com","Test123",roles, true);
		userService.create(user);
		User createdUser = userService.findByEmail("testuser@gmail.com").get();
		LolAccount lolAccount = new LolAccount("Pismerito","EdGOY4Xt",Server.EUROPE_WEST,30L, true);
		lolAccount.setUser(createdUser);
		lolAccountService.create(lolAccount);
		return VIEW;
	}
}
