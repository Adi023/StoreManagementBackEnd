package com.adityaprojects.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;



@Configuration
public class SecurityConfig {

	//spring security will use this in order to configure user
	//by this it will use loadUserByUsername method
	@Bean 
	public UserDetailsService userDetailsService() {
		
		UserDetails normal=User.builder()
		.username("DemoUser")
		.password(passwordEncoder().encode("Aditya"))
		.roles("NORMAL")
		.build();
		
		UserDetails admin=User.builder()
		.username("Aditya")
		.password(passwordEncoder().encode("ADMIN"))
		.roles("ADMIN")
		.build();
		
		//users create
		//InMemoryUserDetailsManager - is implementation class of UserDetailService
		return new InMemoryUserDetailsManager(normal,admin);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
		
}