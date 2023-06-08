package com.utkarsh.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerServiceImpl;

    public CustomerController(CustomerService customerServiceImpl) {
        this.customerServiceImpl = customerServiceImpl;
    }

    @GetMapping
    public List<Customer> getCustomers(){
        return customerServiceImpl.getAllCustomers();
    }


    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable Integer id){
        return customerServiceImpl.getCustomerById(id);
    }

    @PostMapping
    public void insertCustomer(@RequestBody RequestedCustomer requestedCustomer){
        Customer customer = new Customer(requestedCustomer.name(), requestedCustomer.email(), requestedCustomer.age());
        customerServiceImpl.insertCustomer(customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Integer id){
        customerServiceImpl.deleteCustomer(id);
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Integer id,@RequestBody RequestedCustomer requestedCustomer){
        Customer customer = new Customer(requestedCustomer.name(), requestedCustomer.email(), requestedCustomer.age());
        return customerServiceImpl.updateCustomer(id,customer);
    }

    @GetMapping("/hello")
    public String getHello(){
        return "Hello";
    }

}
