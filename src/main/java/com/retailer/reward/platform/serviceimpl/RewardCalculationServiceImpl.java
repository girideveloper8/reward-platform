package com.retailer.reward.platform.serviceimpl;

import com.retailer.reward.platform.entity.Transaction;
import com.retailer.reward.platform.exception.InvalidTransactionAmountException;
import com.retailer.reward.platform.exception.InvalidTransactionException;
import com.retailer.reward.platform.exception.RewardNotFoundException;
import com.retailer.reward.platform.exception.TransactionNotFoundException;
import com.retailer.reward.platform.repository.TransactionRepository;
import com.retailer.reward.platform.service.RewardCalculationService;
import com.retailer.reward.platform.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service implementation for calculating rewards based on transactions.
 * Provides methods to calculate rewards for individual transactions,
 * monthly rewards, and total rewards for a customer.
 */
@Service
@Slf4j
@AllArgsConstructor
public class RewardCalculationServiceImpl implements RewardCalculationService {

    private final TransactionRepository transactionRepository;

    /**
     * Calculates reward points for a given transaction.
     *
     * @param transaction the transaction for which to calculate rewards
     * @return the calculated reward points
     * @throws InvalidTransactionAmountException if the transaction amount is zero or negative
     */
    @Override
    public int getCustomerTransactionRewards(Transaction transaction) {
        int amount = (int) transaction.getAmount();
        int points = 0;

        // If amount is zero or negative, return 0 points
        if (amount <= 0) {
            throw new InvalidTransactionAmountException(Constants.INVALID_AMOUNT);
        }

        // Reward 2 points for each dollar spent over $100
        if (amount > 100) {
            points += (amount - 100) * 2;
        }

        // Reward 1 point for each dollar spent over $50, up to $50
        if (amount > 50) {
            points += Math.min(50, amount - 50);
        }
        return points;
    }

    /**
     * Calculates the monthly reward points for a list of transactions.
     *
     * @param transactions the list of transactions
     * @return a map where the keys are month names and the values are the total reward points for each month
     * @throws TransactionNotFoundException if the list of transactions is null or empty
     */
    @Override
    public Map<String, Integer> getCustomerMonthlyRewards(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            throw new TransactionNotFoundException(Constants.TRANSACTIONS_NOT_FOUND);
        }
        Map<String, Integer> monthlyRewards = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getTransactionMonth,
                        Collectors.summingInt(this::getCustomerTransactionRewards)
                ));
        return monthlyRewards;
    }

    /**
     * Calculates the total reward points for a list of transactions.
     *
     * @param transactions the list of transactions
     * @return the total reward points
     * @throws TransactionNotFoundException if the list of transactions is null or empty
     * @throws RewardNotFoundException      if the calculated total rewards are zero or negative
     */
    @Override
    public int getCustomerTotalRewards(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            throw new TransactionNotFoundException(Constants.TRANSACTIONS_NOT_FOUND);
        }
        int totalRewards = transactions.stream()
                .mapToInt(this::getCustomerTransactionRewards)
                .sum();
        if (totalRewards <= 0) {
            throw new RewardNotFoundException(Constants.REWARDS_NOT_FOUND);
        }
        return totalRewards;
    }

}