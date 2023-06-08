package com.utkarsh.customer;

import com.utkarsh.exception.CustomerAlreadyExistException;
import com.utkarsh.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    private CustomerService underTestService;

    @Mock private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        underTestService = new CustomerServiceImpl(customerRepository);
    }

    @Test
    void getAllCustomers() {
        //When
        underTestService.getAllCustomers();

        //Then
        verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById() {
        //Given
        int id = 10;
        Customer customer = new Customer(id,"test_b0143a82691b","test_db515b4ed19b@test.com",83);
        when(customerRepository.findById(10)).thenReturn(Optional.of(customer));

        //When
        Customer actual = underTestService.getCustomerById(id);

        //Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void getCustomerById_Will_Throw_Exception() {
        //Given
        int id = 10;
        when(customerRepository.findById(10)).thenReturn(Optional.empty());

        //When

        //Then
        assertThatThrownBy(()-> underTestService.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void insertCustomer() {
        //Given
        String email = "test_db515b4ed19b@test.com";
        Customer customer = new Customer("test_b0143a82691b",email,83);

        when(customerRepository.existsByEmail(email)).thenReturn(false);

        //When
        underTestService.insertCustomer(customer);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void insertCustomer_Will_Throw_Exception() {
        //Given
        String email = "test_db515b4ed19b@test.com";
        Customer customer = new Customer("test_b0143a82691b",email,83);
        when(customerRepository.existsByEmail(email)).thenReturn(true);

        assertThatThrownBy(()-> underTestService.insertCustomer(customer))
                .isInstanceOf(CustomerAlreadyExistException.class);

        verify(customerRepository,never()).save(any());
    }

    @Test
    void deleteCustomer() {
        //Given
        int id = 10;
        Customer customer = new Customer(id,"test_b0143a82691b","test_db515b4ed19b@test.com",83);
        when(customerRepository.findById(10)).thenReturn(Optional.of(customer));

        //When
        underTestService.deleteCustomer(id);

        //Then
        verify(customerRepository).delete(customer);
    }

    @Test
    void deleteCustomer_will_throw_exception() {
        //Given
        int id = 10;
        when(customerRepository.findById(10)).thenReturn(Optional.empty());

        //When
        assertThatThrownBy(()-> underTestService.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class);
        //Then
        verify(customerRepository,never()).delete(any());
    }

//Todo
    @Test
    void updateCustomer() {
        //Given

        //When

        //Then

    }
}