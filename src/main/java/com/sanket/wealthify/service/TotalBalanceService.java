package com.sanket.wealthify.service;

import org.springframework.stereotype.Service;

import com.sanket.wealthify.repository.TotalBalanceRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TotalBalanceService {

	private final TotalBalanceRepository totalBalanceRepository;

}
