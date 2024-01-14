package com.adityaprojects.store.dto;

import java.util.HashSet;
import java.util.Set;

import com.adityaprojects.store.validate.ImageNameValid;

//import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

	private String userId;

	@Size(min=3 ,max=15,message = "Invalid User Name !!")
	private String name;

//	@Email(message="Invalid User Email !!")
	@NotBlank(message="Email is required")
	@Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z]+\\.)+[a-z]{2,5}$",message = "Invalid User Email !!")//Pattern validaton
	private String email;

	@NotBlank(message="Password is required")
	private String password;

	@Size(min=4 , max=6,message="Invalid Gender !!")
	private String gender;

	@NotBlank(message="Write Something About  Yourself !!")
	private String about;

	
	//Custom validator
	@ImageNameValid
	private String imageName;
	
	private Set<RoleDto> roles=new HashSet<>();

}
