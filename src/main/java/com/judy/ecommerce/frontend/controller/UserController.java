package com.judy.ecommerce.frontend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/")
    public String showUserDashboardHome() {
        return "user/dashboard";
    }

    @GetMapping("/login")
    public String showUserLogin() {
        return "user/login";
    }

    @PostMapping("/login")
    public String userLogin(String email, String password) {
        // Implémenter la logique d'authentification client
        return "redirect:/user/dashboard";
    }

    @GetMapping("/register")
    public String showRegister() {
        // Créer la page d'inscription
        return "user/register";
    }

    @PostMapping("/register")
    public String register(String email, String password, String name) {
        // Implémenter l'enregistrement client
        return "redirect:/user/login";
    }

    @GetMapping("/dashboard")
    public String showUserDashboard() {
        // Vérifier si le client est authentifié (optionnel)
        return "user/dashboard";
    }

    @GetMapping("/products")
    public String showProducts() {
        return "user/product_list";
    }

    @GetMapping("/product/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        // Charger le produit depuis la base de données en attendant la vrai DB
        Map<String, Object> product = new HashMap<>();
        product.put("id", id);
        product.put("name", "Produit " + id);
        product.put("category", "Catégorie");
        product.put("price", 199.0);
        product.put("originalPrice", 249.0);
        product.put("discount", 20);
        product.put("stock", 12);
        product.put("rating", 4.5);
        product.put("reviews", 128);
        product.put("description", "Description du produit " + id);
        product.put("brand", "Marque X");
        product.put("color", "Noir");
        product.put("size", "M");
        product.put("material", "Composite");
        product.put("weight", 1.2);
        product.put("sku", "SKU-" + id);
        product.put("imageUrl", "/img/sample-product.jpg");

        List<Map<String, Object>> related = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> rel = new HashMap<>();
            rel.put("id", id + i);
            rel.put("name", "Produit lié " + (id + i));
            rel.put("price", 149.0 + i * 10);
            rel.put("imageUrl", "/img/sample-product.jpg");
            related.add(rel);
        }

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", related);
        return "user/product";
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
        // Initialiser les variables pour le panier
        List<Object> cartItems = new ArrayList<>();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartEmpty", cartItems.isEmpty());
        model.addAttribute("subtotal", 0.0);
        model.addAttribute("tax", 0.0);
        model.addAttribute("total", 0.0);
        return "user/panier";
    }

    @GetMapping("/payment")
    public String showPayment() {
        return "user/payment";
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "user/profile";
    }

    @GetMapping("/logout")
    public String userLogout() {
        // Implémenter la déconnexion
        return "redirect:/user/login";
    }
}
