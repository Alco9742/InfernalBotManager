package net.nilsghesquiere.initialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import net.nilsghesquiere.entities.InfernalSettings;
import net.nilsghesquiere.entities.Privilege;
import net.nilsghesquiere.entities.Role;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.InfernalSettingsService;
import net.nilsghesquiere.service.web.PrivilegeService;
import net.nilsghesquiere.service.web.RoleService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.enums.RoleEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(InitialDataLoader.class);
	boolean alreadySetup = false;
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;
	  
	@Autowired
	private PrivilegeService privilegeService;

	@Autowired
	private InfernalSettingsService infernalSettingsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (alreadySetup){
			return;
		}
		// == create initial privileges
		final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
		final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
		final Privilege passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");
		
		// == create initial roles
		final List<Privilege> adminPrivileges = new ArrayList<Privilege>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
		final List<Privilege> userPrivileges = new ArrayList<Privilege>(Arrays.asList(readPrivilege, passwordPrivilege));
		
		final Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
		final Role userRole = createRoleIfNotFound("ROLE_USER", userPrivileges);
		
		// == create initial users
		createUserIfNotFound("ghesquiere.nils@gmail.com", "Syntra1234", new ArrayList<Role>(Arrays.asList(adminRole)));
		createUserIfNotFound("ghesquiere.test@gmail.com", "Syntra1234", new ArrayList<Role>(Arrays.asList(userRole)));
		
		alreadySetup = true;

	}
	
	@Transactional
	private Privilege createPrivilegeIfNotFound(String name) {
		Privilege privilege = privilegeService.findByName(name);
		if (privilege == null) {
			privilege = new Privilege(name);
			privilegeService.create(privilege);
		}
		return privilege;
	}
	 
	@Transactional
	private Role createRoleIfNotFound(
		String name, Collection<Privilege> privileges) {
		Role role = roleService.findByName(name);
		if (role == null) {
			role = new Role(name);
			role.setPrivileges(privileges);
			roleService.create(role);
		}
		return role;
	}

	@Transactional
	private final User createUserIfNotFound(final String email, final String password, final Collection<Role> roles) {
		User user = userService.findUserByEmail(email);
		if (user == null) {
			user = new User();
			user.setPassword(passwordEncoder.encode(password));
			user.setEmail(email);
			user.setEnabled(true);
			user.setRoles(roles);
			userService.create(user);
			User createdUser = userService.findUserByEmail(email);
			InfernalSettings inferalSettings = infernalSettingsService.create(new InfernalSettings(createdUser));
			user.setInfernalSettings(inferalSettings);
			user = createdUser;
		}
		return user;
	}
}
