package com.example.customer;

import com.example.customer.entities.Customer;
import com.example.customer.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerRepository customerRepository) {
        return args -> {
            customerRepository.save(Customer.builder().name("Alice Martin").email("alice@example.com").build());
            customerRepository.save(Customer.builder().name("Bob Dupont").email("bob@example.com").build());
            customerRepository.save(Customer.builder().name("Charlie Bernard").email("charlie@example.com").build());
            customerRepository.save(Customer.builder().name("Diana Dubois").email("diana@example.com").build());
            
            customerRepository.findAll().forEach(customer -> {
                System.out.println("Customer: " + customer.getName() + " - " + customer.getEmail());
            });
        };
    }
}

