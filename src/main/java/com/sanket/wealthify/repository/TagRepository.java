package com.sanket.wealthify.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sanket.wealthify.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

	Optional<Tag> findByName(String name);

}
