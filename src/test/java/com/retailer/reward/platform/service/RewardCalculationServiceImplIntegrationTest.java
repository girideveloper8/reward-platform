package com.retailer.reward.platform.service;

import com.retailer.reward.platform.entity.Transaction;
import com.retailer.reward.platform.exception.InvalidTransactionAmountException;
import com.retailer.reward.platform.exception.TransactionNotFoundException;
import com.retailer.reward.platform.repository.TransactionRepository;
import com.retailer.reward.platform.serviceimpl.RewardCalculationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardCalculationServiceImplIntegrationTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardCalculationServiceImpl rewardCalculationService;

    @Test
    public void testGetCustomerTransactionRewards_ValidAmount() {
        Transaction transaction = new Transaction();
        transaction.setAmount(150);
        int rewards = rewardCalculationService.getCustomerTransactionRewards(transaction);
        assertEquals(150, rewards);
    }

    @Test
    public void testGetCustomerTransactionRewards_InvalidAmount() {
        Transaction transaction = new Transaction();
        transaction.setAmount(0);
        assertThrows(InvalidTransactionAmountException.class, () -> {
            rewardCalculationService.getCustomerTransactionRewards(transaction);
        });
    }

    @Test
    public void testGetCustomerMonthlyRewards_TransactionsNotFound() {
        assertThrows(TransactionNotFoundException.class, () -> {
            rewardCalculationService.getCustomerMonthlyRewards(null);
        });
    }

    @Test
    public void testGetCustomerTotalRewards_TransactionsNotFound() {
        assertThrows(TransactionNotFoundException.class, () -> {
            rewardCalculationService.getCustomerTotalRewards(null);
        });
    }

    }
