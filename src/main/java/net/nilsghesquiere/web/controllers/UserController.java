package net.nilsghesquiere.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.nilsghesquiere.entities.LolAccount;
import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.LolAccountService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.enums.AccountStatus;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.web.annotations.ViewController;
import net.nilsghesquiere.web.dto.UserChangePasswordDTO;
import net.nilsghesquiere.web.error.UserIsNotOwnerOfResourceException;
import net.nilsghesquiere.web.util.GenericResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ViewController
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
public class UserController {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	private static final String USER_SETTINGS_VIEW= "user/settings";
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private LolAccountService lolAccountService;
	
	//HOME
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request) {
		UserChangePasswordDTO dto = new UserChangePasswordDTO();
		String result = (String) request.getSession().getAttribute("resultM");
		if (result != null && !result.isEmpty()){
			request.getSession().removeAttribute("resultM");
			if(result.toLowerCase().contains("success")){
				return new ModelAndView(USER_SETTINGS_VIEW).addObject("passForm",dto).addObject("successM", result);
			}
			if(result.toLowerCase().contains("failure")){
				@SuppressWarnings("unchecked")
				List<String> errorList = (List<String>) request.getSession().getAttribute("errorList");
				request.getSession().removeAttribute("errorList");
				return new ModelAndView(USER_SETTINGS_VIEW).addObject("passForm",dto).addObject("failM", result).addObject("errorList", errorList);
			}
			return new ModelAndView(USER_SETTINGS_VIEW).addObject("passForm",dto).addObject("resultM", result);
		} else {
			return new ModelAndView(USER_SETTINGS_VIEW).addObject("passForm",dto);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String savePassword(@ModelAttribute("passForm") @Valid UserChangePasswordDTO passForm, BindingResult bindingResult,HttpServletRequest request) {
		List<String> errorList = new ArrayList<>();
		User user = authenticationFacade.getAuthenticatedUser();
		if (!passwordEncoder.matches(passForm.getOldPassword(), user.getPassword())){
			errorList.add("Old password is incorrect");
		}
		if(bindingResult.hasErrors()) {
			for( ObjectError error : bindingResult.getAllErrors()){
				String defaultMsg = error.getDefaultMessage();
				if(defaultMsg.contains("<br/>")){
					for( String msg : defaultMsg.split("<br/>")){
						if(!msg.isEmpty()){
							if(!errorList.contains(msg)){
								errorList.add(msg);
							}
						}
					}
				} else {
					errorList.add(defaultMsg);
				}
			}
		}
		
		if (!errorList.isEmpty()){
			request.getSession().setAttribute("resultM","Failure changing password:");
			request.getSession().setAttribute("errorList",errorList);
			return "redirect:/user";	
		} else {
			userService.changeUserPassword(user, passForm.getNewPassword());
			request.getSession().setAttribute("resultM","Succesfully changed password!");
			return "redirect:/user";
		}
	}
	
	@RequestMapping(path = "/user/resetAllStatus",method = RequestMethod.POST)
	public String resetAllStatus(HttpServletRequest request) {
		//VARS
		int aantalAccounts = 0;
	
		//USER CHECK
		User user = authenticationFacade.getAuthenticatedUser();
		
		//PROCESSING
		for (LolAccount lolAccount : lolAccountService.findByUser(user)){
			lolAccount.setAccountStatus(AccountStatus.READY_FOR_USE);
			lolAccount.setAssignedTo("");
			lolAccountService.update(lolAccount);
			aantalAccounts += 1;
		}
		//RESPONSE
		request.getSession().setAttribute("resultM","Succesfully reset the status of " + aantalAccounts + " accounts.");
		return "redirect:/user";
	}
	
}