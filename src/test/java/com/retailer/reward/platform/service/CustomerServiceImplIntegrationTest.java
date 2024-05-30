package com.retailer.reward.platform.service;

import com.retailer.reward.platform.entity.Customer;
import com.retailer.reward.platform.exception.ResourceNotFoundException;
import com.retailer.reward.platform.repository.CustomerRepository;
import com.retailer.reward.platform.serviceimpl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplIntegrationTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setCustomerId(1L);
    }

    @Test
    public void testCreateCustomer_Success() {
        when(customerRepository.save(any())).thenReturn(customer);
        Customer createdCustomer = customerService.createCustomer(customer);
        assertEquals(customer, createdCustomer);
        verify(customerRepository, times(1)).save(any());
    }

    @Test
    public void testGetCustomer_Success() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        Customer fetchedCustomer = customerService.getCustomer(1L);
        assertEquals(customer, fetchedCustomer);
        verify(customerRepository, times(1)).findById(anyLong());
    }

    @Test
    void getCustomer_NonExistingCustomerId_ResourceNotFoundException() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomer(customerId));
    }

    @Test
    public void testUpdateCustomer_Success() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setCustomerName("John Doe");
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenReturn(customer);

        Customer updatedCustomer = customerService.updateCustomer(1L, customer);
        assertEquals(customer, updatedCustomer);
        verify(customerRepository, times(1)).findById(anyLong());
        verify(customerRepository, times(1)).save(any());
    }


    @Test
    void updateCustomer_NonExistingCustomer_ResourceNotFoundException() {
        Long customerId = 1L;
        Customer customer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.updateCustomer(customerId, customer));
        verify(customerRepository, never()).save(customer);
    }

    @Test
    public void testDeleteCustomer_Success() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(any());

        customerService.deleteCustomer(1L);
        verify(customerRepository, times(1)).findById(anyLong());
        verify(customerRepository, times(1)).delete(any());

    }

    @Test
    void deleteCustomer_NonExistingCustomerId_ResourceNotFoundException() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCustomer(customerId));
        verify(customerRepository, never()).delete(any(Customer.class));
    }

    @Test
    public void testGetAllCustomers_Success() {
        List<Customer> customers ;
        Customer customer1 = new Customer();
        customer1.setCustomerId(1L);
        customer1.setCustomerName("John Doe");

        Customer customer2 = new Customer();
        customer2.setCustomerId(2L);
        customer2.setCustomerName("Jane Doe");
        customers = Arrays.asList(customer1, customer2);
        when(customerRepository.findAll()).thenReturn(customers);
        List<Customer> fetchedCustomers = customerService.getAllCustomers();
        assertNotNull(customers);
        assertEquals(2, fetchedCustomers.size());
        assertEquals(customers, fetchedCustomers);
    }


    @Test
    public void testGetCustomer_CustomerNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.getCustomer(1L);
        });
    }


    @Test
    public void testUpdateCustomer_CustomerNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.updateCustomer(1L, customer);
        });
    }
}
