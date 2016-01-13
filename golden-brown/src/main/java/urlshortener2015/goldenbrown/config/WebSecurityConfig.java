package urlshortener2015.goldenbrown.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

import urlshortener2015.goldenbrown.service.SimpleSocialUsersDetailService;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	protected JdbcTemplate jdbc;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/", "/webjars/**", "/css/**", "/js/**", "/images/**", "/fonts/**", "/auth/**", "/hello/**").permitAll()
				.antMatchers(HttpMethod.POST, "/register").permitAll()				
				.antMatchers(HttpMethod.POST, "/hash").permitAll()
				.antMatchers(HttpMethod.POST, "/link").authenticated()
				.antMatchers(HttpMethod.POST, "/update").authenticated()
				.antMatchers(HttpMethod.GET, "/dataUser").authenticated()
				.antMatchers(HttpMethod.POST, "/listLinks").authenticated()
				.antMatchers(HttpMethod.POST, "/deleteRow").authenticated()
				.antMatchers(HttpMethod.POST, "/listUsers").hasAuthority("ROLE_ADMIN")
				.antMatchers(HttpMethod.POST, "/deleteLinksUser").hasAuthority("ROLE_ADMIN")
				.antMatchers("/users").hasAuthority("ROLE_ADMIN")
				.anyRequest().permitAll()
			.and()
			.formLogin()
				.loginPage("/")
				.loginProcessingUrl("/login").permitAll()
			.and()
				.apply(new SpringSocialConfigurer()
					.postLoginUrl("/")
	            	.alwaysUsePostLoginUrl(true))
	        .and()
	        	.logout().permitAll();
    }

	@Bean
    public SocialUserDetailsService socialUsersDetailService() {
        return new SimpleSocialUsersDetailService(userDetailsService());
    }
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.jdbcAuthentication().dataSource(jdbc.getDataSource())
			.usersByUsernameQuery(
					"select username, password, enabled from users where username=?")
			.authoritiesByUsernameQuery(
					"select username, authority from authorities where username=?");
	}
}