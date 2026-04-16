package com.tata.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tata.config.AppConstant;
import com.tata.entity.Role;
import com.tata.entity.User;
import com.tata.exception.ResourceNotFoundException;
import com.tata.payloads.UserDto;
import com.tata.repo.RoleRepository;
import com.tata.repo.UserRepository;
import com.tata.service.EmailService;
import com.tata.service.UserService;
import com.tata.util.EmailMessages;

@Service
public class UserServiceimpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private EmailService emailService;

	// REGISTER USER
	@Override
	public UserDto registerUser(UserDto userDto) {

		User user = this.modelMapper.map(userDto, User.class);

		// Encode password
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));

		// Assign default role
		Role role = this.roleRepository.findById(AppConstant.NORMAL_USER)
				.orElseThrow(() -> new RuntimeException("Default role not found"));

		user.getRoles().add(role);

		user.setRegisteredAt(LocalDate.now());
		user.setIsActive(true);

		User newUser = this.userRepository.save(user);

		// Send email safely (do not break registration)
		try {
			emailService.sendEmail(newUser.getEmail(), "Welcome to InfoCircle 🎉",
					EmailMessages.getWelcomeMessage(newUser.getUsername()));
		} catch (Exception e) {
			System.out.println("Mail sending failed: " + e.getMessage());
		}

		return this.modelMapper.map(newUser, UserDto.class);
	}

	@Override
	public List<UserDto> getAllUser() {
		return this.userRepository.findAll().stream().map(user -> this.modelMapper.map(user, UserDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public UserDto getUserById(Integer userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		return this.modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {

		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		user.setUserName(userDto.getUserName());
		user.setEmail(userDto.getEmail());
		user.setMobileNumber(userDto.getMobileNumber());
		user.setBio(userDto.getBio());
		user.setAbout(userDto.getAbout());
		user.setProfileImage(userDto.getProfileImage());
		user.setAddress(userDto.getAddress());
		user.setCity(userDto.getCity());
		user.setPincode(userDto.getPincode());

		User updatedUser = this.userRepository.save(user);

		return this.modelMapper.map(updatedUser, UserDto.class);
	}

	@Override
	public void deleteUser(Integer userId) {

		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		userRepository.delete(user);
	}

	@Override
	public UserDto saveUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		User savedUser = this.userRepository.save(user);
		return this.modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public void generateResetToken(String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

		String token = UUID.randomUUID().toString();

		user.setResetToken(token);
		user.setTokenExpiry(LocalDateTime.now().plusMinutes(15));

		userRepository.save(user);

		String resetLink = "http://localhost:5000/reset-password?token=" + token;

		// Use centralized email message
		emailService.sendEmail(user.getEmail(), "Password Reset Request",
				EmailMessages.getResetPasswordMessage(resetLink));
	}

	@Override
	public void resetPassword(String token, String newPassword) {

		User user = userRepository.findByResetToken(token).orElseThrow(() -> new RuntimeException("Invalid token"));

		if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Token expired");
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		user.setResetToken(null);
		user.setTokenExpiry(null);

		userRepository.save(user);
	}

	@Override
	public void changePassword(String email, String oldPassword, String newPassword) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new RuntimeException("Old password is incorrect");
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}


}