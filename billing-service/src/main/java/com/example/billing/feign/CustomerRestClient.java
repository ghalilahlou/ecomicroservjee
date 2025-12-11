package com.example.billing.feign;

import com.example.billing.models.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerRestClient {
    
    @GetMapping("/customers/{id}")
    Customer getCustomerById(@PathVariable Long id);
    
    @GetMapping("/customers")
    PagedModel<Customer> getAllCustomers();
}

