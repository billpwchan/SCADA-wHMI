package com.thalesgroup.scadasoft.opmmgt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()   // temporarily disable CSRF to use curl for debug
			.authorizeRequests()
				.antMatchers("/scs/arequest").hasRole("API_USER")
				.antMatchers("/scs/**").permitAll() 
				.anyRequest().permitAll()
				.and()
			.httpBasic()
			.and().headers().frameOptions().disable();  // disable X-Frame-Options to be able to embed in iframe
	}
	
	@Value("${ldap.server.url:ldap://127.0.0.1:12389/dc=gts,dc=thalesgroup,dc=com}")
	private String ldapUrl;
	
	@Value("${authent.useldap:false}")
	private boolean useLDAP;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		if (useLDAP) {
		auth
			.ldapAuthentication()
				.userDnPatterns("uid={0},ou=people")
				.groupSearchBase("ou=groups")
				.contextSource()
				.url(ldapUrl);
		} else {
			auth.inMemoryAuthentication()
			.withUser("chief").password("thales").authorities("ROLE_API_USER")
			.and()
			.withUser("bob").password("bob").authorities("ROLE_USER")
			.and()
			.withUser("ben").password("ben").authorities("ROLE_ADMIN");
		}
	}
	
}