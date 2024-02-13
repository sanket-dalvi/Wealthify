package com.sanket.wealthify.service;

import org.springframework.stereotype.Service;

import com.sanket.wealthify.repository.TicketTagMappingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketTagMappingService {

	private final TicketTagMappingRepository ticketTagMappingRepository;

}
