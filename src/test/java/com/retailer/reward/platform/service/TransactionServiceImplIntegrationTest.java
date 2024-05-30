package com.retailer.reward.platform.service;

import com.retailer.reward.platform.entity.Customer;
import com.retailer.reward.platform.entity.Transaction;
import com.retailer.reward.platform.exception.CustomerNotFoundException;
import com.retailer.reward.platform.exception.TransactionNotFoundException;
import com.retailer.reward.platform.repository.CustomerRepository;
import com.retailer.reward.platform.repository.TransactionRepository;
import com.retailer.reward.platform.serviceimpl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplIntegrationTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction transaction;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
        customer.setCustomerId(1L);
        transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setCustomer(customer);
    }

    @Test
    public void testCreateTransaction_Success() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        assertNotNull(createdTransaction);
        assertEquals(transaction, createdTransaction);
        verify(customerRepository, times(1)).findById(anyLong());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }


    @Test
    public void testUpdateTransaction_Success() {
        Long transactionId = 1L;
        Transaction existingTransaction = new Transaction();
        Transaction transactionDetails = new Transaction();
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.save(transactionDetails)).thenReturn(transactionDetails);

        Transaction updatedTransaction = transactionService.updateTransaction(transactionId, transactionDetails);

        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).save(transactionDetails);
    }

    @Test
    public void testUpdateTransaction_TransactionNotFound() {
        Long transactionId = 1L;
        Transaction transactionDetails = new Transaction();
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.updateTransaction(transactionId, transactionDetails);
        });
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(0)).save(transactionDetails);
    }

    /*@Test
    public void testDeleteTransaction_Success() {
        doNothing().when(transactionRepository).deleteById(anyLong());
        transactionService.deleteTransaction(1L);
        verify(transactionRepository, times(1)).deleteById(anyLong());
    }*/

    @Test
    public void testDeleteTransaction_Success() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction();
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        doNothing().when(transactionRepository).deleteById(transactionId);

        transactionService.deleteTransaction(transactionId);
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).deleteById(transactionId);
    }

    @Test
    public void testDeleteTransaction_TransactionNotFound() {
        Long transactionId = 1L;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.deleteTransaction(transactionId);
        });

        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(0)).deleteById(transactionId);
    }

    @Test
    public void testGetTransactionById_Success() {
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));

        Transaction fetchedTransaction = transactionService.getTransactionById(1L);
        assertEquals(transaction, fetchedTransaction);
        verify(transactionRepository, times(1)).findById(anyLong());
    }
    @Test
    public void testGetTransactionById_TransactionNotFound() {
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.getTransactionById(1L);
        });
        verify(transactionRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetAllTransactions_Success() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> fetchedTransactions = transactionService.getAllTransactions();
        assertEquals(transactions, fetchedTransactions);
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    public void testGetTransactionsByCustomerId_Success() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        when(transactionRepository.findAllByCustomerCustomerId(anyLong())).thenReturn(transactions);

        List<Transaction> fetchedTransactions = transactionService.getTransactionsByCustomerId(1L);

        assertEquals(transactions, fetchedTransactions);
        verify(transactionRepository, times(1)).findAllByCustomerCustomerId(anyLong());
    }

    @Test
    public void testGetTransactionsByCustomerId_NoTransactionsFound() {
        when(transactionRepository.findAllByCustomerCustomerId(anyLong())).thenReturn(new ArrayList<>());
        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.getTransactionsByCustomerId(1L);
        });
    }

}
