package com.retailer.reward.platform.controller;

import com.retailer.reward.platform.entity.Customer;
import com.retailer.reward.platform.entity.Transaction;
import com.retailer.reward.platform.exception.CustomerNotFoundException;
import com.retailer.reward.platform.service.RewardCalculationService;
import com.retailer.reward.platform.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * REST controller for managing transactions.
 * Provides endpoints to create, update, delete, and retrieve transactions.
 */
@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transaction Management System", description = "Operations pertaining to transactions in Transaction Management System")
@Slf4j
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Creates a new transaction.
     *
     * @param transaction the transaction to be created
     * @return the created transaction
     */
    @Operation(summary = "Create a transaction")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {
        log.info("Creating transaction record for: {}", transaction);
        Customer customer = transaction.getCustomer();
        if (customer == null) {
            throw new CustomerNotFoundException("Transaction must have a customer associated with it");
        }
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    /**
     * Updates an existing transaction.
     *
     * @param transactionId the ID of the transaction to be updated
     * @param transaction the updated transaction details
     * @return the updated transaction or a 404 status if not found
     */
    @Operation(summary = "Update an existing transaction")
    @PutMapping("/{transactionId}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long transactionId, @RequestBody Transaction transaction) {
        log.info("Updating transaction with ID {}: {}", transaction);
        Transaction updatedTransaction = transactionService.updateTransaction(transactionId, transaction);
        if (updatedTransaction != null) {
            return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes a transaction.
     *
     * @param transactionId the ID of the transaction to be deleted
     * @return a 204 status indicating the transaction was successfully deleted
     */
    @Operation(summary = "Delete a transaction")
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId) {
        log.info("Deleting transaction with transactionId {}", transactionId);
        transactionService.deleteTransaction(transactionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves a transaction by ID.
     *
     * @param transactionId the ID of the transaction to retrieve
     * @return the retrieved transaction or a 404 status if not found
     */
    @Operation(summary = "Get a transaction by ID")
    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long transactionId) {
        log.info("Fetching transaction with transactionId {}", transactionId);
        Transaction transaction = transactionService.getTransactionById(transactionId);
        if (transaction != null) {
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieves all transactions.
     *
     * @return a list of all transactions
     */
    @Operation(summary = "Get all transactions")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        log.info("Fetching all transactions details");
        List<Transaction> transactions = transactionService.getAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
