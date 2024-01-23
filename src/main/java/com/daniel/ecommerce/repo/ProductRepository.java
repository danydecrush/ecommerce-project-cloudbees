package com.daniel.ecommerce.repo;

import com.daniel.ecommerce.data.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // You can add custom query methods if needed
}

