package com.example.billing.web;

import com.example.billing.entities.Bill;
import com.example.billing.feign.CustomerRestClient;
import com.example.billing.feign.ProductRestClient;
import com.example.billing.repositories.BillRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillRestController {
    
    private final BillRepository billRepository;
    private final CustomerRestClient customerRestClient;
    private final ProductRestClient productRestClient;
    
    public BillRestController(BillRepository billRepository,
                             CustomerRestClient customerRestClient,
                             ProductRestClient productRestClient) {
        this.billRepository = billRepository;
        this.customerRestClient = customerRestClient;
        this.productRestClient = productRestClient;
    }
    
    @GetMapping("/fullBill/{id}")
    public Bill getBill(@PathVariable Long id) {
        Bill bill = billRepository.findById(id).orElse(null);
        if (bill != null) {
            // Fetch customer details
            bill.setCustomer(customerRestClient.getCustomerById(bill.getCustomerId()));
            
            // Fetch product details for each product item
            bill.getProductItems().forEach(productItem -> {
                productItem.setProduct(productRestClient.getProductById(productItem.getProductId()));
            });
        }
        return bill;
    }
}

