package com.daniel.ecommerce.service;

import com.daniel.ecommerce.data.Product;
import com.daniel.ecommerce.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Long productId, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(productId);
        if (existingProduct.isPresent()) {
            Product productToUpdate = existingProduct.get();
            productToUpdate.setName(updatedProduct.getName());
            productToUpdate.setDescription(updatedProduct.getDescription());
            productToUpdate.setPrice(updatedProduct.getPrice());
            productToUpdate.setQuantityAvailable(updatedProduct.getQuantityAvailable());
            productRepository.save(productToUpdate);
        }
        return existingProduct;
    }

    public boolean deleteProduct(Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return true;
        }
        return false;
    }

    public Optional<Product> applyDiscount(Long productId, Double discountPercentage, Double applicableValue) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        return optionalProduct.map(product -> {
            double discountedPrice = applyPercentageDiscount(product.getPrice(), discountPercentage);
            product.setPrice(discountedPrice);
            productRepository.save(product);
            return product;
        });
    }

    public Optional<Product> applyTax(Long productId, Double taxRate, Double applicableValue) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        return optionalProduct.map(product -> {
            double taxedPrice = applyTaxRate(product.getPrice(), taxRate);
            product.setPrice(taxedPrice);
            productRepository.save(product);
            return product;
        });
    }

    private double applyPercentageDiscount(double price, double discountPercentage) {
        return price - (price * discountPercentage / 100);
    }

    private double applyTaxRate(double price, double taxRate) {
        return price + (price * taxRate / 100);
    }
}

