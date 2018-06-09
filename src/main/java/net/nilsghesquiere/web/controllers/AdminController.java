package net.nilsghesquiere.web.controllers;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

import net.nilsghesquiere.entities.User;
import net.nilsghesquiere.security.IUserSecurityService;
import net.nilsghesquiere.service.web.GlobalVariableService;
import net.nilsghesquiere.service.web.StorageService;
import net.nilsghesquiere.service.web.UserService;
import net.nilsghesquiere.util.facades.AuthenticationFacade;
import net.nilsghesquiere.web.annotations.ViewController;
import net.nilsghesquiere.web.error.StorageFileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@ViewController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
	private static final String PANEL_VIEW = "admin/panel";
	private static final String GLOBAL_VARS_VIEW = "admin/globalvars";
	private static final String METRICS_VIEW = "admin/metrics";
	private static final String USERS_VIEW = "admin/users";
	private static final String FILES_VIEW = "admin/files";
	private static final String LOGINATTEMPTS_VIEW = "admin/loginattempts";
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@Autowired
	private StorageService storageService;
	
	//PANEL
	@RequestMapping(method = RequestMethod.GET)
	ModelAndView panel() {
		Optional<User> currentUser = authenticationFacade.getOptionalAuthenticatedUser();
		LOGGER.info("Loading admin page for " + currentUser.get().getEmail());
		return new ModelAndView(PANEL_VIEW).addObject("currentUser");
	}

	//METRICS
	@RequestMapping(value = "/metrics", method = RequestMethod.GET)
	ModelAndView metrics() {
		return new ModelAndView(METRICS_VIEW);
	}
	
	//GLOBAL VARS
	@RequestMapping(value = "/globalvars", method = RequestMethod.GET)
	ModelAndView globalVars() {
		return new ModelAndView(GLOBAL_VARS_VIEW);
	}

	//GLOBAL VARS
	@RequestMapping(value = "/loginattempts", method = RequestMethod.GET)
	ModelAndView loginAttempts() {
		return new ModelAndView(LOGINATTEMPTS_VIEW);
	}
	
	//LOGIN ATTEMPTS
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	ModelAndView users() {
		return new ModelAndView(USERS_VIEW);
	}
	
	//Everything below is copied straight from a spring tutorial
	@RequestMapping(value= "/files", method = RequestMethod.GET)
	String files(Model model) throws IOException {
		model.addAttribute("files", storageService.loadAll().map(path -> MvcUriComponentsBuilder.fromMethodName(AdminController.class,"serveFile", path.getFileName().toString()).build().toString()).collect(Collectors.toList()));
		return FILES_VIEW;
	}
	
	@RequestMapping(value="/files/{filename:.+}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
	
	@RequestMapping(value="/files", method = RequestMethod.POST)
	public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		storageService.store(file);
		redirectAttributes.addFlashAttribute("message","You successfully uploaded " + file.getOriginalFilename() + "!");
		return "redirect:/admin/files/";
	}
	
	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
}