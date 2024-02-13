package com.sanket.wealthify.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sanket.wealthify.entity.Goal;

@Repository
public interface GoalRepository extends JpaRepository<Goal, UUID> {

}
