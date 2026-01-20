package com.judy.ecommerce.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/login")
    public String showAdminLogin() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String adminLogin(String email, String password) {
        // TODO: Implémenter la logique d'authentification admin
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard() {
        // TODO: Vérifier si l'admin est authentifié
        return "admin/dashboard";
    }

    @GetMapping("/logout")
    public String adminLogout() {
        // TODO: Implémenter la déconnexion
        return "redirect:/admin/login";
    }
}
