package com.utkarsh.customer;

import com.utkarsh.exception.CustomerAlreadyExistException;
import com.utkarsh.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }
    @Override
    public Customer getCustomerById(Integer id){
        return customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer Not Found"));
    }

    @Override
    public void insertCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())){
            throw new CustomerAlreadyExistException("Customer with this email id already exist");
        }
        customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Integer id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer Not Found"));
        customerRepository.delete(customer);
    }

    @Override
    public Customer updateCustomer(Integer id,Customer updateCustomer) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer Not Found"));
        if (updateCustomer.getName()!=null && !updateCustomer.getName().equals(customer.getName())){
            customer.setName(updateCustomer.getName());
        }
        if (updateCustomer.getAge()!=null && !updateCustomer.getAge().equals(customer.getAge())){
            customer.setAge(updateCustomer.getAge());
        }
        if (updateCustomer.getEmail()!=null && !updateCustomer.getEmail().equals(customer.getEmail())){
            if (customerRepository.existsByEmail(customer.getEmail())){
                throw new CustomerAlreadyExistException("Customer with this email id already exist");
            }
            customer.setEmail(updateCustomer.getEmail());
        }
        return customerRepository.save(customer);
    }
}
