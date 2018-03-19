package net.nilsghesquiere.configuration;

import net.nilsghesquiere.util.enums.UserType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(2)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception { 
		web.ignoring()
		.antMatchers("/vendor/**") 
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
				.antMatchers("/admin/files/**").permitAll()
				.antMatchers("/admin/**").hasAuthority(UserType.ADMIN.getName())
				.antMatchers("/account/**").hasAnyAuthority(UserType.ADMIN.getName(),UserType.USER.getName())
				.antMatchers("/clients/**").hasAnyAuthority(UserType.ADMIN.getName(),UserType.USER.getName())
				.antMatchers("/user/updatePassword*", "/user/savePassword*","/updatePassword*").hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
				.antMatchers("/login*").permitAll()
				.antMatchers("/logout*").permitAll()
				.antMatchers("/registration").permitAll()
				.antMatchers("/registered**").permitAll()
				.antMatchers("/baduser").permitAll()
				.antMatchers("/emailError").permitAll()
				.antMatchers("/forgotpassword").permitAll()
				.antMatchers("/changepassword").permitAll()
				.antMatchers("/test/**").permitAll()
				.antMatchers("/init/**").permitAll()
				.antMatchers("/api/**").permitAll()
				.antMatchers("/").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/index", true)
				.failureUrl("/login?error=true")
				.and()
			.logout()
				.logoutUrl("/logout")
				.deleteCookies("JSESSIONID")
				.logoutSuccessUrl("/index");
	}

}