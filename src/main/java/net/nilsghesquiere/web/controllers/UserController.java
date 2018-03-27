package net.nilsghesquiere.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.web.dto.InfernalSettingsDTO;
import net.nilsghesquiere.web.dto.UserChangePasswordDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	private static final String USER_SETTINGS_VIEW= "user/settings";
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//HOME
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request) {
		UserChangePasswordDTO dto = new UserChangePasswordDTO();
		String result = (String) request.getSession().getAttribute("resultM");
		System.out.println(result);
		if (result != null && !result.isEmpty()){
			request.getSession().removeAttribute("resultM");
			if(result.toLowerCase().contains("success")){
				return new ModelAndView(USER_SETTINGS_VIEW).addObject("passForm",dto).addObject("successM", result);
			}
			if(result.toLowerCase().contains("failure")){
				return new ModelAndView(USER_SETTINGS_VIEW).addObject("passForm",dto).addObject("failM", result);
			}
			return new ModelAndView(USER_SETTINGS_VIEW).addObject("passForm",dto).addObject("resultM", result);
		} else {
			return new ModelAndView(USER_SETTINGS_VIEW).addObject("passForm",dto);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String savePassword(@ModelAttribute("passForm") @Valid UserChangePasswordDTO passwordDTO, BindingResult bindingResult,HttpServletRequest request) {
		System.out.println(passwordDTO.getOldPassword());
		System.out.println(passwordDTO.getNewPassword());
		System.out.println(passwordDTO.getMatchingPassword());
		if(bindingResult.hasErrors()) {
			System.out.println("test1");
			System.out.println(bindingResult.toString());
			return USER_SETTINGS_VIEW;
		}
		System.out.println("test2");
		User user = authenticationFacade.getAuthenticatedUser();
		if (!passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())){
			System.out.println("test3");
			request.getSession().setAttribute("resultM","Failure: old password is not correct");
			return "redirect:/user";	
		}
		System.out.println("test4");
		userService.changeUserPassword(user, passwordDTO.getNewPassword());
		request.getSession().setAttribute("resultM","Succesfully changed InfernalBot settings!");
		return "redirect:/user";
	}
	
}