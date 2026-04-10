package com.tata.controller;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tata.entity.User;
import com.tata.exception.ApiException;
import com.tata.exception.ResourceNotFoundException;
import com.tata.payloads.JwtAuthRequest;
import com.tata.payloads.UserDto;
import com.tata.repo.UserRepository;
import com.tata.security.JwtTokenHelper;
import com.tata.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class HomeController {

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	
	@PostMapping("/login")
	public ResponseEntity<?> createToken(@RequestBody JwtAuthRequest request) throws Exception {

		this.authenticate(request.getUsername(), request.getPassword());

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());

		String token = this.jwtTokenHelper.generateToken(userDetails);

		// Fetch full user from DB
		User user = userRepository.findByEmail(request.getUsername())
				.orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getUsername()));

		UserDto userDto = modelMapper.map(user, UserDto.class);

		// Return token + user
		Map<String, Object> response = new HashMap<>();
		response.put("token", token);
		response.put("user", userDto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private void authenticate(String username, String password) throws Exception {

		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(username, password);

		try {
			this.authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			throw new ApiException("Invalid Username or password...");
		}
	}

	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {

		UserDto newUser = this.userService.registerUser(userDto);

		return new ResponseEntity<>(newUser, HttpStatus.CREATED);
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestParam String email) {

		userService.generateResetToken(email);
		return ResponseEntity.ok("Reset link sent to your email.");
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(
	        @RequestParam String token,
	        @RequestParam String newPassword) {

	    userService.resetPassword(token, newPassword);
	    return ResponseEntity.ok("Password reset successful.");
	}
	
	
}