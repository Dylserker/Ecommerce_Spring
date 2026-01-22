package com.judy.ecommerce.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class PublicController {

    @GetMapping
    public String showHomePage() {
        return "index";
    }

    @GetMapping("/products")
    public String showPublicProducts(Model model) {
        // TODO: Charger les produits depuis la base de données
        return "public/product_list";
    }

    @GetMapping("/product/{id}")
    public String showPublicProductDetail(@PathVariable Long id, Model model) {
        // TODO: Charger le produit depuis la base de données
        model.addAttribute("productId", id);
        return "public/product";
    }

    @GetMapping("/cart")
    public String showPublicCart(Model model) {
        // Initialiser les variables pour le panier public
        List<Object> cartItems = new ArrayList<>();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartEmpty", cartItems.isEmpty());
        model.addAttribute("subtotal", 0.0);
        model.addAttribute("tax", 0.0);
        model.addAttribute("total", 0.0);
        model.addAttribute("isPublic", true);
        return "public/panier";
    }
}
