package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.ShoppingCart;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service managing shopping cart operations stored within the user's HTTP session.
 */
@Service
public class CartService {

    public static final String SESSION_CART_KEY = "SHOPPING_CART";

    private final ProductService productService;

    public CartService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieve the current session cart, or initialize a new one if not present.
     */
    public ShoppingCart getCart(HttpSession session) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute(SESSION_CART_KEY);
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute(SESSION_CART_KEY, cart);
        }
        return cart;
    }

    /**
     * Add a product with the specified quantity to the session cart.
     */
    public boolean addToCart(HttpSession session, Long productId, int quantity) {
        if (productId == null || quantity <= 0) {
            return false;
        }

        Optional<Product> productOpt = productService.getProductById(productId);
        if (productOpt.isPresent()) {
            ShoppingCart cart = getCart(session);
            cart.addItem(productOpt.get(), quantity);
            return true;
        }
        return false;
    }

    /**
     * Update the quantity of a product in the session cart.
     */
    public void updateQuantity(HttpSession session, Long productId, int quantity) {
        ShoppingCart cart = getCart(session);
        cart.updateQuantity(productId, quantity);
    }

    /**
     * Remove a product completely from the session cart.
     */
    public void removeFromCart(HttpSession session, Long productId) {
        ShoppingCart cart = getCart(session);
        cart.removeItem(productId);
    }

    /**
     * Clear all items from the session cart.
     */
    public void clearCart(HttpSession session) {
        ShoppingCart cart = getCart(session);
        cart.clear();
    }

    /**
     * Get the total item count currently in the cart.
     */
    public int getCartItemCount(HttpSession session) {
        return getCart(session).getTotalQuantity();
    }
}
