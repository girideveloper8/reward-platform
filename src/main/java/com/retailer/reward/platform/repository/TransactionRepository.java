package com.retailer.reward.platform.repository;

import com.retailer.reward.platform.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing transaction data from the database.
 * Provides CRUD operations for the Transaction entity.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByCustomerCustomerId(Long customerId);

}
