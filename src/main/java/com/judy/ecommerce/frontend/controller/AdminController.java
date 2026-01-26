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
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/login")
    public String showAdminLogin() {
        return "admin/login";
    }
        // --- CRUD Section ---
        @GetMapping("/CRUD/products")
        public String showCrudProducts() {
            return "admin/crud_products";
        }

        @GetMapping("/CRUD/users")
        public String showCrudUsers() {
            return "admin/crud_users";
        }

        @GetMapping("/CRUD/orders")
        public String showCrudOrders() {
            return "admin/crud_orders";
        }

    @PostMapping("/login")
    public String adminLogin(String email, String password) {
        // Implémenter la logique d'authentification admin
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard() {
        // Vérifier si l'admin est authentifié
        return "admin/dashboard";
    }

    @GetMapping("/logout")
    public String adminLogout() {
        // Implémenter la déconnexion
        return "redirect:/admin/login";
    }





    @GetMapping("/")
    public String showAdminDashboardHome() {
        return "admin/dashboard";
    }

    @GetMapping("/products")
    public String showAdminProducts() {
        return "admin/product_list";
    }

    @GetMapping("/product/{id}")
    public String showAdminProductDetail(@PathVariable Long id, Model model) {
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
        return "admin/product";
    }

    @GetMapping("/cart")
    public String showAdminCart(Model model) {
        List<Object> cartItems = new ArrayList<>();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartEmpty", cartItems.isEmpty());
        model.addAttribute("subtotal", 0.0);
        model.addAttribute("tax", 0.0);
        model.addAttribute("total", 0.0);
        return "admin/panier";
    }

    @GetMapping("/payment")
    public String showAdminPayment() {
        return "admin/payment";
    }

    @GetMapping("/profile")
    public String showAdminProfile(Model model) {
        return "admin/profile";
    }

}
