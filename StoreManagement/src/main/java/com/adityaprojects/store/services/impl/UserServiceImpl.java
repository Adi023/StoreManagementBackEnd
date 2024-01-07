package com.adityaprojects.store.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.adityaprojects.store.dto.PageableResponse;
import com.adityaprojects.store.dto.UserDto;
import com.adityaprojects.store.entities.User;
import com.adityaprojects.store.exceptions.ResourceNotFoundException;
import com.adityaprojects.store.helper.Helper;
import com.adityaprojects.store.repositories.UserRepository;
import com.adityaprojects.store.services.UserService;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Value("${user.profile.image.path}")
	private String imagePath;
	
	private Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Override
	public UserDto createUser(UserDto userDto) {
		//generate unique id in string format
		String userId=UUID.randomUUID().toString();
		userDto.setUserId(userId);
		
		//dto -> Entity
		User user=dtoToEntity(userDto);
		
		User savedUser=userRepository.save(user);
		
		//Entity -> dto
		UserDto newDto=entityToDto(savedUser);
		
		return newDto;
	}
	
	

	@Override
	public UserDto updateUser(UserDto userDto, String userId) {
		
		User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Not Found With this ID..!!"));		
		user.setName(userDto.getName());
//		user.setEmail(userDto.getEmail());
		user.setAbout(userDto.getAbout());
		user.setGender(userDto.getGender());
		user.setPassword(userDto.getPassword());
		user.setImageName(userDto.getImageName());
		
		//save data
		User updatedUser=userRepository.save(user);
		UserDto updatedDto=entityToDto(updatedUser);
		
		return updatedDto;
	}

	@Override
	public void deleteUser(String userId) {
		User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Not Found With this ID !!"));		
		
		//delete user profile image
		String fullPath=imagePath + user.getImageName();
		
		try {
			Path path=Paths.get(fullPath);
			Files.delete(path);
		}
		catch(NoSuchFileException ex) {
			logger.info("User image not found in folder");
			ex.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		//delete user
		userRepository.delete(user);
	}

	@Override
	public PageableResponse<UserDto> getAllUser(int pageNumber,int pageSize,String sortBy,String sortDir) {
		
//		Sort sort=Sort.by(sortBy);
		
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		
		//pageNumber default start from 0
		Pageable pagebale = PageRequest.of(pageNumber, pageSize,sort);
		
		//if you want pageNumber start with 1 then use this and also check getPageableResponse in Helper class
		//Pageable pagebale = PageRequest.of(pageNumber-1, pageSize,sort);
		
		Page<User> page=userRepository.findAll(pagebale);
		
		//getPageableResponse method is made in helper class of Helper package
		PageableResponse<UserDto> response=Helper.getPageableResponse(page, UserDto.class);
		
		return response;
	}

	@Override
	public UserDto getUserById(String userId) {
		User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Not Found with this ID !!"));
		
		return entityToDto(user);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		User user=userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User Not found with this email  !!"));
		return entityToDto(user);
	}

	@Override
	public List<UserDto> searchUser(String keyword) {
		List<User> users=userRepository.findByNameContaining(keyword);
		List<UserDto> dtoList=users.stream().map(user->entityToDto(user)).collect(Collectors.toList());
		return dtoList;
	}

	//this mehod converting entity to dto
	private UserDto entityToDto(User savedUser) {
//		UserDto userDto=UserDto.builder()
//		.userId(savedUser.getUserId())
//		.name(savedUser.getName())
//		.email(savedUser.getEmail())
//		.password(savedUser.getPassword())
//		.about(savedUser.getAbout())
//		.gender(savedUser.getGender())
//		.imageName(savedUser.getImageName())
//		.build();
		
		return mapper.map(savedUser, UserDto.class);
	}

	//this method converting dto to entity
	private User dtoToEntity(UserDto userDto) {
//		User user=User.builder()
//		.userId(userDto.getUserId())
//		.name(userDto.getName())
//		.email(userDto.getEmail())
//		.password(userDto.getPassword())
//		.about(userDto.getAbout())
//		.gender(userDto.getGender())
//		.imageName(userDto.getImageName())
//		.build();
		
		return mapper.map(userDto, User.class);
	}
	
}
