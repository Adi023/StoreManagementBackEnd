package com.adityaprojects.store.controller;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adityaprojects.store.dto.JwtRequest;
import com.adityaprojects.store.dto.JwtResponse;
import com.adityaprojects.store.dto.UserDto;
import com.adityaprojects.store.exceptions.BadApiRequestException;
import com.adityaprojects.store.security.JwtHelper;
import com.adityaprojects.store.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtHelper helper;

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
		this.doAuthenticate(request.getEmail(), request.getPassword());

		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

		String token = this.helper.generateToken(userDetails);

		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		JwtResponse response = JwtResponse.builder().jwtToken(token).user(userDto).build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private void doAuthenticate(String email, String password) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);

		try {
			manager.authenticate(authentication);
		}
		catch (BadCredentialsException e) {
			throw new BadApiRequestException("Invalid Username Password !!");
		}
	}

	@GetMapping("/current")
	public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
		String name = principal.getName();
		return new ResponseEntity<>(modelMapper.map(userDetailsService.loadUserByUsername(name), UserDto.class),
				HttpStatus.OK);
	}

}
