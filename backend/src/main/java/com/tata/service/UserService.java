package com.tata.service;

import java.util.List;

import com.tata.payloads.UserDto;

public interface UserService {

	UserDto registerUser(UserDto userDto);
	
	UserDto saveUser(UserDto userDto);

	List<UserDto> getAllUser();

	UserDto getUserById(Integer userId);

	UserDto updateUser(UserDto userDto, Integer userID);
	
	public void deleteUser(Integer userId);
	
	void generateResetToken(String email);
	
	void resetPassword(String token, String newPassword);
	
	void changePassword(String email, String oldPassword, String newPassword);
}
