package com.retailer.reward.platform.controller;

import com.retailer.reward.platform.entity.Customer;
import com.retailer.reward.platform.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * This is the controller class for managing customer-related operations.
 * It handles CRUD operations for Customer entities.
 */
@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customer Reward-Platform System", description = "Operations pertaining to customer in Customer Reward-Platform System")
@Slf4j
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Creates a new customer.
     *
     * @param customer the customer to be created
     * @return ResponseEntity containing the created customer and HTTP status
     */
    @Operation(summary = "Add a new customer", description = "Create a new customer")
    @PostMapping()
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        log.info("Creating customer : {}", customer);
        Customer createdCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }


    /**
     * Retrieves a customer by their ID.
     *
     * @param customerId the ID of the customer to retrieve
     * @return ResponseEntity containing the customer and HTTP status
     */
    @Operation(summary = "Fetch customer details by customerId", description = "Get the customer details by customerId")
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long customerId) {
        log.info("Fetching customer with ID {}", customerId);
        Optional<Customer> customerOptional = Optional.ofNullable(customerService.getCustomer(customerId));
        return customerOptional
                .map(customer -> ResponseEntity.ok().body(customer))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing customer.
     *
     * @param customerId the ID of the customer to update
     * @param customer   the customer data to update
     * @return ResponseEntity containing the updated customer and HTTP status
     */
    @Operation(summary = "Update an existing customer", description = "Update customer details")
    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long customerId, @Valid @RequestBody Customer customer) {
        log.info("Updating customer with ID {}: {}", customerId, customer);
        Customer updatedCustomer = customerService.updateCustomer(customerId, customer);
        return updatedCustomer != null ?
                ResponseEntity.ok().body(updatedCustomer) :
                ResponseEntity.notFound().build();
    }

    /**
     * Deletes a customer by their ID.
     *
     * @param customerId the ID of the customer to delete
     * @return ResponseEntity with HTTP status
     */
    @Operation(summary = "Delete a customer", description = "Delete customer by ID")
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        log.info("Deleting customer with ID {}", customerId);
        customerService.deleteCustomer(customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves all customers.
     *
     * @return ResponseEntity containing the list of customers and HTTP status
     */
    @Operation(summary = "Fetch all customers data", description = "Get all customers details")
    @GetMapping()
    public ResponseEntity<List<Customer>> getAllCustomers() {
        log.info("Fetching all customers");
        List<Customer> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

}
