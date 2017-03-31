package net.nilsghesquiere.web;

import java.util.Optional;

import net.nilsghesquiere.entities.AppUser;
import net.nilsghesquiere.facades.AuthenticationFacade;
import net.nilsghesquiere.services.LolAccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.summoner.Summoner;

@Controller
@RequestMapping("/test")
@PreAuthorize("hasAuthority('appadmin')")
public class TestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
	private static final String PANEL_VIEW = "test/panel";
	private static final String RATES_VIEW = "test/rates";
	private static final String SUMMONER_VIEW = "test/summoner";
	
	private final AuthenticationFacade authenticationFacade;
	private final LolAccountService lolAccountService;

	@Autowired
	public TestController(AuthenticationFacade authenticationFacade, LolAccountService lolAccountService) {
		this.lolAccountService = lolAccountService;
		this.authenticationFacade = authenticationFacade;
	}
	
	//PANEL
	@RequestMapping(method = RequestMethod.GET)
	ModelAndView panel() {
		Optional<AppUser> currentUser = authenticationFacade.getOptionalAuthenticatedUser();
		LOGGER.info("Loading test panel for admin " + currentUser.get().getUsername());
		return new ModelAndView(PANEL_VIEW).addObject("currentUser",currentUser.get());
	}
	
	@RequestMapping(path="rates", method = RequestMethod.GET)
	ModelAndView rates() {
		return new ModelAndView(RATES_VIEW);
	}

	@RequestMapping(path="summoner", method = RequestMethod.GET)
	ModelAndView summoner() {
		Orianna.setRiotAPIKey("RGAPI-9d5e09b0-bbc2-42e4-be6b-6d04f40e091c");
		final Summoner summoner = Summoner.named("Pismerito").withRegion(Region.EUROPE_WEST).get();
		return new ModelAndView(SUMMONER_VIEW).addObject(summoner);
	}
	
}