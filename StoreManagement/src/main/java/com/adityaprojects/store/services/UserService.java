package com.adityaprojects.store.services;

import java.io.IOException;
import java.util.List;

import com.adityaprojects.store.dto.PageableResponse;
import com.adityaprojects.store.dto.UserDto;

public interface UserService {

	//create
	UserDto createUser(UserDto userDto);
	
	//update
	UserDto updateUser(UserDto userDto,String userId);
	
	//delete user by id
	void deleteUser(String userId) throws IOException;
	
	//get  all user 
	PageableResponse<UserDto> getAllUser(int pageNumber,int pageSize,String sortBy,String sortDir);
	
	// get single user by id
	UserDto getUserById(String userId);
	
	// get single user by id
	UserDto getUserByEmail(String email);
	//search user
	List<UserDto> searchUser(String keyword);
	
	//other user specific features
}
