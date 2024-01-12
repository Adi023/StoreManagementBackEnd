package com.adityaprojects.store.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;

	//spring security will use this in order to configure user
	//by this it will use loadUserByUsername method
//	@Bean 
//	public UserDetailsService userDetailsService() {
//		
//		UserDetails normal=User.builder()
//		.username("DemoUser")
//		.password(passwordEncoder().encode("Aditya"))
//		.roles("NORMAL")
//		.build();
//		
//		UserDetails admin=User.builder()
//		.username("Aditya")
//		.password(passwordEncoder().encode("ADMIN"))
//		.roles("ADMIN")
//		.build();
//		
//		//users create
//		//InMemoryUserDetailsManager - is implementation class of UserDetailService
//		return new InMemoryUserDetailsManager(normal,admin);
//	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
//		http.authorizeRequests()
//				.anyRequest()
//				.authenticated()
//				.and()
//				.formLogin()
//				.loginPage("login.html")
//				.loginProcessingUrl("process-url")
//				.defaultSuccessUrl("/dashboard")
//				.failureUrl("error")
//				.and()
//				.logout()
//				.logoutUrl("do-logout")
			http.
			csrf().disable()
			.cors().disable()
			.authorizeRequests()
			.anyRequest()
			.authenticated()
			.and()
			.httpBasic();

				return http.build();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider daoAuthenticationProvider =new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return daoAuthenticationProvider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
		
}