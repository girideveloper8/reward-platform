package com.retailer.reward.platform.controller;

import com.retailer.reward.platform.entity.Transaction;
import com.retailer.reward.platform.exception.ResourceNotFoundException;
import com.retailer.reward.platform.service.RewardCalculationService;
import com.retailer.reward.platform.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RewardsControllerIntegrationTest {

    @Mock
    private RewardCalculationService rewardService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private RewardsController rewardsController;

    @Test
    public void testGetCustomerRewards() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        when(transactionService.getTransactionById(transactionId)).thenReturn(transaction);
        when(rewardService.getCustomerTransactionRewards(any(Transaction.class))).thenReturn(100);

        // Act
        ResponseEntity<Integer> response = rewardsController.getCustomerRewards(transactionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(100, response.getBody());
    }

    @Test
    public void testGetCustomerMonthlyRewards() {
        Long customerId = 1L;
        List<Transaction> transactions = List.of(new Transaction(), new Transaction());
        Map<String, Integer> monthlyRewards = new HashMap<>();
        monthlyRewards.put("January", 100);
        when(transactionService.getTransactionsByCustomerId(customerId)).thenReturn(transactions);
        when(rewardService.getCustomerMonthlyRewards(anyList())).thenReturn(monthlyRewards);

        // Act
        ResponseEntity<Map<String, Integer>> response = rewardsController.getCustomerMonthlyRewards(customerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(monthlyRewards, response.getBody());
    }

    @Test
    public void testGetCustomerTotalRewards() {
        Long customerId = 1L;
        List<Transaction> transactions = List.of(new Transaction(), new Transaction());
        when(transactionService.getTransactionsByCustomerId(customerId)).thenReturn(transactions);
        when(rewardService.getCustomerTotalRewards(anyList())).thenReturn(200);

        ResponseEntity<Integer> response = rewardsController.getCustomerTotalRewards(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody());
    }

    @Test
    public void testGetCustomerRewards_transactionNotFound() {
        Long transactionId = 1L;
        when(transactionService.getTransactionById(transactionId)).thenReturn(null);

        try {
            rewardsController.getCustomerRewards(transactionId);
        } catch (ResourceNotFoundException e) {
            assertEquals("Transaction not found with ID: " + transactionId, e.getMessage());
        }
    }
}
