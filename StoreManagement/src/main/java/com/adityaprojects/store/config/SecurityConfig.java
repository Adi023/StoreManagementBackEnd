package com.adityaprojects.store.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.adityaprojects.store.security.JwtAuthenticationEntryPoint;
import com.adityaprojects.store.security.JwtAuthenticationFilter;



@Configuration
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;

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
			http
			.csrf()
			.disable()
			.cors()
			.disable()
			.authorizeHttpRequests()
			.requestMatchers("/auth/login")
			.permitAll()
			.requestMatchers(HttpMethod.POST,"/users")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint)
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			
			http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
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
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		return builder.getAuthenticationManager();
	}
		
}