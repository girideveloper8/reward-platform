package com.retailer.reward.platform.serviceimpl;

import com.retailer.reward.platform.entity.Customer;
import com.retailer.reward.platform.entity.Transaction;
import com.retailer.reward.platform.exception.CustomerNotFoundException;
import com.retailer.reward.platform.exception.InvalidTransactionException;
import com.retailer.reward.platform.exception.ResourceNotFoundException;
import com.retailer.reward.platform.exception.TransactionNotFoundException;
import com.retailer.reward.platform.repository.CustomerRepository;
import com.retailer.reward.platform.repository.TransactionRepository;
import com.retailer.reward.platform.service.TransactionService;
import com.retailer.reward.platform.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing transactions.
 * Provides methods to create, update, delete, and retrieve transactions.
 */
@Service
@Slf4j
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    /**
     * Creates a new transaction.
     *
     * @param transaction the transaction to create
     * @return the created transaction
     * @throws CustomerNotFoundException if the associated customer is not found
     */
    @Override
    public Transaction createTransaction(Transaction transaction) {
        Customer customer = customerRepository.findById(transaction.getCustomer().getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(Constants.CUSTOMER_ID_NOT_FOUND + transaction.getCustomer().getCustomerId()));
        transaction.setCustomer(customer);
        Transaction transactionCreated =  transactionRepository.save(transaction);
        log.info("TransactionServiceImpl.createTransaction():: transaction {}", transaction);
        return  transactionCreated;
    }

    /**
     * Updates an existing transaction.
     *
     * @param transactionId the ID of the transaction to update
     * @param transactionDetails the new details of the transaction
     * @return the updated transaction, or null if the transaction does not exist
     */
    @Override
    public Transaction updateTransaction(Long transactionId, Transaction transactionDetails) {
        // Check if the transaction exists
        Transaction existingTransaction= transactionRepository.findById(transactionId).orElseThrow(() ->
                new TransactionNotFoundException(Constants.TRANSCTION_ID_NOT_FOUND + transactionId));
            transactionDetails.setTransactionId(transactionId);
        transactionDetails.setCustomer(existingTransaction.getCustomer());
        // Save the updated transaction
           Transaction updatedTransaction = transactionRepository.save(transactionDetails);
            log.info("TransactionServiceImpl.updateTransaction() updated transaction: {}", updatedTransaction);
        // Return the updated transaction
            return updatedTransaction;
        }

    /**
     * Deletes a transaction by its ID.
     *
     * @param transactionId the ID of the transaction to delete
     */
    @Override
    public void deleteTransaction(Long transactionId) {
        transactionRepository.findById(transactionId).orElseThrow(() ->
                new TransactionNotFoundException(Constants.TRANSCTION_ID_NOT_FOUND + transactionId));
        transactionRepository.deleteById(transactionId);
        log.info("TransactionServiceImpl.deleteTransaction() deleted transaction Id : {}", transactionId);
    }


    /**
     * Retrieves a transaction by its ID.
     *
     * @param transactionId the ID of the transaction to retrieve
     * @return the retrieved transaction
     * @throws TransactionNotFoundException if the transaction is not found
     */
    @Override
    public Transaction getTransactionById(Long transactionId) {
        Transaction transactionDeatil = transactionRepository.findById(transactionId).orElseThrow(() -> new TransactionNotFoundException(Constants.TRANSCTION_ID_NOT_FOUND + transactionId));
        log.info("TransactionServiceImpl.getTransactionById()::  Fetch transaction details by transactionId : {}", transactionDeatil);
        return transactionDeatil;
    }

    /**
     * Retrieves all transactions.
     *
     * @return a list of all transactions
     */
    @Override
    public List<Transaction> getAllTransactions() {
        log.info("TransactionServiceImpl.getAllTransactions() :: Fetch all transaction details ");
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions;
    }


    /**
     * Retrieves all transactions for a specific customer.
     *
     * @param customerId the ID of the customer
     * @return a list of transactions for the customer
     * @throws TransactionNotFoundException if no transactions are found for the customer
     */
    @Override
    public List<Transaction> getTransactionsByCustomerId(Long customerId) {
        List<Transaction> transactionList = transactionRepository.findAllByCustomerCustomerId(customerId);
        if (transactionList.isEmpty()) {
            throw new TransactionNotFoundException("No transactions found for customer ID: " + customerId);
        }
        return transactionList;
    }
}
