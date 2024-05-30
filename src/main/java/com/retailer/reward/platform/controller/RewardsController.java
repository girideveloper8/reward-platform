package com.retailer.reward.platform.controller;

import com.retailer.reward.platform.entity.Transaction;
import com.retailer.reward.platform.exception.ResourceNotFoundException;
import com.retailer.reward.platform.service.RewardCalculationService;
import com.retailer.reward.platform.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing rewards.
 * Provides endpoints to calculate and retrieve reward points for transactions and customers.
 */

@RestController
@RequestMapping("/api/v1/rewards")
@Tag(name = "Rewards Management System", description = "Operations pertaining to rewards in Rewards Management System")
@Slf4j
@AllArgsConstructor
@Validated
public class RewardsController {

    private final RewardCalculationService rewardService;

    private final TransactionService transactionService;

    /**
     * Calculates the reward points for a specific transaction.
     *
     * @param transactionId the ID of the transaction to calculate rewards for
     * @return the reward points for the specified transaction
     */
    @Operation(summary = "Calculate each transaction reward points", description = "get the each transaction reward points by transactionID ")
    @GetMapping("/transaction/{transactionId}/rewards")
    public ResponseEntity<Integer> getCustomerRewards(@PathVariable @NotNull Long transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        if (transaction == null) {
            throw new ResourceNotFoundException("Transaction not found with ID: " + transactionId);
        }
        int rewardsPerTransaction = rewardService.getCustomerTransactionRewards(transaction);
        return new ResponseEntity<>(rewardsPerTransaction, HttpStatus.OK);
    }

    /**
     * Calculates the monthly reward points for a customer.
     *
     * @param customerId the ID of the customer to calculate monthly rewards for
     * @return a map of months and their corresponding reward points for the specified customer
     */
    @Operation(summary = "Calculate reward points per month for a customer", description = "Get the customer monthly rewards points summary by customerId")
    @GetMapping("/customer/{customerId}/monthly-rewards")
    public ResponseEntity<Map<String, Integer>> getCustomerMonthlyRewards(@PathVariable @NotNull Long customerId) {
        log.info("Fetching reward points per month for customer with ID: {}", customerId);
        List<Transaction> transactions = transactionService.getTransactionsByCustomerId(customerId);
        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException("No transactions found for customer with ID: " + customerId);
        }
        Map<String, Integer> monthlyRewards = rewardService.getCustomerMonthlyRewards(transactions);
        return new ResponseEntity<>(monthlyRewards, HttpStatus.OK);
    }


    /**
     * Calculates the total reward points for a customer.
     *
     * @param customerId the ID of the customer to calculate total rewards for
     * @return the total reward points for the specified customer
     */
    @Operation(summary = "Calculate total reward points for a customer", description = "Get the customer total reward points by customerId")
    @GetMapping("/customer/{customerId}/total-rewards")
    public ResponseEntity<Integer> getCustomerTotalRewards(@PathVariable @NotNull Long customerId) {
        List<Transaction> transactions = transactionService.getTransactionsByCustomerId(customerId);
        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException("No transactions found for customer with ID: " + customerId);
        }
        int totalRewards = rewardService.getCustomerTotalRewards(transactions);
        return new ResponseEntity<>(totalRewards, HttpStatus.OK);
    }


}

