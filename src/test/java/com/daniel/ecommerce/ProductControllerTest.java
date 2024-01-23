package com.daniel.ecommerce;

import com.daniel.ecommerce.controller.ProductController;
import com.daniel.ecommerce.data.Product;
import com.daniel.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private List<Product> dummyProducts;

    @BeforeEach
    public void setUp() {
        dummyProducts = Arrays.asList(
                new Product(1L, "Product 1", "Description 1", 30.0, 50),
                new Product(2L, "Product 2", "Description 2", 40.0, 30),
                new Product(3L, "Product 3", "Description 3", 20.0, 70),
                new Product(4L, "Product 4", "Description 4", 50.0, 10),
                new Product(5L, "Product 5", "Description 5", 25.0, 60)
        );
    }

    @Test
    public void testCreateProduct_Success() {
        Product productToCreate = dummyProducts.get(0);
        when(productService.createProduct(productToCreate)).thenReturn(productToCreate);
        ResponseEntity<?> responseEntity = productController.createProduct(productToCreate);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Product created: " + productToCreate.toString(), responseEntity.getBody());
        verify(productService, times(1)).createProduct(productToCreate);
    }

    @Test
    public void testCreateProduct_Failure() {
        Product productToCreate = dummyProducts.get(0);
        when(productService.createProduct(productToCreate)).thenReturn(null);
        ResponseEntity<?> responseEntity = productController.createProduct(productToCreate);
        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals("Product creation failed", responseEntity.getBody());
        verify(productService, times(1)).createProduct(productToCreate);
    }

    @Test
    public void testGetProduct_Success() {
        Long productId = 1L;
        Product mockProduct = dummyProducts.get(0);
        when(productService.getProductById(productId)).thenReturn(Optional.of(mockProduct));
        ResponseEntity<?> responseEntity = productController.getProduct(productId);
        assertEquals(200, responseEntity.getStatusCodeValue());
        String expected = "Product available: Product(productId=1, name=Product 1, description=Description 1, price=30.0, quantityAvailable=50)";
        assertEquals(expected, responseEntity.getBody());
        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    public void testGetProduct_Failure() {
        Long productId = 2L;
        when(productService.getProductById(productId)).thenReturn(Optional.empty());
        ResponseEntity<?> responseEntity = productController.getProduct(productId);
        assertEquals(400, responseEntity.getStatusCodeValue());
        String expected = "Product not found with ID: 2";
        assertEquals(expected, responseEntity.getBody());
        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    public void testUpdateProduct_Success() {
        Long productId = 1L;
        Product updatedProduct = dummyProducts.get(3);
        when(productService.updateProduct(productId, updatedProduct)).thenReturn(Optional.of(updatedProduct));
        ResponseEntity<?> responseEntity = productController.updateProduct(productId, updatedProduct);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Product updated: " + updatedProduct.toString(), responseEntity.getBody());
        verify(productService, times(1)).updateProduct(productId, updatedProduct);
    }

    @Test
    public void testUpdateProduct_Failure() {
        Long productId = 2L;
        Product updatedProduct = dummyProducts.get(3);
        when(productService.updateProduct(productId, updatedProduct)).thenReturn(Optional.empty());
        ResponseEntity<?> responseEntity = productController.updateProduct(productId, updatedProduct);
        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals("Product update failed", responseEntity.getBody());
        verify(productService, times(1)).updateProduct(productId, updatedProduct);
    }

    @Test
    public void testDeleteProduct_Success() {
        Long productId = 1L;
        when(productService.deleteProduct(productId)).thenReturn(true);
        ResponseEntity<?> responseEntity = productController.deleteProduct(productId);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Product " + productId + " deleted successfully", responseEntity.getBody());
        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    public void testDeleteProduct_Failure() {
        Long productId = 2L;
        when(productService.deleteProduct(productId)).thenReturn(false);
        ResponseEntity<?> responseEntity = productController.deleteProduct(productId);
        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals("Product not found with ID: " + productId, responseEntity.getBody());
        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    public void testApplyDiscount_Success() {
        Long productId = 1L;
        Double discountPercentage = 10.0;
        Double applicableValue = 50.0;
        Product product = dummyProducts.get(2);
        when(productService.applyDiscount(productId, discountPercentage, applicableValue)).thenReturn(Optional.of(product));

        ResponseEntity<?> responseEntity = productController.applyDiscount(productId, discountPercentage, applicableValue);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Discount applied successfully. " + product, responseEntity.getBody());
        verify(productService, times(1)).applyDiscount(productId, discountPercentage, applicableValue);
    }

    @Test
    public void testApplyDiscount_Failure() {
        Long productId = 2L;
        Double discountPercentage = 15.0;
        Double applicableValue = 30.0;
        when(productService.applyDiscount(productId, discountPercentage, applicableValue)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = productController.applyDiscount(productId, discountPercentage, applicableValue);

        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals("Product not found with ID: " + productId, responseEntity.getBody());
        verify(productService, times(1)).applyDiscount(productId, discountPercentage, applicableValue);
    }

    @Test
    public void testApplyTax_Success() {
        Long productId = 1L;
        Double taxRate = 5.0;
        Double applicableValue = 50.0;
        Product product = dummyProducts.get(4);
        when(productService.applyTax(productId, taxRate, applicableValue)).thenReturn(Optional.of(product));

        ResponseEntity<?> responseEntity = productController.applyTax(productId, taxRate, applicableValue);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Tax applied successfully. " + product, responseEntity.getBody());
        verify(productService, times(1)).applyTax(productId, taxRate, applicableValue);
    }

    @Test
    public void testApplyTax_Failure() {
        Long productId = 2L;
        Double taxRate = 8.0;
        Double applicableValue = 30.0;
        when(productService.applyTax(productId, taxRate, applicableValue)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = productController.applyTax(productId, taxRate, applicableValue);

        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals("Product not found with ID: " + productId, responseEntity.getBody());
        verify(productService, times(1)).applyTax(productId, taxRate, applicableValue);
    }
}
