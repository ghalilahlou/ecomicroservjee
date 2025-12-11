package com.example.billing;

import com.example.billing.entities.Bill;
import com.example.billing.entities.ProductItem;
import com.example.billing.feign.CustomerRestClient;
import com.example.billing.feign.ProductRestClient;
import com.example.billing.models.Customer;
import com.example.billing.models.Product;
import com.example.billing.repositories.BillRepository;
import com.example.billing.repositories.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BillRepository billRepository,
                            ProductItemRepository productItemRepository,
                            CustomerRestClient customerRestClient,
                            ProductRestClient productRestClient) {
        return args -> {
            try {
                // Fetch customers and products
                Collection<Customer> customers = customerRestClient.getAllCustomers().getContent();
                Collection<Product> products = productRestClient.getAllProducts().getContent();

                if (customers.isEmpty() || products.isEmpty()) {
                    System.out.println("Waiting for customers and products to be available...");
                    return;
                }

                Random random = new Random();
                
                // Convert collections to lists for easier manipulation
                var customerList = customers.stream().collect(Collectors.toList());
                var productList = products.stream().collect(Collectors.toList());

                // Create bills for each customer
                customerList.forEach(customer -> {
                    Bill bill = Bill.builder()
                            .billingDate(new Date())
                            .customerId(customer.getId())
                            .build();
                    billRepository.save(bill);

                    // Add random products to the bill
                    int numProducts = random.nextInt(3) + 1; // 1 to 3 products per bill
                    for (int i = 0; i < numProducts; i++) {
                        Product product = productList.get(random.nextInt(productList.size()));
                        ProductItem productItem = ProductItem.builder()
                                .productId(product.getId())
                                .price(product.getPrice())
                                .quantity(random.nextInt(5) + 1)
                                .bill(bill)
                                .build();
                        productItemRepository.save(productItem);
                    }
                });

                System.out.println("Bills created successfully!");
            } catch (Exception e) {
                System.err.println("Error creating bills: " + e.getMessage());
                System.err.println("Make sure customer-service and inventory-service are running.");
            }
        };
    }
}

