package com.example.ecommerce.controller;

import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.ShoppingCart;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controller handling shopping cart interactions, quantity adjustments,
 * checkout flow, and REST endpoints for interactive JavaScript frontend updates.
 */
@Controller
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    /**
     * Display the shopping cart page.
     */
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        ShoppingCart cart = cartService.getCart(session);
        model.addAttribute("cart", cart);
        model.addAttribute("cartItemCount", cart.getTotalQuantity());
        return "cart";
    }

    /**
     * Standard Form POST: Add item to cart and redirect.
     */
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            @RequestParam(required = false) String returnUrl,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        boolean added = cartService.addToCart(session, productId, quantity);
        if (added) {
            Optional<Product> productOpt = productService.getProductById(productId);
            String name = productOpt.map(Product::getName).orElse("Product");
            redirectAttributes.addFlashAttribute("successMessage", "Added \"" + name + "\" to your shopping cart!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not add item to cart.");
        }

        if (returnUrl != null && !returnUrl.isBlank()) {
            return "redirect:" + returnUrl;
        }
        return "redirect:/cart";
    }

    /**
     * Standard Form POST: Update item quantity in cart.
     */
    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam Long productId,
                                 @RequestParam int quantity,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        cartService.updateQuantity(session, productId, quantity);
        redirectAttributes.addFlashAttribute("successMessage", "Cart updated successfully.");
        return "redirect:/cart";
    }

    /**
     * Standard GET/POST: Remove item from cart.
     */
    @RequestMapping(value = "/cart/remove/{productId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String removeItem(@PathVariable Long productId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        cartService.removeFromCart(session, productId);
        redirectAttributes.addFlashAttribute("successMessage", "Item removed from cart.");
        return "redirect:/cart";
    }

    /**
     * Clear all items in the shopping cart.
     */
    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        cartService.clearCart(session);
        redirectAttributes.addFlashAttribute("successMessage", "Cart has been cleared.");
        return "redirect:/cart";
    }

    /**
     * Checkout page / order confirmation simulation.
     */
    @GetMapping("/cart/checkout")
    public String checkout(HttpSession session, Model model) {
        ShoppingCart cart = cartService.getCart(session);
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }

        // Prepare order summary for receipt before clearing cart
        model.addAttribute("orderItems", cart.getItems());
        model.addAttribute("subtotal", cart.getFormattedSubtotal());
        model.addAttribute("shipping", cart.getFormattedShipping());
        model.addAttribute("tax", cart.getFormattedTax());
        model.addAttribute("total", cart.getFormattedTotal());
        model.addAttribute("totalQuantity", cart.getTotalQuantity());
        model.addAttribute("orderNumber", "ORD-" + System.currentTimeMillis() % 1000000);

        // Clear cart after order completion
        cartService.clearCart(session);
        model.addAttribute("cartItemCount", 0);

        return "checkout-success";
    }

    // -------------------------------------------------------------
    // REST API Endpoints for Dynamic JavaScript (AJAX) interactions
    // -------------------------------------------------------------

    /**
     * AJAX endpoint to add item without page reload.
     */
    @PostMapping("/api/cart/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiAddToCart(@RequestParam Long productId,
                                                            @RequestParam(defaultValue = "1") int quantity,
                                                            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        boolean success = cartService.addToCart(session, productId, quantity);

        if (success) {
            Optional<Product> productOpt = productService.getProductById(productId);
            ShoppingCart cart = cartService.getCart(session);
            response.put("success", true);
            response.put("message", "Added to cart!");
            response.put("productName", productOpt.map(Product::getName).orElse("Product"));
            response.put("cartCount", cart.getTotalQuantity());
            response.put("cartSubtotal", cart.getFormattedSubtotal());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Product not found or invalid quantity.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * AJAX endpoint to update quantity and get updated pricing breakdown.
     */
    @PostMapping("/api/cart/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiUpdateQuantity(@RequestParam Long productId,
                                                                 @RequestParam int quantity,
                                                                 HttpSession session) {
        cartService.updateQuantity(session, productId, quantity);
        ShoppingCart cart = cartService.getCart(session);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("cartCount", cart.getTotalQuantity());
        response.put("subtotal", cart.getFormattedSubtotal());
        response.put("shipping", cart.getFormattedShipping());
        response.put("tax", cart.getFormattedTax());
        response.put("total", cart.getFormattedTotal());
        response.put("isEmpty", cart.isEmpty());

        CartItem item = cart.getItemsMap().get(productId);
        if (item != null) {
            response.put("itemSubtotal", item.getFormattedSubtotal());
            response.put("itemQuantity", item.getQuantity());
        } else {
            response.put("itemRemoved", true);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * AJAX endpoint to remove item directly.
     */
    @PostMapping("/api/cart/remove/{productId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiRemoveItem(@PathVariable Long productId,
                                                             HttpSession session) {
        cartService.removeFromCart(session, productId);
        ShoppingCart cart = cartService.getCart(session);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("cartCount", cart.getTotalQuantity());
        response.put("subtotal", cart.getFormattedSubtotal());
        response.put("shipping", cart.getFormattedShipping());
        response.put("tax", cart.getFormattedTax());
        response.put("total", cart.getFormattedTotal());
        response.put("isEmpty", cart.isEmpty());

        return ResponseEntity.ok(response);
    }
}
