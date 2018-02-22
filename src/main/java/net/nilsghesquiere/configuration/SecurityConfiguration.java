package net.nilsghesquiere.configuration;

import net.nilsghesquiere.enums.UserType;
import net.nilsghesquiere.security.MyUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;


@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(2)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	//private static final String USERS_BY_USERNAME =
	//		"select username as username, password as password, enabled as enabled" +
	//		" from users where username = ?";
	//private static final String AUTHORITIES_BY_USERNAME =
	//		"select users.username as username, roles.name as authorities" +
	//		" from users inner join userroles" +
	//		" on users.id = userroles.userid" +
	//		" inner join roles" +
	//		" on roles.id = userroles.roleid" +
	//		" where users.username= ?";
	
	//@Override
	//@Autowired
	//public void configure(AuthenticationManagerBuilder auth) throws Exception {
	//	auth
	//		.jdbcAuthentication().dataSource(dataSource)
	//		.usersByUsernameQuery(USERS_BY_USERNAME)
	//		.authoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME);
	//		//.passwordEncoder(new BCryptPasswordEncoder());
	//}
	@Autowired
	private final UserDetailsService userDetailsService;
	

	public SecurityConfiguration(UserDetailsService userDetailsService){
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception { 
		web.ignoring()
		.antMatchers("/images/**") 
		.antMatchers("/css/**")
		.antMatchers("/js/**")
		.antMatchers("/webjars/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
				.disable()
			.authorizeRequests()
				.antMatchers("/admin/**").hasAuthority(UserType.ADMIN.getName())
				.antMatchers("/account/**").hasAnyAuthority(UserType.ADMIN.getName(),UserType.USER.getName())
				.antMatchers("/anonymous*").anonymous()
				.antMatchers("/login*").permitAll()
				.antMatchers("/perform_login").permitAll()
				.antMatchers("/test/**").permitAll()
				.antMatchers("/init/**").permitAll()
				.antMatchers("/api/**").permitAll()
				.antMatchers("/").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login.html")
				.defaultSuccessUrl("/homepage.html", true)
				.failureUrl("/login.html?error=true")
				.and()
			.logout()
				.logoutUrl("/perform_logout")
				.deleteCookies("JSESSIONID")
				.logoutSuccessUrl("/login.html");
	}

}