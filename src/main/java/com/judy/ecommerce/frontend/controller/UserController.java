package com.judy.ecommerce.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.List;

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
        // TODO: Implémenter la logique d'authentification client
        return "redirect:/user/dashboard";
    }

    @GetMapping("/register")
    public String showRegister() {
        // TODO: Créer la page d'inscription
        return "user/register";
    }

    @PostMapping("/register")
    public String register(String email, String password, String name) {
        // TODO: Implémenter l'enregistrement client
        return "redirect:/user/login";
    }

    @GetMapping("/dashboard")
    public String showUserDashboard() {
        // TODO: Vérifier si le client est authentifié (optionnel)
        return "user/dashboard";
    }

    @GetMapping("/products")
    public String showProducts() {
        return "user/product_list";
    }

    @GetMapping("/product/{id}")
    public String showProductDetail() {
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
        // TODO: Implémenter la déconnexion
        return "redirect:/user/login";
    }
}
