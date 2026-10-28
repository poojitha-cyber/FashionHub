package com.example.ecommerce.model;

import java.io.Serializable;

/**
 * CartItem model representing a product and its selected quantity in the shopping cart.
 */
public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private Product product;
    private int quantity;

    public CartItem() {
    }

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        if (product == null) {
            return 0.0;
        }
        return product.getPrice() * quantity;
    }

    public String getFormattedSubtotal() {
        return String.format("$%.2f", getSubtotal());
    }
}
