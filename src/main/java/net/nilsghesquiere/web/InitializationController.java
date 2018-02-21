package net.nilsghesquiere.web;

import java.util.HashSet;
import java.util.Set;

import net.nilsghesquiere.entities.AppUser;
import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.entities.Role;
import net.nilsghesquiere.enums.Server;
import net.nilsghesquiere.facades.AuthenticationFacade;
import net.nilsghesquiere.services.LolAccountService;
import net.nilsghesquiere.services.RoleService;
import net.nilsghesquiere.services.UserService;

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
	
	private final RoleService roleService;
	private final UserService userService;
	private final LolAccountService lolAccountService;
	
	@Autowired
	public InitializationController(RoleService roleService, UserService userService, LolAccountService lolAccountService) {
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
			Role role1 = new Role("appadmin");
			Role role2 = new Role("basicuser");
			roleService.create(role1);
			roleService.create(role2);
			
			//create the user and commit to DB
			Set<Role> roles = new  HashSet<>();
			roles.add(role1);
			AppUser user = new AppUser("NilsGhes","Syntra1234",roles, true);
			userService.create(user);
			LOGGER.info("Created initial roles and user");
		} else {
			LOGGER.warn("Initial roles and user already created");
		}
		return VIEW;
	}
	
	@RequestMapping(path="extended",method = RequestMethod.GET)
	public String extendedInit() {
		AppUser user = userService.findByUsername("NilsGhes").get();
		LolAccount lolAccount = new LolAccount("Pismerito","EdGOY4Xt",Server.EUROPE_WEST,30L, true);
		lolAccount.setUser(user);
		lolAccountService.create(lolAccount);
		return VIEW;
	}

	@RequestMapping(path="testuser",method = RequestMethod.GET)
	public String testUserInit() {
		Role role1 = roleService.read(8L);
		Set<Role> roles = new  HashSet<>();
		roles.add(role1);
		AppUser user = new AppUser("TestUser","Test123",roles, true);
		userService.create(user);
		AppUser createdUser = userService.findByUsername("TestUser").get();
		LolAccount lolAccount = new LolAccount("Pismerito","EdGOY4Xt",Server.EUROPE_WEST,30L, true);
		lolAccount.setUser(createdUser);
		lolAccountService.create(lolAccount);
		return VIEW;
	}
}
