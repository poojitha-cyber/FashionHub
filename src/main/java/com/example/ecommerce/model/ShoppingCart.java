package com.example.ecommerce.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ShoppingCart represents a user's active shopping cart session.
 * Manages cart items, quantities, and calculates subtotals, tax, shipping, and total.
 */
public class ShoppingCart implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final double TAX_RATE = 0.08; // 8% sales tax
    private static final double FREE_SHIPPING_THRESHOLD = 50.0;
    private static final double STANDARD_SHIPPING_FEE = 5.99;

    private final Map<Long, CartItem> items = new LinkedHashMap<>();

    public ShoppingCart() {
    }

    public void addItem(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return;
        }

        Long productId = product.getId();
        if (items.containsKey(productId)) {
            CartItem existing = items.get(productId);
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            items.put(productId, new CartItem(product, quantity));
        }
    }

    public void updateQuantity(Long productId, int quantity) {
        if (productId == null) {
            return;
        }

        if (quantity <= 0) {
            removeItem(productId);
        } else if (items.containsKey(productId)) {
            items.get(productId).setQuantity(quantity);
        }
    }

    public void removeItem(Long productId) {
        if (productId != null) {
            items.remove(productId);
        }
    }

    public void clear() {
        items.clear();
    }

    public Collection<CartItem> getItems() {
        return items.values();
    }

    public Map<Long, CartItem> getItemsMap() {
        return items;
    }

    public int getTotalQuantity() {
        return items.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public double getSubtotal() {
        return items.values().stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    public double getShipping() {
        if (items.isEmpty()) {
            return 0.0;
        }
        return getSubtotal() >= FREE_SHIPPING_THRESHOLD ? 0.0 : STANDARD_SHIPPING_FEE;
    }

    public double getTax() {
        return getSubtotal() * TAX_RATE;
    }

    public double getTotal() {
        if (items.isEmpty()) {
            return 0.0;
        }
        return getSubtotal() + getTax() + getShipping();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean isFreeShippingEligible() {
        return getSubtotal() >= FREE_SHIPPING_THRESHOLD;
    }

    public double getAmountForFreeShipping() {
        double subtotal = getSubtotal();
        return subtotal >= FREE_SHIPPING_THRESHOLD ? 0.0 : (FREE_SHIPPING_THRESHOLD - subtotal);
    }

    // Formatted presentation getters
    public String getFormattedSubtotal() {
        return String.format("$%.2f", getSubtotal());
    }

    public String getFormattedShipping() {
        double shipping = getShipping();
        return shipping == 0.0 ? "FREE" : String.format("$%.2f", shipping);
    }

    public String getFormattedTax() {
        return String.format("$%.2f", getTax());
    }

    public String getFormattedTotal() {
        return String.format("$%.2f", getTotal());
    }
}
