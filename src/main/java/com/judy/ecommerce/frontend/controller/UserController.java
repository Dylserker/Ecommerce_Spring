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
    @GetMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/user/cart/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = (String) session.getAttribute("jwtToken");
        if (token != null) {
            headers.set("Authorization", "Bearer " + token);
        }
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            restTemplate.postForEntity(apiUrl, entity, List.class);
        } catch (Exception e) {
        }
        return "redirect:/user/cart";
    }

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
        return "user/dashboard";
    }

    @GetMapping("/products")
    public String showProducts(@RequestParam(value = "categoryId", required = false) Integer categoryId, Model model, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/product";
        String categoriesUrl = "http://localhost:8080/api/category";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = (String) session.getAttribute("jwtToken");
        if (token != null) {
            headers.set("Authorization", "Bearer " + token);
        }
        try {
            ResponseEntity<List> catResponse = restTemplate.exchange(categoriesUrl, org.springframework.http.HttpMethod.GET, new HttpEntity<>(headers), List.class);
            model.addAttribute("categories", catResponse.getBody());
        } catch (Exception e) {
            model.addAttribute("categories", new ArrayList<>());
        }
        List products = new ArrayList<>();
        try {
            if (categoryId != null) {
                String searchUrl = "http://localhost:8080/api/product/search/";
                Map<String, Object> filters = new HashMap<>();
                filters.put("categoryId", categoryId);
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(filters, headers);
                ResponseEntity<List> response = restTemplate.postForEntity(searchUrl, entity, List.class);
                products = response.getBody();
            } else {
                HttpEntity<Void> entity = new HttpEntity<>(headers);
                ResponseEntity<List> response = restTemplate.exchange(apiUrl, org.springframework.http.HttpMethod.GET, entity, List.class);
                products = response.getBody();
            }
        } catch (Exception e) {
            model.addAttribute("error", true);
        }
        model.addAttribute("products", products);
        model.addAttribute("selectedCategoryId", categoryId);
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
    public String showCart(Model model, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/user/cart";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = (String) session.getAttribute("jwtToken");
        if (token != null) {
            headers.set("Authorization", "Bearer " + token);
        }
        List<Object> cartItems = new ArrayList<>();
        try {
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<List> response = restTemplate.exchange(apiUrl, org.springframework.http.HttpMethod.GET, entity, List.class);
            cartItems = response.getBody();
        } catch (Exception e) {
        }
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartEmpty", cartItems == null || cartItems.isEmpty());

        // Calcul du sous-total, taxe et total (robuste pour produit simple ou {product, quantity})
        double subtotal = 0.0;
        double tax = 0.0;
        double total = 0.0;
        double taxRate = 0.20; // 20% TVA

        if (cartItems != null) {
            for (Object obj : cartItems) {
                if (obj instanceof Map) {
                    Map item = (Map) obj;
                    double price = 0.0;
                    int quantity = 1;
                    // Cas 1 : produit simple (item['price'])
                    if (item.containsKey("price")) {
                        Object priceObj = item.get("price");
                        if (priceObj instanceof Number) {
                            price = ((Number) priceObj).doubleValue();
                        } else if (priceObj != null) {
                            try { price = Double.parseDouble(priceObj.toString()); } catch (Exception ignore) {}
                        }
                        Object qtyObj = item.get("quantity");
                        if (qtyObj instanceof Number) {
                            quantity = ((Number) qtyObj).intValue();
                        } else if (qtyObj != null) {
                            try { quantity = Integer.parseInt(qtyObj.toString()); } catch (Exception ignore) {}
                        }
                    }
                    // Cas 2 : objet {product, quantity}
                    else if (item.containsKey("product")) {
                        Object productObj = item.get("product");
                        if (productObj instanceof Map) {
                            Map product = (Map) productObj;
                            Object priceObj = product.get("price");
                            if (priceObj instanceof Number) {
                                price = ((Number) priceObj).doubleValue();
                            } else if (priceObj != null) {
                                try { price = Double.parseDouble(priceObj.toString()); } catch (Exception ignore) {}
                            }
                        }
                        Object qtyObj = item.get("quantity");
                        if (qtyObj instanceof Number) {
                            quantity = ((Number) qtyObj).intValue();
                        } else if (qtyObj != null) {
                            try { quantity = Integer.parseInt(qtyObj.toString()); } catch (Exception ignore) {}
                        }
                    }
                    subtotal += price * quantity;
                }
            }
        }
        tax = subtotal * taxRate;
        total = subtotal + tax;
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("tax", tax);
        model.addAttribute("total", total);
        return "user/panier";
    }

    @GetMapping("/payment")
    public String showPayment() {
        return "user/payment";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = (String) session.getAttribute("jwtToken");
        if (token != null) {
            headers.set("Authorization", "Bearer " + token);
        }
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, org.springframework.http.HttpMethod.GET, entity, Map.class);
            model.addAttribute("user", response.getBody());
        } catch (Exception e) {
            model.addAttribute("user", null);
            model.addAttribute("error", true);
        }
        return "user/profile";
    }

    @GetMapping("/logout")
    public String userLogout(HttpSession session, SessionStatus status) {
        session.invalidate();
        status.setComplete();
        return "redirect:/user/login";
    }
}