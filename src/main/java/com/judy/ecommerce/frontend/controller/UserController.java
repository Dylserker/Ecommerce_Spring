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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
@SessionAttributes("jwtToken")
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
    public String userLogin(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/auth/login";
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", email);
        loginRequest.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(loginRequest, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            String token = (String) response.getBody().get("token");
            session.setAttribute("jwtToken", token);
            return "redirect:/user/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", true);
            return "user/login";
        }
    }

    @GetMapping("/register")
    public String showRegister() {
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password, @RequestParam String confirmPassword, Model model, HttpSession session) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", true);
            return "user/register";
        }
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/auth/register";
        Map<String, String> registerRequest = new HashMap<>();
        registerRequest.put("firstName", firstName);
        registerRequest.put("lastName", lastName);
        registerRequest.put("email", email);
        registerRequest.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(registerRequest, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            String token = (String) response.getBody().get("token");
            session.setAttribute("jwtToken", token);
            return "redirect:/user/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", true);
            return "user/register";
        }
    }

    @GetMapping("/dashboard")
    public String showUserDashboard() {
        // Vérifier si le client est authentifié (optionnel)
        return "user/dashboard";
    }

    @GetMapping("/products")
    public String showProducts(Model model, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/product";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = (String) session.getAttribute("jwtToken");
        if (token != null) {
            headers.set("Authorization", "Bearer " + token);
        }
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<List> response = restTemplate.exchange(apiUrl, org.springframework.http.HttpMethod.GET, entity, List.class);
            model.addAttribute("products", response.getBody());
        } catch (Exception e) {
            model.addAttribute("products", new ArrayList<>());
            model.addAttribute("error", true);
        }
        return "user/product_list";
    }

    @GetMapping("/product/{id}")
    public String showProductDetail(@PathVariable Long id, Model model, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/product/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = (String) session.getAttribute("jwtToken");
        if (token != null) {
            headers.set("Authorization", "Bearer " + token);
        }
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, org.springframework.http.HttpMethod.GET, entity, Map.class);
            model.addAttribute("product", response.getBody());
        } catch (Exception e) {
            model.addAttribute("product", null);
            model.addAttribute("error", true);
        }
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
    public String userLogout(HttpSession session, SessionStatus status) {
        session.invalidate();
        status.setComplete();
        return "redirect:/user/login";
    }
}