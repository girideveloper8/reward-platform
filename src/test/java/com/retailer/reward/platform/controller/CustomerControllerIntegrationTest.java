package com.retailer.reward.platform.controller;

import com.retailer.reward.platform.entity.Customer;
import com.retailer.reward.platform.service.CustomerService;
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
public class CustomerControllerIntegrationTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @Test
    public void testCreateCustomer() {
        Customer customer = new Customer();
        customer.setCustomerName("Test Customer");

        when(customerService.createCustomer(any(Customer.class))).thenReturn(customer);
         ResponseEntity<Customer> response = customerController.createCustomer(customer);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(customer, response.getBody());
        verify(customerService, times(1)).createCustomer(any(Customer.class));
    }

    @Test
    public void testGetCustomer() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setCustomerName("Test Customer");

        when(customerService.getCustomer(customerId)).thenReturn(customer);

        ResponseEntity<Customer> response = customerController.getCustomer(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer, response.getBody());
        verify(customerService, times(1)).getCustomer(customerId);
    }

    @Test
    public void testUpdateCustomer() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setCustomerName("Test Customer");

        when(customerService.updateCustomer(eq(customerId), any(Customer.class))).thenReturn(customer);

        ResponseEntity<Customer> response = customerController.updateCustomer(customerId, customer);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer, response.getBody());
        verify(customerService, times(1)).updateCustomer(eq(customerId), any(Customer.class));
    }

    @Test
    public void testDeleteCustomer() {
        Long customerId = 1L;

        ResponseEntity<Void> response = customerController.deleteCustomer(customerId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(customerService, times(1)).deleteCustomer(eq(customerId));
    }

    @Test
    public void testGetAllCustomers() {

        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());
        customers.add(new Customer());

        when(customerService.getAllCustomers()).thenReturn(customers);

        ResponseEntity<List<Customer>> response = customerController.getAllCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customers, response.getBody());
        verify(customerService, times(1)).getAllCustomers();
    }
}
