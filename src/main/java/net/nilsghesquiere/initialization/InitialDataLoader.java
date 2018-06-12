package net.nilsghesquiere.initialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import net.nilsghesquiere.entities.GlobalVariable;
import net.nilsghesquiere.entities.Privilege;
import net.nilsghesquiere.entities.Role;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.entities.UserSettings;
import net.nilsghesquiere.service.web.SystemTasksService;
import net.nilsghesquiere.service.web.UserService;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(InitialDataLoader.class);
	boolean alreadySetup = false;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SystemTasksService systemTasksService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@SuppressWarnings("unused")
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
		final List<Privilege> systemPrivileges = new ArrayList<Privilege>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
		final List<Privilege> adminPrivileges = new ArrayList<Privilege>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
		final List<Privilege> moderatorPrivileges = new ArrayList<Privilege>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
		final List<Privilege> userPrivileges = new ArrayList<Privilege>(Arrays.asList(readPrivilege, passwordPrivilege));
		
		final Role systemRole = createRoleIfNotFound("ROLE_SYSTEM", systemPrivileges);
		final Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
		final Role userRole = createRoleIfNotFound("ROLE_USER", userPrivileges);
		final Role moderatorRole = createRoleIfNotFound("ROLE_MODERATOR", moderatorPrivileges);
		final Role paidUserRole = createRoleIfNotFound("ROLE_PAID_USER", userPrivileges);
		
		// == create initial users
		createUserIfNotFound("system", "NfAq5cKKRRgc2LvkfMPN", new ArrayList<Role>(Arrays.asList(systemRole)));
		createUserIfNotFound("ghesquiere.nils@gmail.com", "OwAgojg2FDrpXfbtNCWQ", new ArrayList<Role>(Arrays.asList(adminRole)));
		createUserIfNotFound("ghesquiere.moderator@gmail.com", "OwAgojg2FDrpXfbtNCWQ", new ArrayList<Role>(Arrays.asList(moderatorRole)));
		createUserIfNotFound("ghesquiere.user@gmail.com", "OwAgojg2FDrpXfbtNCWQ", new ArrayList<Role>(Arrays.asList(userRole)));
		
		// == create initial global vars
		createGlobalVariableIfNotFound("connection", "Connected");
		createGlobalVariableIfNotFound("killSwitch", "on");
		createGlobalVariableIfNotFound("killSwitchMessage", "InfernalBotManager is currently disabled");
		createGlobalVariableIfNotFound("serverVersion", "x.x.x");
		createGlobalVariableIfNotFound("clientVersion", "x.x.x");
		createGlobalVariableIfNotFound("update", "No");
		
		alreadySetup = true;

	}
	
	@Transactional
	private Privilege createPrivilegeIfNotFound(String name) {
		Privilege privilege = systemTasksService.findPrivilegeByName(name);
		if (privilege == null) {
			privilege = new Privilege(name);
			systemTasksService.createPrivilege(privilege);
		}
		return privilege;
	}
	 
	@Transactional
	private Role createRoleIfNotFound(
		String name, Collection<Privilege> privileges) {
		Role role = systemTasksService.findRoleByName(name);
		if (role == null) {
			role = new Role(name);
			role.setPrivileges(privileges);
			systemTasksService.createRole(role);
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
			user.setUserSettings(new UserSettings());
			userService.create(user);
			User createdUser = userService.findUserByEmail(email);
		//	UserSettings userSettings = new UserSettings();
		//	userSettings.setUser(createdUser);
		//	userSettingsService.create(userSettings);
			user = createdUser;
		}
		return user;
	}

	@Transactional
	private GlobalVariable createGlobalVariableIfNotFound(String name, String value) {
		GlobalVariable var = systemTasksService.findGlobalVariableByName(name);
		if (var == null) {
			var = new GlobalVariable(name, value);
			systemTasksService.createGlobalVariable(var);
		}
		return var;
	}
	
}
