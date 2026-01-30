package com.judy.ecommerce.frontend.controller;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.ParameterizedTypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PublicController {

    private void addCategoriesToModel(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String categoriesUrl = "http://localhost:8080/api/category";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<List> catResponse = restTemplate.exchange(categoriesUrl, org.springframework.http.HttpMethod.GET, entity, List.class);
            model.addAttribute("categories", catResponse.getBody());
        } catch (Exception e) {
            model.addAttribute("categories", new ArrayList<>());
        }
    }

    @GetMapping("/dashboard")
    public String showPublicDashboard(Model model) {
        addCategoriesToModel(model);
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/product";
        String promoUrl = "http://localhost:8080/api/product/sale";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Map<String, Object>> allProducts = new ArrayList<>();
        List<Map<String, Object>> promoProducts = new ArrayList<>();
        try {
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, org.springframework.http.HttpMethod.GET, entity, Map.class);
            Map body = response.getBody();
            if (body != null && body.get("products") instanceof List) {
                allProducts = (List<Map<String, Object>>) body.get("products");
            }
        } catch (Exception e) {}
        try {
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(promoUrl, org.springframework.http.HttpMethod.GET, entity, Map.class);
            Map body = response.getBody();
            if (body != null && body.get("products") instanceof List) {
                promoProducts = (List<Map<String, Object>>) body.get("products");
            }
        } catch (Exception e) {}
        model.addAttribute("featuredProducts", allProducts != null && allProducts.size() > 0 ? allProducts.subList(0, Math.min(3, allProducts.size())) : new ArrayList<>());
        model.addAttribute("popularProducts", allProducts != null && allProducts.size() > 3 ? allProducts.subList(3, Math.min(6, allProducts.size())) : new ArrayList<>());
        model.addAttribute("promoProducts", promoProducts != null ? promoProducts : new ArrayList<>());
        return "public/dashboard";
    }

    @GetMapping
    public String showHomePage(Model model) {
        addCategoriesToModel(model);
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/product";
        String promoUrl = "http://localhost:8080/api/product/sale";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Map<String, Object>> allProducts = new ArrayList<>();
        List<Map<String, Object>> promoProducts = new ArrayList<>();
        try {
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, org.springframework.http.HttpMethod.GET, entity, Map.class);
            Map body = response.getBody();
            if (body != null && body.get("products") instanceof List) {
                allProducts = (List<Map<String, Object>>) body.get("products");
            }
        } catch (Exception e) {}
        try {
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(promoUrl, org.springframework.http.HttpMethod.GET, entity, Map.class);
            Map body = response.getBody();
            if (body != null && body.get("products") instanceof List) {
                promoProducts = (List<Map<String, Object>>) body.get("products");
            }
        } catch (Exception e) {}
        model.addAttribute("featuredProducts", allProducts != null && allProducts.size() > 0 ? allProducts.subList(0, Math.min(3, allProducts.size())) : new ArrayList<>());
        model.addAttribute("popularProducts", allProducts != null && allProducts.size() > 3 ? allProducts.subList(3, Math.min(6, allProducts.size())) : new ArrayList<>());
        model.addAttribute("promoProducts", promoProducts != null ? promoProducts : new ArrayList<>());
        return "index";
    }

    @GetMapping("/products")
    public String showPublicProducts(Model model) {
        addCategoriesToModel(model);
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/product";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Map<String, Object>> products = new ArrayList<>();
        try {
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, org.springframework.http.HttpMethod.GET, entity, Map.class);
            Map body = response.getBody();
            if (body != null && body.get("products") instanceof List) {
                products = (List<Map<String, Object>>) body.get("products");
            }
        } catch (Exception e) {}
        model.addAttribute("products", products);
        return "public/product_list";
    }

    @GetMapping("/product/{id}")
    public String showPublicProductDetail(@PathVariable Long id, Model model) {
        addCategoriesToModel(model);
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8080/api/product/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> product = new HashMap<>();
        try {
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                apiUrl,
                org.springframework.http.HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            product = response.getBody();
        } catch (Exception e) {}
        model.addAttribute("product", product);
        return "public/product";
    }

    @GetMapping("/cart")
    public String showPublicCart(Model model) {
        addCategoriesToModel(model);
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
