package com.sanket.wealthify.service;

import org.springframework.stereotype.Service;

import com.sanket.wealthify.repository.NoteTagMappingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NoteTagMappingService {

	private final NoteTagMappingRepository noteTagMappingRepository;

}
