package com.utkarsh.customer;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomers();
    Customer getCustomerById(Integer id);
    void insertCustomer(Customer customer);
    void deleteCustomer(Integer id);
    Customer updateCustomer(Integer id,Customer updateCustomer);
}
