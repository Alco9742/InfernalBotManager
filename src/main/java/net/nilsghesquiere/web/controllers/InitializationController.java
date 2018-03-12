package net.nilsghesquiere.web.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.entities.Role;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.LolAccountService;
import net.nilsghesquiere.service.web.RoleService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.enums.Region;
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
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LolAccountService lolAccountService;
	
	// Setup user for login
	@RequestMapping(path="main",method = RequestMethod.GET)
	public String init() {
		if (roleService.findAll().size() == 0 ){
			//create the roles and commit to DB
			Role userRole = new Role(UserType.USER.getName());
			Role adminRole = new Role(UserType.ADMIN.getName());
			roleService.create(userRole);
			roleService.create(adminRole);
			
			//creat userand account and commit to DB
			UserDTO userDTO = new UserDTO("ghesquiere.nils@gmail.com", "Syntra1234");
			User user = userService.registerNewUserAccount(userDTO);
			List<Role> roles = new  ArrayList<>();
			roles.add(userRole);
			roles.add(adminRole);
			user.setEnabled(true);
			user.setRoles(roles);
			userService.createVerificationTokenForUser(user, UUID.randomUUID().toString());
			LolAccount lolAccount = new LolAccount(user,"Pismerito","EdGOY4Xt",Region.EUW);
			lolAccountService.create(lolAccount);
			LOGGER.info("Created initial roles,users,accounts");
		} else {
			LOGGER.warn("Initial roles and user already created");
		}
		return VIEW;
	}
}
