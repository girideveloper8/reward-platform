package com.retailer.reward.platform.service;

import com.retailer.reward.platform.entity.Transaction;
import com.retailer.reward.platform.exception.InvalidTransactionAmountException;
import com.retailer.reward.platform.serviceimpl.RewardCalculationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RewardsCalculatorServiceTest {

    @Autowired
    RewardCalculationServiceImpl rewardsCalculatorService;

    @Test
    public void testGetCustomerTransactionRewards_ZeroAmount() {
        Transaction transaction = new Transaction();
        transaction.setAmount(0.0); // Zero amount
        assertThrows(InvalidTransactionAmountException.class, ()
                -> rewardsCalculatorService.getCustomerTransactionRewards(transaction));
    }

    @Test
    public void testGetCustomerTransactionRewards_NegativeAmount() {
        Transaction transaction = new Transaction();
        transaction.setAmount(-50.0); // Negative amount

        assertThrows(InvalidTransactionAmountException.class, () -> rewardsCalculatorService.getCustomerTransactionRewards(transaction));
    }

    @Test
    public void testGetCustomerTransactionRewards_LessThan50() {
        Transaction transaction = new Transaction();
        transaction.setAmount(40.0); // Amount less than $50

        assertEquals(0, rewardsCalculatorService.getCustomerTransactionRewards(transaction));
    }

    @Test
    public void testGetCustomerTransactionRewards_Between50And100() {
        Transaction transaction = new Transaction();
        transaction.setAmount(70.0); // Amount between $50 and $100

        assertEquals(20, rewardsCalculatorService.getCustomerTransactionRewards(transaction));
    }

    @Test
    public void testGetCustomerTransactionRewards_MoreThan100() {
        Transaction transaction = new Transaction();
        transaction.setAmount(150.0); // Amount more than $100

        assertEquals(150, rewardsCalculatorService.getCustomerTransactionRewards(transaction));
    }

    @Test
    public void testGetCustomerTransactionRewards_Test1() {
        Transaction transaction = new Transaction();
        transaction.setAmount(51.5); // Amount more than $100

        assertEquals(1, rewardsCalculatorService.getCustomerTransactionRewards(transaction));
    }

    @Test
    public void testGetCustomerTransactionRewards_Test2() {
        Transaction transaction = new Transaction();
        transaction.setAmount(109.5); // Amount more than $100

        assertEquals(68, rewardsCalculatorService.getCustomerTransactionRewards(transaction));
    }
}


