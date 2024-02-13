package com.sanket.wealthify.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sanket.wealthify.service.SpendingThresholdRecordService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class MonthlySpendingRecordCalculationScheduler {

	private final SpendingThresholdRecordService spendingThresholdRecordService;

	@Scheduled(cron = "0 0 12 1 * ?")
	public void monthlySpendingRecordCalculationJob() {
		spendingThresholdRecordService.calulate();
	}

}
