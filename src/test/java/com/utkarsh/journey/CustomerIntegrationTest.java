package com.utkarsh.journey;

import com.utkarsh.customer.Customer;
import com.utkarsh.customer.CustomerRepository;
import com.utkarsh.customer.RequestedCustomer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    private static final String CUSTOMER_PATH = "/api/v1/customers";
    @Autowired
    private CustomerRepository customerRepository;
    private final Random random = new Random();

    @Test
    void canRegisterCustomer() {
        //Create Registration Request
        String email = "random"+random.nextInt()+"@gmail.com";
        RequestedCustomer requestedCustomer = new RequestedCustomer("random",email,89);

        //Send a post request
        webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestedCustomer),RequestedCustomer.class)
                .exchange()
                .expectStatus()
                .isOk();

        //Get all customer
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        assert allCustomers != null;
        int id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        Customer expectedCustomer = new Customer("random",email,89);

        //make sure that customer is present
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        //get customer by id
        Customer customer = webTestClient.get()
                    .uri(CUSTOMER_PATH + "/{id}", id)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(new ParameterizedTypeReference<Customer>() {
                    })
                    .returnResult()
                    .getResponseBody();

        assert customer != null;
        assertThat(customer.getEmail()).isEqualTo(expectedCustomer.getEmail());

    }


//Todo
/*
    @Test
    void canDeleteCustomer() {
        //Create Registration Request
        String email = "random@gmail.com";
        RequestedCustomer requestedCustomer = new RequestedCustomer("random","random@gmail.com",89);

        //Send a post request
        webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestedCustomer),RequestedCustomer.class)
                .exchange()
                .expectStatus()
                .isOk();

        //Get all customer
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        assert allCustomers != null;
        int id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        Customer expectedCustomer = new Customer("random","random@gmail.com",89);

        //make sure that customer is present
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        //get customer by id
        Customer customer = webTestClient.get()
                .uri(CUSTOMER_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        assert customer != null;
        assertThat(customer.getEmail()).isEqualTo(expectedCustomer.getEmail());

    }
*/


}
