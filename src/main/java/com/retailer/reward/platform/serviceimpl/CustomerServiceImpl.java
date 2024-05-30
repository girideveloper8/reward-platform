package com.retailer.reward.platform.serviceimpl;

import com.retailer.reward.platform.entity.Customer;
import com.retailer.reward.platform.exception.ResourceNotFoundException;
import com.retailer.reward.platform.repository.CustomerRepository;
import com.retailer.reward.platform.service.CustomerService;
import com.retailer.reward.platform.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing customers.
 * Provides methods to create, retrieve, update, and delete customers.
 */
@Service
@Slf4j
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * Creates a new customer.
     *
     * @param customer the customer entity to be created
     * @return the created customer
     */
    @Override
    public Customer createCustomer(Customer customer) {
        Customer createdCustomer = customerRepository.save(customer);
        log.info("CustomerServiceImpl.createCustomer() :: Created Customer: {}", createdCustomer);
        return createdCustomer;
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param customerId the ID of the customer to retrieve
     * @return the customer entity
     * @throws ResourceNotFoundException if no customer is found with the given ID
     */
    @Override
    public Customer getCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                new ResourceNotFoundException(Constants.CUSTOMER_ID_NOT_FOUND + customerId));
        log.info("CustomerServiceImpl.getCustomer():: Fetch Customer by ID: {}", customer);
        return customer;
    }


    /**
     * Updates the details of an existing customer.
     *
     * @param customerId      the ID of the customer to update
     * @param customerDetails the updated customer entity
     * @return the updated customer
     * @throws ResourceNotFoundException if no customer is found with the given ID
     */
    @Override
    public Customer updateCustomer(Long customerId, Customer customerDetails) {
        customerRepository.findById(customerId).orElseThrow(() ->
                new ResourceNotFoundException(Constants.CUSTOMER_ID_NOT_FOUND + customerId));
        customerDetails.setCustomerId(customerId);
        Customer updatedCustomer = customerRepository.save(customerDetails);
        log.info("CustomerServiceImpl.updateCustomer():: Updated Customer details: {}", updatedCustomer);
        return updatedCustomer;
    }


    /**
     * Deletes a customer by their ID.
     *
     * @param customerId the ID of the customer to delete
     * @throws ResourceNotFoundException if no customer is found with the given ID
     */
    @Override
    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.CUSTOMER_ID_NOT_FOUND + customerId));
        customerRepository.delete(customer);
        log.info("CustomerServiceImpl.deleteCustomer():: deleted Customer by Id: {}", customerId);
    }

    /**
     * Retrieves all customers.
     *
     * @return a list of all customers
     */
    @Override
    public List<Customer> getAllCustomers() {
        log.info("CustomerServiceImpl.getAllCustomers():: Fetch all Customer details");
        List<Customer> customers = customerRepository.findAll();
        return customers;
    }
}
