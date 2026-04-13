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
}