package com.daniel.ecommerce.controller;

import com.daniel.ecommerce.data.Product;
import com.daniel.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/products/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {
        Optional<Product> product = productService.getProductById(productId);
        if(product.isPresent()){
            System.out.println("Product available: "+product.toString());
        } else {
            System.out.println("Product not found with ID: " + productId);
        }
        return product.isPresent() ? ResponseEntity.ok("Product available: "+product.get().toString()) :
        ResponseEntity.badRequest().body("Product not found with ID: " + productId);
    }


    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        boolean result = createdProduct != null;
        if(result){
            System.out.println("Product created: "+createdProduct.toString());
        } else {
            System.out.println("Product creation failed");
        }
        return  createdProduct!=null ? ResponseEntity.ok("Product created: "+product.toString()) :
                ResponseEntity.badRequest().body("Product creation failed");
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        Optional<Product> existingProduct = productService.updateProduct(productId, updatedProduct);
        if(existingProduct.isPresent()){
            System.out.println("Product updated:"+existingProduct.toString());
        } else {
            System.out.println("Product update failed");
        }
        return existingProduct.isPresent() ? ResponseEntity.ok("Product updated: "+existingProduct.get().toString()) :
                ResponseEntity.badRequest().body("Product update failed");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        boolean isDeleted = productService.deleteProduct(productId);
        if(isDeleted){
            System.out.println("Product deleted successfully");
        } else {
            System.out.println("Cannot delete product - product not found with ID: " + productId);
        }
        return isDeleted ? ResponseEntity.ok("Product "+productId+" deleted successfully") :
                ResponseEntity.badRequest().body("Product not found with ID: " + productId);
    }

    @PutMapping("/{productId}/apply-discount")
    public ResponseEntity<?> applyDiscount(@PathVariable Long productId,
                                           @RequestParam Double discountPercentage,
                                           @RequestParam Double applicableValue) {
        try {
            Optional<Product> updatedProduct = productService.applyDiscount(productId, discountPercentage, applicableValue);
            if(updatedProduct.isPresent()){
                System.out.println("Product price modified by applying discount:"+updatedProduct.get().getPrice());
            } else {
                System.out.println("Product price modification failed");
            }
            return updatedProduct.map(product -> ResponseEntity.ok("Discount applied successfully. " + product))
                    .orElse(ResponseEntity.badRequest().body("Product not found with ID: " + productId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error applying discount: " + e.getMessage());
        }
    }

    @PutMapping("/{productId}/apply-tax")
    public ResponseEntity<?> applyTax(@PathVariable Long productId,
                                      @RequestParam Double taxRate,
                                      @RequestParam Double applicableValue) {
        try {
            Optional<Product> updatedProduct = productService.applyTax(productId, taxRate, applicableValue);
            if(updatedProduct.isPresent()){
                System.out.println("Product price modified by applying tax:"+updatedProduct.get().getPrice());
            } else {
                System.out.println("Product price modification failed");
            }
            return updatedProduct.map(product -> ResponseEntity.ok("Tax applied successfully. " + product))
                    .orElse(ResponseEntity.badRequest().body("Product not found with ID: " + productId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error applying tax: " + e.getMessage());
        }
    }
}
