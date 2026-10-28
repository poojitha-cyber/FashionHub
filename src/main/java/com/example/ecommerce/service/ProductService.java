package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service managing sample product catalog in-memory.
 * No external database required - perfect for zero-config startup and DevOps testing.
 */
@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();

    @PostConstruct
    public void init() {
        // Electronics
        products.add(new Product(
                1L,
                "Wireless Noise-Cancelling Headphones",
                "Immerse yourself in rich, high-fidelity acoustics with hybrid active noise cancellation, ambient transparency mode, and up to 40 hours of playtime on a single charge.",
                149.99,
                "Electronics",
                "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=800&q=80",
                4.8,
                142,
                true,
                List.of("Hybrid Active Noise Cancellation (ANC)", "40-Hour Battery Life & Fast USB-C Charging", "Plush Memory Foam Ear Cushions", "Bluetooth 5.3 with Multi-Point Pairing")
        ));

        products.add(new Product(
                2L,
                "Ultra-Slim Smartwatch Pro",
                "Advanced fitness and health companion featuring an always-on AMOLED touchscreen, blood oxygen and heart rate tracking, built-in GPS, and 5 ATM water resistance.",
                199.99,
                "Electronics",
                "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=800&q=80",
                4.6,
                98,
                true,
                List.of("1.43\" AMOLED Display with Sapphire Glass", "All-day SpO2, Heart Rate, & Sleep Metrics", "Built-in Dual-band GPS", "Up to 7 Days Battery Life")
        ));

        products.add(new Product(
                3L,
                "RGB Mechanical Gaming Keyboard",
                "Engineered for competitive typing and gaming with responsive hot-swappable tactile switches, per-key RGB backlighting, and an aircraft-grade aluminum top frame.",
                89.99,
                "Electronics",
                "https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=800&q=80",
                4.7,
                215,
                true,
                List.of("Hot-Swappable Tactile Mechanical Switches", "Customizable Per-Key RGB Lighting", "Durable Double-Shot PBT Keycaps", "Detachable Braided USB-C Cable")
        ));

        // Clothing
        products.add(new Product(
                4L,
                "Organic Cotton Crewneck T-Shirt",
                "Made from 100% GOTS-certified combed organic cotton for an ultra-soft feel, tailored drape, reinforced neckline, and exceptional durability wash after wash.",
                29.99,
                "Clothing",
                "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?auto=format&fit=crop&w=800&q=80",
                4.5,
                320,
                true,
                List.of("100% GOTS-Certified Organic Cotton", "Breathable Mid-Weight 180 GSM Fabric", "Pre-Shrunk for Consistent Fit", "Eco-friendly Low-Impact Dyes")
        ));

        products.add(new Product(
                5L,
                "Vintage Washed Denim Jacket",
                "A timeless outerwear staple crafted from 12.5oz rugged cotton denim with subtle vintage wash fading, brass hardware, and dual front chest flap pockets.",
                79.99,
                "Clothing",
                "https://images.unsplash.com/photo-1551028719-00167b16eac5?auto=format&fit=crop&w=800&q=80",
                4.7,
                146,
                true,
                List.of("Heavyweight 12.5oz Durable Cotton Denim", "Classic Relaxed Fit for Layering", "Antiqued Brass Shank Buttons", "Dual Welt Hand Pockets & Chest Pockets")
        ));

        products.add(new Product(
                6L,
                "Comfort-Stretch Chino Trousers",
                "Everyday modern pants engineered with a four-way stretch cotton-twill blend, wrinkle-resistant finish, and a flattering tapered silhouette.",
                49.99,
                "Clothing",
                "https://images.unsplash.com/photo-1473966968600-fa801b869a1a?auto=format&fit=crop&w=800&q=80",
                4.4,
                88,
                true,
                List.of("97% Cotton, 3% Elastane Flex Twill", "Wrinkle & Stain-Resistant Coating", "Tailored Tapered Leg Profile", "Hidden Zipper Security Pocket")
        ));

        // Footwear
        products.add(new Product(
                7L,
                "Cloudfoam Air Running Shoes",
                "Lightweight performance road runners with breathable engineered mesh uppers, responsive energy-return foam cushioning, and high-abrasion rubber outsoles.",
                119.99,
                "Footwear",
                "https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=800&q=80",
                4.9,
                420,
                true,
                List.of("Engineered Breathable Jacquard Mesh Upper", "High-Rebound Energy Return Midsole", "Strategic Rubber Traction Pods", "Ortholite Antimicrobial Sockliner")
        ));

        products.add(new Product(
                8L,
                "Classic Canvas High-Top Sneakers",
                "Iconic street-style casual sneakers built with rugged 10oz canvas, vulcanized rubber midsoles, and signature textured diamond tread for all-day grip.",
                59.99,
                "Footwear",
                "https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?auto=format&fit=crop&w=800&q=80",
                4.6,
                175,
                true,
                List.of("Durable 10oz Cotton Canvas Upper", "Protective Vulcanized Rubber Toe Cap", "Cushioned EVA Footbed", "Signature Grip Rubber Outsole")
        ));

        products.add(new Product(
                9L,
                "Waterproof Trail Hiking Boots",
                "Rugged all-terrain outdoor boots featuring a waterproof breathable membrane, reinforced rubber rand, EVA impact absorber, and deep multidirectional lugs.",
                139.99,
                "Footwear",
                "https://images.unsplash.com/photo-1520639888713-7851133b1ed0?auto=format&fit=crop&w=800&q=80",
                4.8,
                92,
                true,
                List.of("HydroGuard 100% Waterproof Bootie Membrane", "Ghillie Speed-Lacing Eyelets", "Molded TPU Heel Counter & Ankle Support", "4mm Deep Multi-Directional Lugs")
        ));

        // Accessories
        products.add(new Product(
                10L,
                "Minimalist Polarized Sunglasses",
                "Featherlight aviator sunglasses engineered with TAC polarized UV400 lenses, corrosion-resistant magnesium-aluminum alloy frames, and spring hinges.",
                39.99,
                "Accessories",
                "https://images.unsplash.com/photo-1511499767150-a48a237f0083?auto=format&fit=crop&w=800&q=80",
                4.5,
                112,
                true,
                List.of("TAC 9-Layer Polarized Lenses", "100% UV400 Protection Against UVA/UVB", "Ultralight Matte Aluminum-Magnesium Alloy", "Adjustable Silicone Nose Pads")
        ));

        products.add(new Product(
                11L,
                "Full-Grain Leather Bi-Fold Wallet",
                "Handcrafted from vegetable-tanned full-grain cowhide leather that develops a rich natural patina over time. Equipped with certified RFID shielding.",
                45.99,
                "Accessories",
                "https://images.unsplash.com/photo-1627123424574-724758594e93?auto=format&fit=crop&w=800&q=80",
                4.7,
                230,
                true,
                List.of("Handcrafted 100% Full-Grain Vegetable Leather", "Certified RFID-Blocking Inner Lining", "8 Dedicated Card Slots & Double Currency Billfold", "Ultra-Slim 0.4\" Pocket Profile")
        ));

        products.add(new Product(
                12L,
                "Water-Resistant Commuter Backpack",
                "Ergonomic urban backpack constructed from 900D water-repellent oxford fabric, featuring a padded 15.6-inch laptop sleeve and hidden anti-theft back pocket.",
                69.99,
                "Accessories",
                "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&w=800&q=80",
                4.8,
                194,
                true,
                List.of("900D High-Density Water-Repellent Oxford", "Shockproof Padded 15.6\" Laptop Sleeve", "External USB Pass-Through Port", "Luggage Trolley Strap & Breathable Mesh Back")
        ));
    }

    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(products);
    }

    public Optional<Product> getProductById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public List<Product> getProductsByCategory(String category) {
        if (category == null || category.isBlank() || "all".equalsIgnoreCase(category)) {
            return getAllProducts();
        }
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category.trim()))
                .collect(Collectors.toList());
    }

    public List<Product> searchProducts(String query) {
        if (query == null || query.isBlank()) {
            return getAllProducts();
        }
        String lowerQuery = query.trim().toLowerCase();
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerQuery)
                        || p.getDescription().toLowerCase().contains(lowerQuery)
                        || p.getCategory().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public List<String> getAllCategories() {
        return List.of("Electronics", "Clothing", "Footwear", "Accessories");
    }

    public List<Product> getFeaturedProducts() {
        // Return top 4 highly-rated flagship products for homepage hero showcase
        return products.stream()
                .filter(p -> p.getRating() >= 4.7)
                .limit(4)
                .collect(Collectors.toList());
    }

    public List<Product> getRelatedProducts(Long productId, String category, int limit) {
        return products.stream()
                .filter(p -> !p.getId().equals(productId) && p.getCategory().equalsIgnoreCase(category))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
