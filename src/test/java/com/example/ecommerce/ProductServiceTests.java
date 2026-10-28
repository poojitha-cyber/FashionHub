package com.example.ecommerce;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductService Unit Tests")
class ProductServiceTests {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
        productService.init();
    }

    @Test
    @DisplayName("Should retrieve all catalog products")
    void testGetAllProducts() {
        List<Product> products = productService.getAllProducts();
        assertNotNull(products);
        assertEquals(12, products.size(), "Catalog should contain 12 sample items");
    }

    @Test
    @DisplayName("Should find product by existing ID")
    void testGetProductById() {
        Optional<Product> productOpt = productService.getProductById(1L);
        assertTrue(productOpt.isPresent());
        assertEquals("Wireless Noise-Cancelling Headphones", productOpt.get().getName());
        assertEquals("Electronics", productOpt.get().getCategory());
    }

    @Test
    @DisplayName("Should filter products by category")
    void testGetProductsByCategory() {
        List<Product> electronics = productService.getProductsByCategory("Electronics");
        assertEquals(3, electronics.size());
        assertTrue(electronics.stream().allMatch(p -> "Electronics".equalsIgnoreCase(p.getCategory())));

        List<Product> clothing = productService.getProductsByCategory("Clothing");
        assertEquals(3, clothing.size());

        List<Product> all = productService.getProductsByCategory("all");
        assertEquals(12, all.size());
    }

    @Test
    @DisplayName("Should search products by keyword in name or description")
    void testSearchProducts() {
        List<Product> results = productService.searchProducts("jacket");
        assertFalse(results.isEmpty());
        assertTrue(results.get(0).getName().toLowerCase().contains("jacket"));

        List<Product> emptyQueryResults = productService.searchProducts("");
        assertEquals(12, emptyQueryResults.size());
    }

    @Test
    @DisplayName("Should retrieve featured top-rated products")
    void testGetFeaturedProducts() {
        List<Product> featured = productService.getFeaturedProducts();
        assertNotNull(featured);
        assertFalse(featured.isEmpty());
        assertTrue(featured.stream().allMatch(p -> p.getRating() >= 4.7));
    }
}
