package com.retailer.reward.platform.service;

import com.retailer.reward.platform.entity.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction);

    Transaction updateTransaction(Long transactionId, Transaction transaction);

    void deleteTransaction(Long transactionId);

    Transaction getTransactionById(Long transactionId);

    List<Transaction> getAllTransactions();

    List<Transaction> getTransactionsByCustomerId(Long customerId);




}
