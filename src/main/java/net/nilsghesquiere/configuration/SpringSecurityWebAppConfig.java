package net.nilsghesquiere.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityWebAppConfig extends WebSecurityConfigurerAdapter {
	private static final String APPADMIN ="appadmin";
	private static final String BASICUSER ="basicuser";
	private static final String USERS_BY_USERNAME =
			"select username as username, password as password, enabled as enabled" +
			" from users where username = ?";
	private static final String AUTHORITIES_BY_USERNAME =
			"select users.username as username, roles.name as authorities" +
			" from users inner join userroles" +
			" on users.id = userroles.userid" +
			" inner join roles" +
			" on roles.id = userroles.roleid" +
			" where users.username= ?";
	
	@Autowired
	private DataSource dataSource;
	
	@Override
	@Autowired
	public void configure(AuthenticationManagerBuilder auth)
	throws Exception {
	auth
	.jdbcAuthentication().dataSource(dataSource)
	.usersByUsernameQuery(USERS_BY_USERNAME)
	.authoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME);
	//.passwordEncoder(new BCryptPasswordEncoder());
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
        http.formLogin()
        .loginPage("/login.html")
        .failureUrl("/login-error.html")
        .defaultSuccessUrl("/", true)
        .and()
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/")
        .and()
        .authorizeRequests()
        .antMatchers("/admin/**").hasAuthority(APPADMIN)
        .antMatchers("/account/**").hasAnyAuthority(BASICUSER,APPADMIN)
        .antMatchers("/test/**").permitAll()
        .antMatchers("/init/**").permitAll()
        .antMatchers("/api/**").permitAll()
        .antMatchers("/").permitAll()
        .antMatchers("/login.html").permitAll()
        .antMatchers("/forbidden.html").permitAll()
        .antMatchers("/**").authenticated();
        
       //H2 console
		http.csrf().disable();
        http.headers().frameOptions().disable();
	}
}