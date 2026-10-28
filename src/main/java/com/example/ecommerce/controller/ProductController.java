package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * Controller handling product listings and individual product details.
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    public ProductController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping
    public String listProducts(@RequestParam(required = false) String category,
                               @RequestParam(required = false) String search) {
        StringBuilder redirect = new StringBuilder("redirect:/");
        boolean hasParam = false;
        if (category != null && !category.isBlank()) {
            redirect.append("?category=").append(category);
            hasParam = true;
        }
        if (search != null && !search.isBlank()) {
            redirect.append(hasParam ? "&" : "?").append("search=").append(search);
        }
        return redirect.toString();
    }

    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, HttpSession session, Model model) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isEmpty()) {
            return "redirect:/?error=product_not_found";
        }

        Product product = productOpt.get();
        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", productService.getRelatedProducts(product.getId(), product.getCategory(), 4));
        model.addAttribute("cartItemCount", cartService.getCartItemCount(session));

        return "product-detail";
    }
}
