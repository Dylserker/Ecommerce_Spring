package com.judy.ecommerce.frontend.controller;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import jakarta.servlet.http.HttpSession;

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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/products")
    public String showAdminProducts(Model model, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/admin/product";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = (String) session.getAttribute("jwtToken");
        if (token != null) headers.set("Authorization", "Bearer " + token);
        List<Object> products = new ArrayList<>();
        try {
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<List> response = restTemplate.exchange(apiUrl, org.springframework.http.HttpMethod.GET, entity, List.class);
            products = response.getBody();
        } catch (Exception e) {}
        model.addAttribute("products", products);
        return "admin/product_list";
    }

    @GetMapping("/product/{id}")
    public String showAdminProductDetail(@PathVariable Long id, Model model, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/admin/product/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = (String) session.getAttribute("jwtToken");
        if (token != null) headers.set("Authorization", "Bearer " + token);
        Map product = new HashMap<>();
        try {
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, org.springframework.http.HttpMethod.GET, entity, Map.class);
            product = response.getBody();
        } catch (Exception e) {}
        model.addAttribute("product", product);
        return "admin/product";
    }


    @GetMapping("/login")
    public String showAdminLogin() {
        return "admin/login";
    }
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
    public String adminLogin(String email, String password, HttpSession session, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/auth/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().get("token") != null) {
                String token = response.getBody().get("token").toString();
                session.setAttribute("jwtToken", token);
                return "redirect:/admin/dashboard";
            } else {
                model.addAttribute("loginError", "Identifiants invalides");
                return "admin/login";
            }
        } catch (Exception e) {
            model.addAttribute("loginError", "Erreur lors de la connexion");
            return "admin/login";
        }
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/product";
        String promoUrl = "http://localhost:8080/api/product/sale";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = (String) session.getAttribute("jwtToken");
        if (token != null) {
            headers.set("Authorization", "Bearer " + token);
        }
        List<Object> allProducts = new ArrayList<>();
        List<Object> promoProducts = new ArrayList<>();
        try {
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<List> response = restTemplate.exchange(apiUrl, org.springframework.http.HttpMethod.GET, entity, List.class);
            allProducts = response.getBody();
        } catch (Exception e) {}
        try {
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<List> response = restTemplate.exchange(promoUrl, org.springframework.http.HttpMethod.GET, entity, List.class);
            promoProducts = response.getBody();
        } catch (Exception e) {}
        model.addAttribute("featuredProducts", allProducts != null && allProducts.size() > 0 ? allProducts.subList(0, Math.min(3, allProducts.size())) : new ArrayList<>());
        model.addAttribute("popularProducts", allProducts != null && allProducts.size() > 3 ? allProducts.subList(3, Math.min(6, allProducts.size())) : new ArrayList<>());
        model.addAttribute("promoProducts", promoProducts != null ? promoProducts : new ArrayList<>());
        return "admin/dashboard";
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

    @GetMapping("/logout")
    public String userLogout(HttpSession session, SessionStatus status) {
        session.invalidate();
        status.setComplete();
        return "redirect:/admin/login";
    }

}
