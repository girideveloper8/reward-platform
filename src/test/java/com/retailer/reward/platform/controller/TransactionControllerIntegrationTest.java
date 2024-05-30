package com.retailer.reward.platform.controller;

import com.retailer.reward.platform.entity.Customer;
import com.retailer.reward.platform.entity.Transaction;
import com.retailer.reward.platform.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerIntegrationTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    public void testCreateTransaction() {
        Transaction transaction = new Transaction();
        Customer customer = new Customer();
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);
        transaction.setCustomer(customer);

        ResponseEntity<Transaction> response = transactionController.createTransaction(transaction);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transaction, response.getBody());
        verify(transactionService, times(1)).createTransaction(any(Transaction.class));
    }

    @Test
    public void testUpdateTransaction() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction();

        when(transactionService.updateTransaction(eq(transactionId), any(Transaction.class))).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.updateTransaction(transactionId, transaction);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transaction, response.getBody());
        verify(transactionService, times(1)).updateTransaction(eq(transactionId), any(Transaction.class));
    }

    @Test
    public void testDeleteTransaction() {
        Long transactionId = 1L;

        ResponseEntity<Void> response = transactionController.deleteTransaction(transactionId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(transactionService, times(1)).deleteTransaction(eq(transactionId));
    }

    @Test
    public void testGetTransaction() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction();
        when(transactionService.getTransactionById(eq(transactionId))).thenReturn(transaction);
        ResponseEntity<Transaction> response = transactionController.getTransaction(transactionId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transaction, response.getBody());
        verify(transactionService, times(1)).getTransactionById(eq(transactionId));
    }

    @Test
    public void testGetAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        when(transactionService.getAllTransactions()).thenReturn(transactions);

        ResponseEntity<List<Transaction>> response = transactionController.getAllTransactions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
        verify(transactionService, times(1)).getAllTransactions();
    }
}
