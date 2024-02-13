package com.sanket.wealthify.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sanket.wealthify.dto.request.UserCreationRequestDto;
import com.sanket.wealthify.dto.request.UserDetailUpdationRequestDto;
import com.sanket.wealthify.dto.request.UserLoginRequestDto;
import com.sanket.wealthify.dto.request.UserPasswordUpdationRequestDto;
import com.sanket.wealthify.dto.response.UserDetailsDto;
import com.sanket.wealthify.entity.CurrentMonthlySpendingThresholdLimit;
import com.sanket.wealthify.entity.TotalBalance;
import com.sanket.wealthify.entity.User;
import com.sanket.wealthify.repository.CurrentMonthlySpendingThresholdLimitRepository;
import com.sanket.wealthify.repository.TotalBalanceRepository;
import com.sanket.wealthify.repository.UserRepository;
import com.sanket.wealthify.security.utility.JwtUtils;
import com.sanket.wealthify.utils.ResponseUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final TotalBalanceRepository totalBalanceRepository;

	private final PasswordEncoder passwordEncoder;

	private final ResponseUtils responseUtils;

	private final JwtUtils jwtUtils;

	private final CurrentMonthlySpendingThresholdLimitRepository currentMonthlySpendingThresholdLimitRepository;

	public boolean userExists(final String emailId) {
		return userRepository.findByEmailId(emailId).isPresent() ? true : false;
	}

	public ResponseEntity<?> retreive(String token) {
		final var userId = jwtUtils.extractUserId(token.replace("Bearer ", ""));
		final var user = userRepository.findById(userId).get();
		return ResponseEntity.ok(UserDetailsDto.builder().createdAt(user.getCreatedAt())
				.dateOfBirth(user.getDateOfBirth()).emailId(user.getEmailId()).firstName(user.getFirstName())
				.lastName(user.getLastName()).updatedAt(user.getUpdatedAt()).build());
	}

	public ResponseEntity<?> createUser(final UserCreationRequestDto userCreationRequest) {

		if (userExists(userCreationRequest.getEmailId()))
			return responseUtils.duplicateEmailIdResponse();

		final var user = new User();
		user.setEmailId(userCreationRequest.getEmailId());
		user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
		user.setFirstName(userCreationRequest.getFirstName());
		user.setLastName(userCreationRequest.getLastName());
		user.setDateOfBirth(userCreationRequest.getDateOfBirth());

		final var savedUser = userRepository.save(user);

		final var totalBalance = new TotalBalance();
		totalBalance.setUserId(savedUser.getId());
		totalBalanceRepository.save(totalBalance);

		final var currentMonthlySpendingThresholdLimit = new CurrentMonthlySpendingThresholdLimit();
		currentMonthlySpendingThresholdLimit.setUserId(savedUser.getId());
		currentMonthlySpendingThresholdLimit.setLimitValue(0.0);
		currentMonthlySpendingThresholdLimit.setIsActive(false);
		currentMonthlySpendingThresholdLimitRepository.save(currentMonthlySpendingThresholdLimit);

		return responseUtils.userCreationSuccessResponse();
	}

	public ResponseEntity<?> login(final UserLoginRequestDto userLoginRequestDto) {
		final var user = userRepository.findByEmailId(userLoginRequestDto.getEmailId());
		if (user.isEmpty())
			return responseUtils.invalidEmailIdResponse();
		if (passwordEncoder.matches(userLoginRequestDto.getPassword(), user.get().getPassword())) {
			final var retreivedUser = user.get();
			final var jwtToken = jwtUtils.generateToken(retreivedUser,
					totalBalanceRepository.findByUserId(retreivedUser.getId()).get().getId());
			return responseUtils.loginSuccessResponse(jwtToken);
		}
		return responseUtils.invalidPasswordResponse();
	}

	public ResponseEntity<?> updatePassword(final UserPasswordUpdationRequestDto userPasswordUpdationRequestDto,
			final String token) {
		final var loggedInUserId = jwtUtils.extractUserId(token.replace("Bearer ", ""));
		final var user = userRepository.findById(loggedInUserId).get();

		if (!passwordEncoder.matches(userPasswordUpdationRequestDto.getOldPassword(), user.getPassword()))
			return responseUtils.invalidPasswordResponse();

		user.setPassword(passwordEncoder.encode(userPasswordUpdationRequestDto.getNewPassword()));
		userRepository.save(user);

		return responseUtils.passwordUpdationSuccessResponse();
	}

	public ResponseEntity<?> deleteAccount(final String token) {
		final var userId = jwtUtils.extractUserId(token.replace("Bearer ", ""));
		final var totalBalance = totalBalanceRepository.findByUserId(userId).get();
		totalBalanceRepository.deleteById(totalBalance.getId());
		return ResponseEntity.ok().build();
	}

	public ResponseEntity<?> update(final UserDetailUpdationRequestDto userDetailUpdationRequest, final String token) {
		final var loggedInUserId = jwtUtils.extractUserId(token.replace("Bearer ", ""));
		final var user = userRepository.findById(loggedInUserId).get();

		user.setFirstName(userDetailUpdationRequest.getFirstName());
		user.setLastName(userDetailUpdationRequest.getLastName());
		user.setDateOfBirth(userDetailUpdationRequest.getDateOfBirth());
		userRepository.save(user);

		return responseUtils.profileDetailUpdationSuccessResponse();
	}

}
