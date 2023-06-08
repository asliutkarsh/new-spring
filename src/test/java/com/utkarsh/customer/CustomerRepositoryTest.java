package com.utkarsh.customer;

import com.utkarsh.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainer {

    @Autowired
    private CustomerRepository underTestRepo;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTestRepo.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }


    @Test
    void existsByEmail() {
        //Given
        String email = "test_db515b4ed19b@test.com";
        Customer customer = new Customer("test_b0143a82691b",email,83);
        underTestRepo.save(customer);

        //When
        var actual = underTestRepo.existsByEmail(email);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsByEmailFailWhenEmailNotPresent() {
        //Given
        String email = "test_db515b4ed19b@test.com";

        //When
        var actual = underTestRepo.existsByEmail(email);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void findAll() {
        //Given
        Customer customer1 = new Customer("test_b0143a82691b","test_db515b4ed19b@test.com",83);
        Customer customer2 = new Customer("test_b0143a826","test_db515b4ed@test.com",83);
        Customer customer3 = new Customer("test_b0143a8269","test_db515b4edb@test.com",83);
        underTestRepo.save(customer1);
        underTestRepo.save(customer2);
        underTestRepo.save(customer3);

        //When
        var actual = underTestRepo.findAll();

        //Then
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(3);

    }

    @Test
    void findById() {
        //Given
        int id =47;
        Customer customer = new Customer(id,"test_b0143a82691b","test_db515b4ed19b@test.com",83);
        underTestRepo.save(customer);

        //When
        var actual = underTestRepo.findById(id);

        //Then
        assertThat(actual).isNotNull();

    }

    @Test
    void findByIdFailWhenCustomerWithIdNotPresent() {
        //Given
        int id =47;

        //When
        var actual = underTestRepo.findById(id);

        //Then
        assertThat(actual).isEmpty();

    }

    @Test
    void givenCustomerObject_whenSave_thenReturnSavedCustomer() {
        //Given
        Customer customer = new Customer("test_b0143a82691b","test_db515b4ed19b@test.com",83);

        //When
        var actual = underTestRepo.save(customer);

        //Then
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isGreaterThan(0);

    }

    @Test
    void delete() {
        //Given
        Customer customer = new Customer("test_b0143a82691b","test_db515b4ed19b@test.com",83);
        underTestRepo.save(customer);

        //When
        underTestRepo.delete(customer);

        //Then
        Optional<Customer> findCustomer = underTestRepo.findById(customer.getId());
        assertThat(findCustomer).isEmpty();

    }
}