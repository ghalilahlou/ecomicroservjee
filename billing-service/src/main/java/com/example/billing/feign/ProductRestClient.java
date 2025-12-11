package com.example.billing.feign;

import com.example.billing.models.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "INVENTORY-SERVICE")
public interface ProductRestClient {
    
    @GetMapping("/products/{id}")
    Product getProductById(@PathVariable Long id);
    
    @GetMapping("/products")
    PagedModel<Product> getAllProducts();
}

