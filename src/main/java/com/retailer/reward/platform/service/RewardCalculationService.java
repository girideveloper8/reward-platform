package com.retailer.reward.platform.service;

import com.retailer.reward.platform.entity.Transaction;

import java.util.List;
import java.util.Map;

public interface RewardCalculationService {

    int getCustomerTransactionRewards(Transaction transaction);

    Map<String, Integer> getCustomerMonthlyRewards(List<Transaction> transactions);

    int getCustomerTotalRewards(List<Transaction> transactions);

}