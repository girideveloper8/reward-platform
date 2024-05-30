package com.retailer.reward.platform.repository;

import com.retailer.reward.platform.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for accessing customer data from the database.
 * Provides CRUD operations for the Customer entity.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
}
