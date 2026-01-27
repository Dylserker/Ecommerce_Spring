
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
    public String adminLogin(String email, String password) {
        return "redirect:/admin/dashboard";
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
