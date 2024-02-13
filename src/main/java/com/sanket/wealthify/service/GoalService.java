package com.sanket.wealthify.service;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sanket.wealthify.dto.request.GoalCreationRequestDto;
import com.sanket.wealthify.dto.request.GoalUpdationRequestDto;
import com.sanket.wealthify.dto.response.GoalDto;
import com.sanket.wealthify.entity.Goal;
import com.sanket.wealthify.repository.GoalRepository;
import com.sanket.wealthify.repository.UserRepository;
import com.sanket.wealthify.security.utility.JwtUtils;
import com.sanket.wealthify.utils.ResponseUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GoalService {

	private final GoalRepository goalRepository;

	private final UserRepository userRepository;

	private final JwtUtils jwtUtils;

	private final ResponseUtils responseUtils;

	public ResponseEntity<?> create(final GoalCreationRequestDto goalCreationRequest, final String token) {
		final var goal = new Goal();
		goal.setActive(true);
		goal.setDescription(goalCreationRequest.getDescription());
		goal.setTitle(goalCreationRequest.getTitle());
		goal.setUserId(jwtUtils.extractUserId(token.replace("Bearer ", "")));
		final var savedGoal = goalRepository.save(goal);
		return responseUtils.goalSuccessResponse(savedGoal.getId());
	}

	public ResponseEntity<?> update(final GoalUpdationRequestDto goalUpdationRequest, final String token) {
		final var goal = goalRepository.findById(goalUpdationRequest.getId()).get();
		goal.setActive(goalUpdationRequest.getIsActive());
		goal.setDescription(goalUpdationRequest.getDescription());
		final var savedGoal = goalRepository.save(goal);
		return responseUtils.goalSuccessResponse(savedGoal.getId());
	}

	public ResponseEntity<?> retreive(final String token) {
		final var user = userRepository.findById(jwtUtils.extractUserId(token.replace("Bearer ", ""))).get();
		return ResponseEntity.ok(user.getGoals().parallelStream()
				.map(goal -> GoalDto.builder().createdAt(goal.getCreatedAt()).description(goal.getDescription())
						.id(goal.getId()).isActive(goal.isActive()).title(goal.getTitle())
						.updatedAt(goal.getUpdatedAt()).build())
				.collect(Collectors.toList()));
	}

}
