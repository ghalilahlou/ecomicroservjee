package com.example.inventory;

import com.example.inventory.entities.Product;
import com.example.inventory.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(ProductRepository productRepository) {
        return args -> {
            Random random = new Random();
            
            productRepository.save(Product.builder()
                    .name("Laptop Dell XPS 15")
                    .price(1299.99)
                    .quantity(random.nextInt(100))
                    .build());
            
            productRepository.save(Product.builder()
                    .name("iPhone 15 Pro")
                    .price(999.99)
                    .quantity(random.nextInt(100))
                    .build());
            
            productRepository.save(Product.builder()
                    .name("Samsung Galaxy S24")
                    .price(899.99)
                    .quantity(random.nextInt(100))
                    .build());
            
            productRepository.save(Product.builder()
                    .name("MacBook Pro 14\"")
                    .price(1999.99)
                    .quantity(random.nextInt(100))
                    .build());
            
            productRepository.save(Product.builder()
                    .name("Sony WH-1000XM5 Headphones")
                    .price(399.99)
                    .quantity(random.nextInt(100))
                    .build());
            
            productRepository.findAll().forEach(product -> {
                System.out.println("Product: " + product.getName() + " - Price: $" + product.getPrice() + " - Quantity: " + product.getQuantity());
            });
        };
    }
}

