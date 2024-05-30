package com.retailer.reward.platform.service;

import com.retailer.reward.platform.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customer);

    Customer getCustomer(Long customerId);

    Customer updateCustomer(Long customerId, Customer customerUpdateDetails);

    void deleteCustomer(Long customerId);

    List<Customer> getAllCustomers();
}