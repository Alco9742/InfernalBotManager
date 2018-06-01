package net.nilsghesquiere.configuration;

import net.nilsghesquiere.util.enums.RoleEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
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
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;




@Configuration
@ComponentScan("net.nilsghesquiere.security")
public class SecurityConfiguration{

	@Configuration
	@EnableResourceServer
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	public class OAuth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
		@Override
		public  void configure(HttpSecurity http) throws Exception {
			http
				.antMatcher("/api/**")
				.authorizeRequests()
					.anyRequest().authenticated()
				.and()
					.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}
	
	}


	@EnableWebSecurity
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{
		@Autowired
		@Qualifier("userDetailsService")
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
			.antMatchers("/favicon.ico") 
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
					.antMatchers("/queuers/**").hasAnyRole(RoleEnum.ADMIN.getName(),RoleEnum.USER.getName(),RoleEnum.PAID_USER.getName())
					.antMatchers("/clients/**").hasAnyRole(RoleEnum.ADMIN.getName(),RoleEnum.USER.getName(),RoleEnum.PAID_USER.getName())
					.antMatchers("/settings/**").hasAnyRole(RoleEnum.ADMIN.getName(),RoleEnum.USER.getName(),RoleEnum.PAID_USER.getName())
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
					.antMatchers("/oauth/**").permitAll()
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
					.maximumSessions(3)
					.expiredUrl("/sessionExpired");
		}
	
		@Bean
		public HttpSessionEventPublisher httpSessionEventPublisher() {
			return new HttpSessionEventPublisher();
		}
	}
}

