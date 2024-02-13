package com.sanket.wealthify.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sanket.wealthify.entity.TicketTagMapping;

@Repository
public interface TicketTagMappingRepository extends JpaRepository<TicketTagMapping, Integer> {

	Optional<TicketTagMapping> findByTagIdAndTicketId(Integer tagId, UUID ticketId);

	List<TicketTagMapping> findByTicketId(UUID ticketId);

}
