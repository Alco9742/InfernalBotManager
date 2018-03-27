package net.nilsghesquiere.configuration;


import net.nilsghesquiere.security.MySavedRequestAwareAuthenticationSuccessHandler;
import net.nilsghesquiere.security.RestAuthenticationEntryPoint;
import net.nilsghesquiere.util.enums.RoleEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan("net.nilsghesquiere.security")
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Configuration
	@Order(1)
	public class ApiSecurityConfig extends WebSecurityConfigurerAdapter{
		@Autowired
		private UserDetailsService userDetailsService;
		
		@Autowired
		private DaoAuthenticationProvider daoAuthenticationProvider;
		
		@Autowired
		private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
		
		@Autowired
		private MySavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler;
	 	
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(daoAuthenticationProvider);
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.antMatcher("/api/**")
				.csrf()
					.disable()
				.exceptionHandling()
				.and()
				.authorizeRequests()
					.anyRequest().authenticated()
				.and()
				// TODO: Stateless REST is prefered but not workable with grids atm
				//.sessionManagement()
				//	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				//.and()
				.httpBasic()
				.authenticationEntryPoint(restAuthenticationEntryPoint)
				.and()
				.logout();
		}
		
		@Bean
		public MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler(){
			return new MySavedRequestAwareAuthenticationSuccessHandler();
		}
		@Bean
		public SimpleUrlAuthenticationFailureHandler myFailureHandler(){
			return new SimpleUrlAuthenticationFailureHandler();
		}
	}
	
	@Order(2)
	@Configuration
	public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter{
		@Autowired
		private UserDetailsService userDetailsService;
		
		@Autowired
		private AuthenticationFailureHandler authenticationFailureHandler;
		
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
					.antMatchers("/downloads/**").permitAll()
					.antMatchers("/test/**").permitAll()
					.antMatchers("/admin/**").hasRole(RoleEnum.ADMIN.getName())
					.antMatchers("/user/**").hasAnyRole(RoleEnum.ADMIN.getName(),RoleEnum.USER.getName(),RoleEnum.PAID_USER.getName())
					.antMatchers("/accounts/**").hasAnyRole(RoleEnum.ADMIN.getName(),RoleEnum.USER.getName(),RoleEnum.PAID_USER.getName())
					.antMatchers("/clients/**").hasAnyRole(RoleEnum.ADMIN.getName(),RoleEnum.USER.getName(),RoleEnum.PAID_USER.getName())
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
					.antMatchers("/api/**").denyAll()
					.antMatchers("/").permitAll()
					.anyRequest().authenticated()
					.and()
				.formLogin()
					.loginPage("/login")
					.defaultSuccessUrl("/index", true)
					.failureUrl("/login?error=true")
					.failureHandler(authenticationFailureHandler)
					.and()
				.logout()
					.logoutUrl("/logout")
					.deleteCookies("JSESSIONID")
					.logoutSuccessUrl("/index")
					.and()
				.sessionManagement()
					.sessionFixation().migrateSession()
					.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
					.invalidSessionUrl("/invalidSession")
					.maximumSessions(1)
					.expiredUrl("/sessionExpired");
		}
	
		@Bean
		public HttpSessionEventPublisher httpSessionEventPublisher() {
			return new HttpSessionEventPublisher();
		}
	}
}