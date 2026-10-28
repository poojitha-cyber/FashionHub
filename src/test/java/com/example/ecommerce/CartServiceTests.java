package com.example.ecommerce;

import com.example.ecommerce.model.ShoppingCart;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CartService Unit Tests")
class CartServiceTests {

    private CartService cartService;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        ProductService productService = new ProductService();
        productService.init();
        cartService = new CartService(productService);
        session = new MockHttpSession();
    }

    @Test
    @DisplayName("Should initialize an empty shopping cart in session")
    void testGetEmptyCart() {
        ShoppingCart cart = cartService.getCart(session);
        assertNotNull(cart);
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getTotalQuantity());
        assertEquals(0.0, cart.getTotal(), 0.001);
    }

    @Test
    @DisplayName("Should add product to cart and update quantities")
    void testAddToCart() {
        boolean added = cartService.addToCart(session, 1L, 2);
        assertTrue(added);

        ShoppingCart cart = cartService.getCart(session);
        assertFalse(cart.isEmpty());
        assertEquals(2, cart.getTotalQuantity());
        assertEquals(1, cart.getItems().size());

        // Add 1 more of same product
        cartService.addToCart(session, 1L, 1);
        assertEquals(3, cart.getTotalQuantity());
    }

    @Test
    @DisplayName("Should correctly calculate subtotals, tax, and shipping")
    void testCartCalculations() {
        // Product 4 is $29.99 (below $50 threshold, so standard shipping applies)
        cartService.addToCart(session, 4L, 1);
        ShoppingCart cart = cartService.getCart(session);

        assertEquals(29.99, cart.getSubtotal(), 0.001);
        assertEquals(5.99, cart.getShipping(), 0.001);
        assertEquals(29.99 * 0.08, cart.getTax(), 0.001);

        // Add 2nd shirt -> Subtotal becomes 59.98 (exceeds $50 threshold, free shipping!)
        cartService.addToCart(session, 4L, 1);
        assertEquals(59.98, cart.getSubtotal(), 0.001);
        assertEquals(0.0, cart.getShipping(), 0.001);
    }

    @Test
    @DisplayName("Should update quantity or remove item if set to 0")
    void testUpdateQuantity() {
        cartService.addToCart(session, 1L, 3);
        cartService.updateQuantity(session, 1L, 1);

        ShoppingCart cart = cartService.getCart(session);
        assertEquals(1, cart.getTotalQuantity());

        // Update to 0 removes the item
        cartService.updateQuantity(session, 1L, 0);
        assertTrue(cart.isEmpty());
    }

    @Test
    @DisplayName("Should remove product from cart")
    void testRemoveFromCart() {
        cartService.addToCart(session, 1L, 1);
        cartService.addToCart(session, 2L, 1);
        assertEquals(2, cartService.getCartItemCount(session));

        cartService.removeFromCart(session, 1L);
        assertEquals(1, cartService.getCartItemCount(session));
    }

    @Test
    @DisplayName("Should clear all items from cart")
    void testClearCart() {
        cartService.addToCart(session, 1L, 2);
        cartService.addToCart(session, 2L, 1);
        assertFalse(cartService.getCart(session).isEmpty());

        cartService.clearCart(session);
        assertTrue(cartService.getCart(session).isEmpty());
        assertEquals(0, cartService.getCartItemCount(session));
    }
}
